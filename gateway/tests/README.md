# Gateway regression checks

From the repository root, run:

```sh
python3 gateway/tests/run_regression.py
```

Requirements: Python 3.9+ and an installed JDK 21. No global Maven, Python
packages, Redis, or manually started backend is needed. On macOS the harness
finds JDK 21 using `/usr/libexec/java_home -v 21`. If `JAVA_HOME` is set it must
point to JDK 21; on other systems set it or put JDK 21 on PATH. The harness
checks the Java/runtime compiler versions and invokes `gateway/mvnw package`
with that JDK. Maven may download the existing project's dependencies on a
first run; no dependency declarations are changed.

The command exits zero only when all assertions pass. It prints a unique
`gateway/target/regression/run-*` directory containing the Maven build log,
gateway startup logs, backend receipt log, generated test configuration, and
`results.json`. These artifacts are already ignored through `gateway/target/`.

## What it verifies

1. **Sequential:** a fresh JVM admits five requests (HTTP 200 with backend JSON),
   rejects requests six and seven (HTTP 429), receives exactly five requests at
   the backend, and keeps health UP after exhaustion.
2. **Restart:** after stopping that exhausted JVM, the same JAR/configuration
   gets a new JVM and a fresh allowance. Repeat the seven-request assertions
   and check five additional backend receipts. The backend stays alive.
3. **Concurrency:** another fresh JVM handles 100 requests submitted to 20
   worker threads. The first 20 workers wait together at a start gate. Require
   exactly five HTTP 200s, 95 HTTP 429s, five backend receipts, and health UP.

Each scenario uses a fresh limiter because each has its own gateway process.
The fixture counts actual received requests under a lock. Worker exceptions
and HTTP transport errors fail the run; HTTP errors such as 429 are explicitly
read and checked. Assertions use explicit checks, so Python's `-O` option
cannot silently disable them.

## Isolation and cleanup

The fixture binds to loopback port zero, letting the OS assign its port. The
gateway does the same. Readiness is determined by its own startup log and a
successful HTTP health response, with a bounded deadline and polling; there
is no fixed startup sleep. Requests and worker waits also have timeouts.

The harness reads `application.properties` from the JAR it just built and
writes a test-only copy. It changes the backend URI and listening port, and
adds loopback binding and plain logs. The existing predicates, filter, limit,
identity, and health settings are retained. Production files are never edited.
Using a full configuration copy also avoids accidentally replacing Gateway's
route list with a partial higher-precedence property override. See Spring's
[external configuration documentation](https://docs.spring.io/spring-boot/reference/features/external-config.html).

Only processes created by the harness are stopped. Existing services on 8080
and 8081 are untouched. Cleanup runs on assertion failures and Ctrl-C as well;
if a test-owned JVM ignores termination it is killed after a bounded wait.
Normal completion stops all test gateway processes and the backend fixture.
The script targets macOS/Linux (`mvnw`), not Windows.

## Review together

Start with `test_sequential()`: its expected status list and backend count are
the acceptance criteria, separate from process setup. Then review
`test_concurrent()`: the start gate creates contention, and collecting every
future propagates failures. No new Java concepts or production code are used.
These are correctness regressions, not load benchmarks or proof of all thread
schedules. Fixed identity, process-local counters, and no reset except a fresh
limiter remain the application's intentional limitations.
