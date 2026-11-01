#!/usr/bin/env python3
"""Build and test the real gateway using only Python's standard library."""

from collections import Counter
from concurrent.futures import ThreadPoolExecutor
from contextlib import contextmanager
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import threading
import time
import traceback
import urllib.error
import urllib.request
import zipfile


GATEWAY = Path(__file__).resolve().parents[1]
JAR = GATEWAY / "target/sentinel-gateway-0.0.1-SNAPSHOT.jar"
HELLO = {"message": "Hello from backend"}


def check(condition, message):
    if not condition:
        raise AssertionError(message)


def request(url):
    # Explicitly avoid system HTTP proxies for these isolated loopback requests.
    opener = urllib.request.build_opener(urllib.request.ProxyHandler({}))
    try:
        response = opener.open(url, timeout=10)
    except urllib.error.HTTPError as error:
        response = error  # A 429 is a response to inspect, not a transport failure.
    with response:
        return response.status, response.read().decode("utf-8")


def check_health(url):
    status, body = request(url + "/actuator/health")
    check(status == 200 and json.loads(body) == {"status": "UP"},
          "Health response was {}: {}".format(status, body))


class Backend:
    """A test-owned backend with synchronized, directly observable receipts."""

    def __init__(self, log_path):
        self.receipts = []
        self.lock = threading.Lock()
        fixture = self

        class Handler(BaseHTTPRequestHandler):
            def do_GET(self):
                with fixture.lock:
                    fixture.receipts.append(self.path)
                    with log_path.open("a") as log:
                        log.write(json.dumps({"method": "GET", "path": self.path}) + "\n")
                status = 200 if self.path == "/hello" else 404
                body = json.dumps(HELLO if status == 200 else {"error": "Not Found"}).encode()
                self.send_response(status)
                self.send_header("Content-Type", "application/json")
                self.send_header("Content-Length", str(len(body)))
                self.end_headers()
                self.wfile.write(body)

            def log_message(self, format, *args):
                pass

        self.server = ThreadingHTTPServer(("127.0.0.1", 0), Handler)
        self.url = "http://127.0.0.1:{}".format(self.server.server_port)
        self.thread = threading.Thread(target=self.server.serve_forever, daemon=True)

    def __enter__(self):
        self.thread.start()
        return self

    def __exit__(self, *error):
        self.server.shutdown()
        self.server.server_close()
        self.thread.join(timeout=10)
        check(not self.thread.is_alive(), "Backend fixture did not stop")

    def snapshot(self):
        with self.lock:
            return list(self.receipts)


def select_java():
    home = os.environ.get("JAVA_HOME")
    if not home and sys.platform == "darwin":
        home = subprocess.check_output(
            ["/usr/libexec/java_home", "-v", "21"], text=True).strip()
    if not home:
        executable = shutil.which("java")
        check(executable is not None, "Install Java 21 or set JAVA_HOME to an existing JDK 21")
        home = str(Path(executable).resolve().parent.parent)
    home = Path(home)
    java = home / "bin/java"
    version = subprocess.check_output([str(java), "-version"], stderr=subprocess.STDOUT, text=True)
    check(re.search(r'version "21(?:[.\"-])', version),
          "Java 21 is required; set JAVA_HOME to JDK 21. Found: " + version.strip())
    compiler = subprocess.check_output([str(home / "bin/javac"), "-version"], text=True)
    check(compiler.startswith("javac 21"), "A Java 21 compiler is required: " + compiler.strip())
    env = dict(os.environ, JAVA_HOME=str(home))
    env["PATH"] = str(home / "bin") + os.pathsep + env.get("PATH", "")
    return str(java), env, version.splitlines()[0]


def build(env, run_dir):
    command = ["./mvnw", "-B", "-ntp", "package"]
    with (run_dir / "build.log").open("w") as log:
        result = subprocess.run(command, cwd=GATEWAY, env=env,
                                stdout=log, stderr=subprocess.STDOUT, timeout=300)
    check(result.returncode == 0, "Maven build failed; inspect " + str(run_dir / "build.log"))
    check(JAR.is_file(), "Maven did not produce the expected executable JAR: " + str(JAR))
    print("PASS Maven Wrapper package", flush=True)


def write_test_config(run_dir, backend):
    # Copy the built app's whole configuration so route predicates/filters stay intact.
    with zipfile.ZipFile(JAR) as jar:
        config = jar.read("BOOT-INF/classes/application.properties").decode("utf-8")
    replacements = {
        "server.port": "0",
        "spring.cloud.gateway.server.webflux.routes[0].uri": backend.url,
    }
    for key, value in replacements.items():
        pattern = r"^" + re.escape(key) + r"\s*=.*$"
        config, count = re.subn(pattern, lambda match: key + "=" + value,
                                config, flags=re.MULTILINE)
        check(count == 1, "Expected exactly one packaged property: " + key)
    config += "\nserver.address=127.0.0.1\nspring.output.ansi.enabled=never\n"
    path = run_dir / "application.properties"
    path.write_text(config)
    return path


