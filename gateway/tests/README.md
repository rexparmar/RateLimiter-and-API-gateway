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
deterministic token-bucket log, gateway startup logs, backend receipt log,
generated test configuration, and `results.json`. These artifacts are already
ignored through `gateway/target/`.

## What it verifies

1. **Deterministic token bucket:** the Java test harness uses an injected clock
   to verify initial capacity, exact one-second and fractional refill, capacity
   capping, independent clients, preserved credit after rejection, and exactly
   five admissions out of 100 concurrent requests while time is frozen.
2. **Sequential and refill:** a fresh JVM admits five immediate requests (HTTP
   200 with backend JSON), rejects requests six and seven (HTTP 429), and sends
   exactly five requests to the backend before refill. Bounded polling then
   requires a replenished request to return HTTP 200 and reach the backend.
   Health remains UP.
3. **Restart:** after stopping that JVM, the same JAR/configuration gets a new
   JVM and a fresh five-token bucket. The sequential and refill scenario is
   repeated while the backend stays alive.
4. **Concurrency:** another fresh JVM handles 100 requests submitted to 20
   worker threads. The first 20 workers wait together at a start gate. The
   admitted count must stay within the initial capacity plus the tokens that
   could legitimately refill during the measured run. Every other response
   must be HTTP 429, backend receipts must exactly equal admissions, and health
   must remain UP. The frozen-clock Java test supplies the exact 5/95 assertion.

Each HTTP scenario uses a fresh limiter because each has its own gateway
process. The fixture counts actual received requests under a lock. Worker
exceptions and HTTP transport errors fail the run; HTTP errors such as 429 are
explicitly read and checked. Assertions use explicit checks, so Python's `-O`
option cannot silently disable them.

## Isolation and cleanup

The fixture binds to loopback port zero, letting the OS assign its port. The
gateway does the same. Readiness and token replenishment are determined by
observable responses with bounded deadlines and polling; there is no fixed
startup or refill sleep. Requests and worker waits also have timeouts.

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

Start with `LocalRateLimiterDeterministicChecks`: its controlled clock makes the
algorithm boundaries exact. Then review `test_sequential()` for HTTP forwarding,
rejection, refill, backend receipts, and health. In `test_concurrent()`, the
start gate creates contention and collecting every future propagates failures.
These are correctness regressions, not load benchmarks or proof of all thread
schedules. Fixed identity and process-local state remain intentional limitations.