@contextmanager
def running_gateway(java, env, config, run_dir, scenario):
    log_path = run_dir / (scenario + ".log")
    command = [java, "-jar", str(JAR), "--spring.config.location=" + config.as_uri()]
    with log_path.open("w") as log:
        process = subprocess.Popen(command, cwd=GATEWAY, env=env, stdout=log,
                                   stderr=subprocess.STDOUT, start_new_session=True)
        try:
            deadline = time.monotonic() + 60
            url = None
            while time.monotonic() < deadline:
                check(process.poll() is None, "Gateway exited during startup; inspect " + str(log_path))
                match = re.search(r"Netty started on port (\d+)", log_path.read_text())
                if match:
                    url = "http://127.0.0.1:" + match.group(1)
                    try:
                        check_health(url)
                        break
                    except (OSError, ValueError, AssertionError):
                        pass
                # Poll an actual readiness condition; do not assume startup takes N seconds.
                time.sleep(0.05)
            else:
                raise AssertionError("Gateway readiness timed out; inspect " + str(log_path))
            yield url, process.pid
        finally:
            if process.poll() is None:
                process.terminate()
                try:
                    process.wait(timeout=15)
                except subprocess.TimeoutExpired:
                    process.kill()
                    process.wait(timeout=10)
            check(process.poll() is not None, "Test-owned gateway did not stop")


def check_backend(backend, before):
    receipts = backend.snapshot()[before:]
    check(receipts == ["/hello"] * 5, "Expected five backend receipts; got " + repr(receipts))
    return len(receipts)


def test_sequential(url, backend):
    before = len(backend.snapshot())
    responses = [request(url + "/hello") for _ in range(7)]
    statuses = [status for status, body in responses]
    check(statuses == [200, 200, 200, 200, 200, 429, 429],
          "Sequential statuses were " + repr(statuses))
    for status, body in responses[:5]:
        check(json.loads(body) == HELLO, "Admitted request did not return backend JSON: " + body)
    check_health(url)
    return {"statuses": statuses, "backend_receipts": check_backend(backend, before), "health": "UP"}


def test_concurrent(url, backend):
    before = len(backend.snapshot())
    workers = 20
    ready = threading.Semaphore(0)
    start = threading.Event()

    def send_request():
        ready.release()
        check(start.wait(timeout=15), "Concurrent request timed out waiting for start")
        return request(url + "/hello")

    with ThreadPoolExecutor(max_workers=workers) as executor:
        futures = [executor.submit(send_request) for _ in range(100)]
        try:
            # The first 20 tasks block, proving all 20 worker threads reached the gate.
            deadline = time.monotonic() + 15
            for _ in range(workers):
                check(ready.acquire(timeout=max(0, deadline - time.monotonic())),
                      "Workers did not reach the start gate")
        finally:
            start.set()  # Also release workers if readiness fails.
        responses = [future.result(timeout=20) for future in futures]
    counts = Counter(status for status, body in responses)
    check(counts == {200: 5, 429: 95}, "Concurrent statuses were " + repr(dict(counts)))
    for status, body in responses:
        if status == 200:
            check(json.loads(body) == HELLO, "Concurrent admission did not return backend JSON")
    check_health(url)
    return {"requests": 100, "workers": workers, "allowed": counts[200],
            "rejected": counts[429], "backend_receipts": check_backend(backend, before), "health": "UP"}


def main():
    results_root = GATEWAY / "target/regression"
    results_root.mkdir(parents=True, exist_ok=True)
    run_dir = Path(tempfile.mkdtemp(prefix="run-", dir=results_root))
    print("Logs and results: " + str(run_dir), flush=True)
    summary = {"passed": False, "scenarios": {}}
    try:
        java, env, version = select_java()
        summary["java"] = version
        print("Using " + version, flush=True)
        build(env, run_dir)
        with Backend(run_dir / "backend.jsonl") as backend:
            config = write_test_config(run_dir, backend)
            # Each context starts/stops its own JVM; the demo server is never touched.
            for name, test in [("sequential", test_sequential),
                               ("restart_reset", test_sequential),
                               ("concurrent", test_concurrent)]:
                with running_gateway(java, env, config, run_dir, name) as (url, pid):
                    result = test(url, backend)
                    result.update(gateway_pid=pid, gateway_url=url)
                summary["scenarios"][name] = result
                print("PASS " + name + ": " + json.dumps(result, sort_keys=True), flush=True)
            summary["total_backend_receipts"] = len(backend.snapshot())
        summary["passed"] = True
        print("ALL GATEWAY REGRESSION TESTS PASSED", flush=True)
        return 0
    except Exception as error:
        summary["error"] = str(error)
        traceback.print_exc()
        return 1
    finally:
        (run_dir / "results.json").write_text(json.dumps(summary, indent=2) + "\n")


if __name__ == "__main__":
    sys.exit(main())
