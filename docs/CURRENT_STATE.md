Sentinel — Current Project State

Last Updated: 2026-11-01
Repository: RateLimiter
Project: Sentinel — Distributed Rate Limiter & API Gateway

⸻

1. Current Position

* Phase: 1 — Java and Gateway Foundations
* Phase Status: In Progress
* Roadmap Weeks: 2–3 (planned learning sequence, not elapsed-time verification)
* Milestone: SENT-006 completed and approved; existing build and runtime evidence reviewed without repeating tests
* Active Ticket: None — SENT-006 closed; next engineering ticket awaits owner approval
* Status: Completed (SENT-006)

2. Current Objective

SENT-006 is Completed and approved, 2026-11-01, under the owner's explicit conditional closure instruction: close if existing acceptance evidence is sufficient (DOCUMENTED). Closure review confirmed all six criteria were already successfully executed against the reviewed integration (VERIFIED; sections 24–25). No application build or runtime tests were repeated. Two seven-request sequences separated by restart each returned five 200 responses and two 429 responses, with exactly five backend receipts; health remained UP and the Java 21 Maven package succeeded. No active next ticket is selected. PROPOSED next milestone: persist repeatable gateway regression checks and add concurrent HTTP admission coverage, subject to approval. SENT-005 remains completed/approved; SENT-003/SENT-004 closure remains unrecorded. No Redis, expiration, authentication, or dependency additions were implemented.

The project owner has approved the high-level architectural direction and instructed closure of SENT-002 (DOCUMENTED). The readiness review verified the required Phase 0 artifacts and consistency with REQUIREMENTS.md. Phase 0 is Completed against the roadmap's documented deliverables and review/approval rules; no separate Phase 0 acceptance checklist is present. No independent proficiency assessment or runtime validation is inferred from this completion.

The owner authorized and wrote a standalone exercise in excercise/sent-003-java-basics/src with file-by-file mentoring (DOCUMENTED: owner instruction; VERIFIED: reviewed source). Java 21 compilation and execution succeeded for both always-allow and always-reject implementations. This is guided practical evidence, not an independent proficiency assessment or a completed production rate limiter. See section 13 for commands, results, and limitations.

Strict shared enforcement applies during normal operation when Redis is available; when Redis is unavailable, the configured failure policy applies. Fail-open and local fallback may weaken global guarantees. The production rate-limiting algorithm, identity mechanism, load balancer, and Redis-failure policy remain explicitly undecided. Spring Boot 4.1.2, Spring Cloud 2025.1.4, and Gateway Server WebFlux 5.0.4 are the selected framework setup. SENT-006's fixed demo-client identity and local no-reset allowance are explicitly temporary learning behavior, not production identity verification or a distributed enforcement implementation.

3. Completed Work

* SENT-006 — Completed and approved by the owner, 2026-11-01 (DOCUMENTED: closure authorized after verifying existing acceptance evidence). Route-scoped filter, shared injected limiter, fixed demo-client, five-admission cap, backend exclusion, health, restart reset, and successful Java 21 package were reviewed (VERIFIED; sections 24–25). Assisted implementation and guided explanation are recorded; no independent proficiency claim or broader production readiness is inferred.
* SENT-005 — Completed and approved by the owner, 2026-10-31 (DOCUMENTED: explicit closure instruction). Standalone assisted implementation and JDK-only tests passed sequential boundaries, count preservation, independent clients, and 100-request concurrent scenarios (VERIFIED: section 21). No independent proficiency or exhaustive concurrency proof is inferred.
* Created the RateLimiter project directory.
* Created the docs directory.
* Initialized the local Git repository.
* Created AGENTS.md.
* Defined Codex’s learning-first mentoring instructions.
* SENT-000.2 — Completed and approved by the project owner.
* SENT-000.3 — Completed and approved by the project owner.
* SENT-000.4 — Context recovery test passed; completed and approved by the project owner.
* SENT-000.5 — Completed and approved by the project owner (DOCUMENTED: explicit owner instruction).
* SENT-000.6 — Documentation review and Git workflow completed (DOCUMENTED: project-owner instruction; VERIFIED: workflow documentation committed in 7637fa1). The review itself and a remote push have not been independently verified.
* SENT-000.7 — Completed and approved by the project owner (DOCUMENTED: explicit closure approval). Automatic context checkpointing is configured in AGENTS.md (VERIFIED).
* SENT-000.8 — Completed and approved by the project owner (DOCUMENTED: explicit closure approval). docs/PROJECT_ROADMAP.md contains all seven planned phases, topics, and deliverables (VERIFIED). At roadmap creation, Phase 0 was In Progress; later phases and all listed deliverables were Not Started.
* SENT-001 — Completed and approved by the project owner, 2026-10-23 (DOCUMENTED: explicit owner instruction). docs/REQUIREMENTS.md records FR-001 through FR-008, NFR-001 through NFR-008, and the clarified normal-operation and Redis-unavailable guarantees (VERIFIED). Assumptions, proposed initial exclusions, and open architectural choices remain subject to their stated review; no failure policy or implementation was selected.
* SENT-002 — Completed and approved by the project owner, 2026-10-26 (DOCUMENTED: explicit instruction). ARCHITECTURE.md contains the high-level Mermaid diagram, allow/reject flow, component and trust boundaries, shared-state requirements, and failure limitations (VERIFIED). Approval covers the high-level direction; explicitly open implementation details remain undecided.
* Phase 0 — Completed, 2026-10-26, following the authorized conditional readiness review. The requirements document, initial architecture diagram, and system boundaries are present (VERIFIED), with requirements and high-level architecture approval recorded from the project owner (DOCUMENTED). See section 12 for the evidence and limitations.
* SENT-003 partial progress — Reviewed the owner's RateLimiter interface, LocalRateLimiter always-allow stub, RejectAllRateLimiter, GatewayService constructor injection, and Main demonstration. Compiled all five files with javac 21.0.11 and executed Main with Java 21.0.11; both commands exited 0 and produced the expected true/false output (VERIFIED). SENT-003 remains In Progress pending further learning, review, and explicit closure approval.
* SENT-003 scaffold milestone, 2026-10-29 — Created the authorized initial gateway framework/configuration, downloaded dependencies, built the executable JAR with Java 21, started Netty on port 8080, and verified HTTP 200 with {"status":"UP"} from /actuator/health (VERIFIED). No gateway behavior was implemented. Owner review and SENT-003 closure approval remain pending.

4. Approved Technology Stack

* Language: Java 21
* Framework: Spring Boot 4.1.2 (approved for initialization; availability, build, and startup verified)
* Gateway: Spring Cloud Gateway Server WebFlux 5.0.4, managed by Spring Cloud BOM 2025.1.4 (approved for initialization; packaged dependency verified)
* Distributed State: Redis (approved in principle; see DEC-003)
* Build Tool: Maven 3.10.0 through Maven Wrapper 3.3.4 (verified with Java 21.0.11)
* Containerization: Docker Compose
* Testing: JUnit, Mockito, Testcontainers
* Monitoring: Micrometer, Prometheus, Grafana
* Load Testing: Locust
* CI: GitHub Actions

The scaffold's framework/build versions are selected and verified as recorded above. Future-phase dependency versions remain unselected; no Redis, authentication, routing, or custom-filter dependency was added.

5. Verified Repository State

* Git repository initialized on branch main.
* VERIFIED at the SENT-000.7 inspection: two commits exist, 75ee1e6 and 7637fa1; HEAD is 7637fa1 (docs: establish engineering workflow).
* VERIFIED at the SENT-000.7 inspection: the working tree was clean before editing; local main and the cached origin/main reference both pointed to 7637fa1.
* UNKNOWN: live remote state and push execution; no remote query was performed.
* AGENTS.md exists and contains content.
* docs/CURRENT_STATE.md exists and contains content.
* docs/DECISIONS.md exists and contains content.
* docs/PROJECT_ROADMAP.md exists and contains content (VERIFIED during SENT-000.8).
* At the SENT-000.4 inspection, all three context files had unstaged modifications.
* Initial Spring Boot gateway scaffold exists under gateway/ and has a verified executable build and health response (section 14). The owner subsequently added a configuration-only GET /hello route with verified forwarding (section 16). No rate limiting, Redis, authentication, or custom filters were implemented.
* Owner-written standalone learning code under excercise/sent-003-java-basics/src remains separate and was not modified during scaffold initialization.
* No tests executed during the SENT-000.4 recovery or SENT-000.5 review.
* VERIFIED during SENT-000.7: only AGENTS.md and docs/CURRENT_STATE.md were modified; docs/DECISIONS.md and the Git index were unchanged. Permission rules were reviewed and git diff --check passed.
* No tests or benchmarks executed during SENT-000.7; no new learning proficiency claims made.
* VERIFIED at the SENT-000.8 inspection: HEAD remained 7637fa1; AGENTS.md and docs/CURRENT_STATE.md already had unstaged modifications. These prior changes were preserved.
* VERIFIED during SENT-000.8: reviewed roadmap alignment with DEC-001 through DEC-006; open architectural decisions remain unresolved. git diff --check passed. AGENTS.md, docs/DECISIONS.md, and the Git index remained unchanged during this ticket.
* No application tests or benchmarks executed during SENT-000.8; no learning proficiency assessment performed.
* VERIFIED during final context synchronization: reviewed all four context files; synchronized ticket closures and the SENT-001 transition in this file and the roadmap. git diff --check passed. AGENTS.md, docs/DECISIONS.md, and the Git index remained unchanged during synchronization.
* No application tests, benchmarks, or proficiency assessments executed during final context synchronization. Requirements drafting remains the next action.
* VERIFIED at the 2026-10-23 requirements-draft inspection: the working tree was clean; HEAD was afa556a (docs: establish automated project context management). Live remote state and push execution were not independently verified.
* docs/REQUIREMENTS.md exists and records the owner-approved requirements direction with the operating-mode clarification (VERIFIED: file contents; DOCUMENTED: owner approval, 2026-10-23). No application code or build configuration was present in the inspected repository.
* VERIFIED during requirements drafting: reviewed all 16 requirement entries and proposal/decision boundaries; git diff --check passed. AGENTS.md, docs/DECISIONS.md, and the Git index were unchanged. Updated only requirements documentation and progress records.
* No application tests, benchmarks, or proficiency assessments executed during requirements drafting.
* VERIFIED at SENT-001 closure, 2026-10-23: reviewed requirements consistency across normal operation and Redis-unavailable behavior; all eight FR and eight NFR entries are present exactly once. git diff --check passed. AGENTS.md, docs/DECISIONS.md, and the Git index remained unchanged during clarification and closure synchronization.
* No application tests, benchmarks, or proficiency assessments executed during SENT-001 closure. No failure policy was selected or implemented; no architecture drafting was performed.
* VERIFIED at the 2026-10-26 architecture-draft inspection: the working tree was clean; HEAD was f18fecb (SENT-01: Defining functional requirements and non-functional requirements and doc finishing). The repository contained documentation and no application code or build configuration. Live remote state and push execution were not independently verified.
* docs/ARCHITECTURE.md exists and contains a proposed draft (VERIFIED, 2026-10-26). No application tests, benchmarks, or proficiency assessments executed during drafting.
* VERIFIED during architecture drafting: reviewed consistency with REQUIREMENTS.md and DEC-001 through DEC-006; git diff --check passed and new-file whitespace was clean. Mermaid source was reviewed but not rendered. AGENTS.md, docs/DECISIONS.md, docs/REQUIREMENTS.md, and the Git index remained unchanged; only the new architecture document and progress records changed.

File contents and current Git status must be verified by Codex before reporting them as current facts.

6. Current Restrictions

* Explicit initialization authorization covers the minimal gateway/ scaffold, Maven Wrapper, Gateway Server WebFlux, Actuator, and their dependency downloads. The owner will implement actual gateway behavior with mentoring.
* SENT-006 authorizes explained, incremental assisted coding: the owner explicitly requested Codex write one class/method at a time and explain decisions. Scope remains a route-scoped filter, shared injected in-memory limiter, fixed demo-client identity, and specified verification. Pause for understanding between teaching steps. No Redis, expiration, authentication, unrelated changes, or dependency additions are authorized.
* Do not implement future-phase features.
* Automatic progress updates to this file and progress indicators in docs/PROJECT_ROADMAP.md, once it exists, have standing project-owner approval under AGENTS.md. Other file changes require explicit approval.
* Application code, dependency changes, architectural decisions, scope or learning-objective changes, ticket closure, commits, pushes, and destructive operations still require explicit project-owner approval.
* Work on one micro-task at a time.

7. Current Blockers

No blocker identified for continuing the standalone Java exercise. Java 21.0.11 runtime and compiler are usable through their explicit JDK paths; the default java and javac commands point to Java 25.0.3, so they were not used for the verified Java 21 run.

VERIFIED setup inspection, 2026-10-29: global mvn was unavailable on PATH. This is resolved for the project by the working gateway/mvnw wrapper, which downloaded Maven 3.10.0 and dependencies to the local Maven cache. Explicit JAVA_HOME selects Java 21.0.11; the system default Java remains 25.0.3.

The DEC-002 initialization gate is satisfied by the owner's explicit setup approval, official compatibility/availability checks, and successful build/startup. DECISIONS.md still retains its earlier open questions about framework versions and Gateway variant; their resolved scaffold selections are recorded here without editing that decision log. The algorithm, identity mechanism, configuration/time semantics, load balancer/health checks, Redis atomic operations and outage/recovery policies remain open.

No current initialization blocker remains. Framework startup emits Hibernate Validator warning HV000271 about deprecated container-level @Valid on Gateway's routes configuration. It did not prevent startup or health verification; no framework version override or warning suppression was introduced. Sandbox restrictions initially prevented dependency-cache writes and port binding; the approved commands succeeded when rerun with the required environment permissions.

Documentation discrepancy: ARCHITECTURE.md retains its PROPOSED and approval-pending drafting language, including its final SENT-002 status. The owner's subsequent approval and ticket closure are recorded here and in PROJECT_ROADMAP.md. Architectural source content was preserved; no architectural decision was changed. Mermaid rendering remains unverified, distinct from verification that diagram source is present.

8. Next Micro-Task

PROPOSED next implementation milestone: reproducible gateway regression checks. (1) Save the existing HTTP/backend-receipt/health/restart checks as one runnable harness with isolated startup and cleanup. (2) Add a 100-request concurrent HTTP scenario asserting five admissions, 95 rejections, and only five backend receipts. (3) Document and run the harness with Java 21 and Maven as a repeatable verification workflow. No new ticket, source change, dependency, or execution of that proposed work is authorized by closure alone. At the last runtime verification, the demo allowance was exhausted and both processes were left running; no fresh availability check was performed at closure.

Official Spring documentation and release records establish the approved compatibility line; exact parent/BOM artifact downloads returned HTTP 200, and the resulting build and startup passed. Section 14 records sources and actual results. Constructor null-validation and independent compiler/runtime explanation remain unverified; they were not silently marked complete when the owner requested moving to the actual project.

The roadmap records the owner-approved learning scope. Further behavior and open architectural decisions remain subject to the learning protocol and applicable owner authorization.

Phase 0 and SENT-002 remain Completed. The recorded overall phase remains Phase 1 In Progress; SENT-005 is a limited rate-limiting/concurrency exercise without asserting completion of Phase 1 or a formal phase transition. SENT-003 and SENT-004 closure approvals remain unrecorded. Routing has historical execution evidence, and the standalone limiter now has passing sequential/concurrent checks; gateway-specific automated tests and deliverable acceptance remain outstanding.

9. Learning Progress

Concepts Introduced

* Git repository initialization
* Working directory
* Staging area
* Commits
* Repository-based context management
* Codex instruction files
* Interface contracts, implementing classes, polymorphic delegation, constructor dependency injection, and Java object construction (VERIFIED: guided code review and exercise execution; initial conceptual exercise completion is owner-reported).
* Java compiler versus runtime, explicit JDK selection, and compiler output/classpath separation (introduced during the verified compile/run workflow; owner explanation pending).
* Spring Boot entry-point/autoconfiguration, Gateway Server WebFlux and Netty, Actuator health, Maven Wrapper, parent/BOM dependency management, and executable-JAR startup (introduced through the scaffold; owner understanding not yet independently assessed).

Concepts Requiring Further Practice

* Git staging and committing
* Architecture Decision Records
* Professional engineering ticket workflows
* Independently assembling objects through constructor injection and explaining the dispatch path without hints.
* Null-dependency behavior, broader test cases, and distinguishing a demonstration from automated tests or a real rate limiter.

No independent proficiency assessment has been completed.

10. Session Handoff Rules

At the end of every meaningful coding session:

1. Inspect relevant repository changes.
2. Identify completed and incomplete work.
3. Record actual test results.
4. Identify unresolved problems.
5. Automatically update this document for meaningful progress under AGENTS.md's Automatic Context Checkpointing rules; update roadmap progress indicators once that file exists.
6. Preserve unrelated changes. Obtain explicit approval for ticket closure and actions outside the standing documentation authorization.

Do not mark tasks complete without verification and project-owner approval.

⸻

11. Next Session Startup

Codex must:

1. Read AGENTS.md.
2. Read this document.
3. Inspect Git status.
4. Review relevant decisions.
5. Identify the current ticket.
6. Report any inconsistencies.
7. Propose exactly one next micro-task.
8. Wait for approval before work outside existing authorization. Automatic context checkpoints require no separate approval.

12. Phase 0 Readiness Review — 2026-10-26

The roadmap lists three required Phase 0 deliverables. No additional phase-specific acceptance checklist was found; the review applied those deliverables and the existing verification, review, and owner-approval rules without inventing an implementation or proficiency gate.

| Required deliverable | Evidence | Approval |
| --- | --- | --- |
| Requirements document | VERIFIED: REQUIREMENTS.md contains FR-001 through FR-008, NFR-001 through NFR-008, and the operating-mode clarification. | DOCUMENTED: SENT-001 completed and approved. |
| Initial architecture diagram | VERIFIED: ARCHITECTURE.md section 2 contains Mermaid source for clients, ingress, multiple gateways, identity verification, admission, Redis, and the backend. Rendering was not independently verified. | DOCUMENTED: owner approved the high-level architectural direction and instructed SENT-002 closure. |
| Approved system boundaries | VERIFIED: ARCHITECTURE.md sections 4–8 describe component ownership, trust boundaries, state ownership, and failure limitations. | DOCUMENTED: owner approved the high-level direction; implementation selections remain open. |

VERIFIED through document review: the architecture preserves independent client accounting, trusted identity, atomic shared admission while Redis is available, no backend forwarding for exhausted rejections, no extra allowance charge for those rejections, consumption at admission regardless of backend response status, and forwarding for admitted requests. Its Redis-outage qualifications agree with REQUIREMENTS.md. No behavioral contradiction was found. No load-balancer product, authentication implementation, algorithm, failure policy, or framework version was selected.

Owner approval is DOCUMENTED; artifact presence and textual consistency are VERIFIED. Runtime behavior, performance, diagram rendering, and independent learning proficiency remain UNKNOWN or unassessed. Only progress documentation was changed during this review; no application tests, benchmarks, dependency changes, or implementation work were performed.

VERIFIED: git diff --check passed. AGENTS.md, REQUIREMENTS.md, ARCHITECTURE.md, and DECISIONS.md contents remained unchanged during this review. HEAD changed from f18fecb at startup to 5e9a804 (Finished the architecture diagram designing.) during the review; the Git index also changed, while the final staged diff was empty. No staging, commit, or push commands were run as part of this review. Only CURRENT_STATE.md and PROJECT_ROADMAP.md had uncommitted changes at final inspection; live remote state was not independently checked.

13. SENT-003 Standalone Java Exercise Checkpoint

DOCUMENTED: The owner reported completing initial conceptual exercises on interfaces, polymorphism, and constructor dependency injection, and authorized a small Java 21 exercise with the owner writing each file. Codex supplied explanations, hints, and an empty Main entry-point scaffold; the owner wrote and corrected the exercise behavior during guided review. No independent proficiency assessment has been completed.

VERIFIED source: excercise/sent-003-java-basics/src contains RateLimiter.java, LocalRateLimiter.java, RejectAllRateLimiter.java, GatewayService.java, and Main.java. The actual directory spelling is excercise. The interface method is boolean processRequest(String clientId). LocalRateLimiter is a temporary always-allow stub; RejectAllRateLimiter always rejects. Main constructs both objects and injects each into a separate GatewayService. GatewayService stores a final interface-typed field and delegates the decision.

VERIFIED environment: default java/javac resolve to version 25.0.3; the explicit Temurin JDK at /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home provides java and javac 21.0.11.

Executed from excercise/sent-003-java-basics/src:

```text
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/javac --release 21 -d /private/tmp/sentinel-sent003.4QY3Y4 RateLimiter.java LocalRateLimiter.java RejectAllRateLimiter.java GatewayService.java Main.java
```

Compilation result: exit 0, no diagnostics. Class files were written to the temporary directory, outside the repository. Executed:

```text
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java -cp /private/tmp/sentinel-sent003.4QY3Y4 Main
```

Execution result: exit 0. Observed standard output:

```text
Local limiter allowed : true
Reject all limiter allowed : false
```

VERIFIED guided comprehension check: after discussion of the delegation path, the owner correctly stated that request evaluation happens when the method is called. The constructor stores the supplied limiter; the later processRequest call invokes its behavior. This is a specific response checked during mentoring, not an independent proficiency assessment. The owner's explanation of compilation versus execution remains pending.

DOCUMENTED owner reproduction: following supplied Java 21 commands, the owner reported silent compilation into /private/tmp/sentinel-sent003-classes and pasted the Java run command with output Local limiter allowed : true and Reject all limiter allowed : false. VERIFIED comparison: the pasted output matches the earlier directly observed smoke demonstration. This records reproduction with guidance, not independent command authoring or proof of broader test coverage.

At the standalone exercise checkpoint, this verified one smoke demonstration for client-1 using two stubs. No automated tests, null-input/dependency checks, per-client allowance accounting, time-window behavior, concurrency tests, Redis integration, HTTP forwarding, Spring Boot execution, performance measurements, or independent learning assessment had been completed. The Phase 1 Spring Boot/forwarding/automated-test deliverables and the Phase 2 real local-limiter deliverable were Not Started at that checkpoint. Section 14 records subsequent Spring Boot scaffold progress; SENT-003 is not closed.

14. SENT-003 Gateway Initialization Checkpoint — 2026-10-29

DOCUMENTED authorization: the owner explicitly approved generating the minimal framework scaffold/configuration and downloading its dependencies, with actual gateway behavior reserved for owner implementation. This supersedes the earlier initialization restrictions only for the approved scope. Existing changes in CURRENT_STATE.md, PROJECT_ROADMAP.md, and the owner's untracked excercise/ directory were preserved.

Verified framework availability and compatibility sources:

* [Spring Boot 4.1.2 official release](https://github.com/spring-projects/spring-boot/releases/tag/v4.1.2) and [Java/system requirements](https://docs.spring.io/spring-boot/system-requirements.html): this release supports Java 21.
* [Spring Cloud compatibility table](https://spring.io/projects/spring-cloud/): Cloud 2025.1.x supports Boot 4.1.x starting with Cloud 2025.1.2.
* [Spring Cloud 2025.1.4 official release](https://github.com/spring-cloud/spring-cloud-release/releases/tag/v2025.1.4): includes Gateway 5.0.4.
* Exact Maven Central availability checks returned HTTP 200 for spring-boot-starter-parent:4.1.2 and spring-cloud-dependencies:2025.1.4. Spring Initializr also generated this exact parent/BOM pair successfully.

VERIFIED resolved versions: Java 21.0.11 (Temurin), Maven 3.10.0, Maven Wrapper 3.3.4, Spring Boot/Actuator 4.1.2, Cloud BOM 2025.1.4, Gateway Server WebFlux 5.0.4, Spring Framework 7.0.10. Dependency versions were confirmed from the build/runtime and packaged JAR rather than assumed from labels alone.

Created scaffold files:

* gateway/pom.xml — Boot parent, Java target, Cloud BOM, the two runtime starters, and Boot packaging plugin.
* gateway/mvnw and gateway/mvnw.cmd — official wrapper scripts from Spring Initializr, with the Unix script executable.
* gateway/.mvn/wrapper/maven-wrapper.properties — pins the wrapper and Maven distribution; no global Maven installation is required.
* gateway/.gitignore and gateway/.gitattributes — keep target/ build output out of Git and define wrapper line endings.
* gateway/src/main/java/com/sentinel/gateway/SentinelGatewayApplication.java — generated @SpringBootApplication entry point calling SpringApplication.run; it starts the Spring context and auto-configured Netty HTTP server.
* gateway/src/main/resources/application.properties — application name, port 8080, health-only Actuator exposure, hidden health details, and disabled optional health probe groups to match the exact requested body.

The only direct runtime dependencies are spring-cloud-starter-gateway-server-webflux (gateway/reactive HTTP infrastructure, including transitive WebFlux/Reactor/Netty) and spring-boot-starter-actuator (built-in operational health endpoint). No additional application dependency or test starter was added. No routing configuration, custom filters, rate limiter, Redis integration, authentication, or custom health controller was implemented.

Executed from gateway/:

```sh
env JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw -version
env JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw -B -ntp package
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java -jar target/sentinel-gateway-0.0.1-SNAPSHOT.jar
```

VERIFIED build result: exit 0, BUILD SUCCESS, compiled with release 21, executable artifact target/sentinel-gateway-0.0.1-SNAPSHOT.jar. The initial build downloaded dependencies; the final rebuild included the health-response configuration adjustment. Maven reported No tests to run: no automated tests were created, skipped, or claimed as passed.

VERIFIED startup: final process PID 30049 logged Java 21.0.11, Netty started on port 8080 (http), and Started SentinelGatewayApplication. It was left running after verification; this is an observed local process, not a guarantee of future availability. To start through the wrapper in a terminal when port 8080 is free, use the same JAVA_HOME with ./mvnw spring-boot:run. The packaged-JAR startup above is the path directly exercised during verification.

Executed HTTP verification:

```sh
curl -sS -i --max-time 10 http://localhost:8080/actuator/health
```

Observed final response (curl exit 0):

```text
HTTP/1.1 200 OK
Content-Type: application/vnd.spring-boot.actuator.v3+json
Content-Length: 15

{"status":"UP"}
```

The first health check returned HTTP 200 with status UP and additional liveness/readiness groups. [Spring Boot documents](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) the management.endpoint.health.probes.enabled switch; setting it false, rebuilding, and restarting produced the exact final body above. Health exposure and its output are verified; this does not verify any future gateway forwarding or rate-limiting behavior.

Remaining issues: the upstream HV000271 routes-validation deprecation warning is present. Initial sandbox cache/network/port restrictions were resolved through required execution permissions. Existing architecture/decision documents retain historical draft/open-version metadata; this checkpoint records the current explicit approval and verified choices without changing those documents. No runtime blocker remains for the scaffold, and no ticket closure or owner proficiency is inferred.

Final verification: git diff --check passed. Git status showed only the pre-existing progress-document edits and untracked excercise/ plus the new gateway/. gateway/target/ is ignored. Hash comparison confirmed AGENTS.md, DECISIONS.md, REQUIREMENTS.md, ARCHITECTURE.md, the standalone exercise source, and the Git index were unchanged during initialization. No files were staged, committed, or pushed. lsof confirmed PID 30049 listening on port 8080 after the successful health request.

15. SENT-004 Basic HTTP Routing — Preparation Checkpoint, 2026-10-29

DOCUMENTED authorization: the owner started SENT-004, requested a minimal local JSON backend, reserved route implementation for themselves, and authorized actual HTTP checks of forwarding, unknown paths, and backend unavailability. SENT-004 is In Progress; SENT-003 formal closure remains unrecorded.

VERIFIED inspection: read AGENTS.md, CURRENT_STATE.md, DECISIONS.md, PROJECT_ROADMAP.md, gateway/pom.xml, and application.properties. Git status before changes showed existing modified progress documents and untracked excercise/ and gateway/ directories; those changes were preserved. lsof showed the existing Java process PID 30049 listening on 8080, with 8081 free. Python 3.9.6 is available.

VERIFIED configuration compatibility: inspected META-INF/spring-configuration-metadata.json inside the packaged spring-cloud-gateway-server-webflux-5.0.4.jar. The route list property is spring.cloud.gateway.server.webflux.routes. [Spring's 5.0.4 property reference](https://docs.spring.io/spring-cloud-gateway/reference/appendix.html) agrees. The [predicate reference](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/request-predicates-factories.html) documents Path and Method predicates and their logical AND combination. No route configuration was generated or added.

Created a temporary test fixture outside the repository at /private/tmp/sentinel-sent004-backend.py using Python's standard-library HTTPServer. It binds only to 127.0.0.1:8081 and returns JSON for GET /hello; it is not production application code or a dependency addition. Launch command:

```sh
python3 /private/tmp/sentinel-sent004-backend.py
```

The sandbox initially denied port binding; the same command started successfully with the required execution permission. The temporary file may need recreation after temporary-directory cleanup. The backend was left running at this checkpoint.

Executed:

```sh
curl -sS -i --max-time 10 http://127.0.0.1:8081/hello
```

VERIFIED result: curl exit 0; HTTP/1.0 200 OK; Content-Type: application/json; response body {"message":"Hello from backend"}. This verifies only the backend directly, not gateway forwarding.

UNKNOWN at this preparation checkpoint: GET /hello through 8080, GET /unknown through 8080, and GET /hello through 8080 while the backend is unavailable. Section 16 records their subsequent execution. No automated tests, performance measurements, routing proficiency assessment, or ticket closure were claimed. No production source or route configuration was modified during preparation; repository edits were limited to the two progress documents. No staging, commit, or push was performed.

16. SENT-004 Owner Route Review and HTTP Verification — 2026-10-29

VERIFIED source review: the owner added route hello-route in gateway/src/main/resources/application.properties, targeting http://127.0.0.1:8081 with Path=/hello and Method=GET under spring.cloud.gateway.server.webflux.routes[0]. Initial lowercase predicate names and then whitespace in Method = GET were corrected by the owner after review. Inspection of PredicateDefinition bytecode from the installed Gateway 5.0.4 JAR confirmed that the predicate name before the inner equals sign is retained without trimming. Codex did not write or edit the owner's route configuration or Java source.

Executed from gateway/:

```sh
env JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw -B -ntp package
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java -jar target/sentinel-gateway-0.0.1-SNAPSHOT.jar
```

VERIFIED build: exit 0, BUILD SUCCESS; saved configuration packaged into the executable JAR. Maven reported No tests to run. The first sandboxed build failed to access its Maven cache lock; the authorized build succeeded with the required execution permission. No dependency declaration changed. The previous gateway process was stopped; the new Java 21.0.11 process PID 31334 logged Netty started on port 8080 and successful application startup.

Executed HTTP checks, each with curl exit 0 (an HTTP error response is not a curl transport failure without --fail):

```sh
curl -sS -i --max-time 10 http://127.0.0.1:8080/hello
curl -sS -i --max-time 10 http://127.0.0.1:8080/unknown
# Stopped the temporary backend, confirmed no listener on 8081, then repeated:
curl -sS -i --max-time 10 http://127.0.0.1:8080/hello
# Restarted the same temporary backend with python3 /private/tmp/sentinel-sent004-backend.py:
curl -sS -i --max-time 10 http://127.0.0.1:8080/hello
```

VERIFIED responses:

| Check | Actual HTTP status | Actual JSON response body |
| --- | --- | --- |
| GET /hello with backend available | 200 OK | {"message":"Hello from backend"} |
| GET /unknown | 404 Not Found | {"timestamp":"2026-10-29T15:44:09.728Z","path":"/unknown","status":404,"error":"Not Found","requestId":"ef42f9cb-2"} |
| GET /hello with backend stopped | 500 Internal Server Error | {"timestamp":"2026-10-29T15:44:19.381Z","path":"/hello","status":500,"error":"Internal Server Error","requestId":"1ab42e15-3"} |
| GET /hello after backend restart | 200 OK | {"message":"Hello from backend"} |

The backend request log confirmed receipt of the forwarded GET /hello. Sentinel logged Connection refused: /127.0.0.1:8081 for the outage request. HTTP 500 is the observed current behavior, not a selected production failure policy; no retries, fallback, circuit breaker, or custom error handling was implemented. The backend and gateway were left running after the restoration check.

Warnings and limits: HV000271 deprecation warnings were logged for routes, filters, and predicates; they did not prevent startup or forwarding. The old gateway's shutdown logged NoClassDefFoundError: ch/qos/logback/classic/spi/ThrowableProxy after the JAR rebuild; its root cause remains UNKNOWN. The old process exited and the new process started and served the checked requests successfully. These checks do not establish graceful shutdown or recovery guarantees. No automated tests, performance measurements, broader method/path coverage, or independent learning assessment were executed.

SENT-004 remained In Progress at this verification checkpoint, pending the owner's explanation of predicates, URI, and the observed response differences, review of results, and explicit closure approval. SENT-003 formal closure remained unrecorded. Codex modified only CURRENT_STATE.md and PROJECT_ROADMAP.md during this verification; generated build output is under ignored gateway/target/. Existing changes, the owner route, and the standalone exercise were preserved. No files were staged, committed, or pushed.

17. SENT-005 Preparation Checkpoint — 2026-10-31

DOCUMENTED: the owner authorized beginning the in-memory implementation exercise and will write the admission algorithm. Required behavior: independent per-client counters, first five requests admitted, subsequent requests rejected without increasing the count, ConcurrentHashMap with an atomic per-key update, and no time-window reset. This is a bounded local learning exercise, not the eventual distributed production policy.

VERIFIED inspection: read AGENTS.md, CURRENT_STATE.md, DECISIONS.md, PROJECT_ROADMAP.md, gateway/pom.xml, and the existing exercise's RateLimiter, LocalRateLimiter, and GatewayService. Git was clean on main at HEAD df26a7c (Excercise and springboot setup) before progress updates. The old LocalRateLimiter returns true unconditionally. The gateway POM has Gateway and Actuator runtime starters but no test dependencies. No new limiter or tests are present in the inspected file inventory. No live remote state or push execution was checked.

VERIFIED commands: the explicit Temurin JDK's java -version and javac -version both report 21.0.11 at /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home. No application compilation, HTTP checks, or limiter tests were executed during preparation.

PROPOSED location: excercise/sent-005-in-memory-rate-limiter/src, preserving the existing directory spelling and the SENT-003 exercise. Keep this exercise separate from production gateway/src/main/java. Reuse the existing interface contract boolean processRequest(String clientId); create a LocalRateLimiter implementation with a private per-instance ConcurrentHashMap<String, Integer> and a cap of five. Later add a plain Java test harness so sequential and concurrent checks can run without new dependencies. No exercise directory or source file has been created by Codex.

Planned verification, not results: admit the first five requests and reject subsequent ones; verify rejected requests leave the count at five; prove different clients have independent allowances; synchronize concurrent requests against one shared limiter instance and verify exactly five admissions per client. Tests must collect worker outcomes/errors and finish before results are assessed. The owner will explain their approach, write the logic, and receive review before execution. Correctness, concurrency results, and learning proficiency remain UNKNOWN until verified.

The Java 21 ConcurrentHashMap API documents compute() as an atomic invocation for a key: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html#compute(K,java.util.function.BiFunction). This supports the proposed approach; thread-safe individual get/put operations alone do not make a compound admission decision atomic.

Only CURRENT_STATE.md and roadmap progress information were updated during preparation. No source, dependencies, architectural decisions, staging, commits, or pushes were changed. Prior metadata discrepancies in DECISIONS.md and ARCHITECTURE.md remain as described above; formal SENT-003/SENT-004 closure is still unrecorded.

18. SENT-005 First Implementation Review — 2026-10-31

VERIFIED: the owner created RateLimiter.java and LocalRateLimiter.java directly under excercise/sent-005-in-memory-rate-limiter/. The interface matches the existing boolean processRequest(String clientId) contract. The implementation declares a ConcurrentHashMap<String, Integer>, reads with get(), rejects a count equal to five, and attempts requestMap.get(clientId)++ before returning true. No compute() call or tests are present.

Executed javac --release 21 with the explicit Temurin 21.0.11 compiler against these two source files, directing any class output to a fresh /private/tmp/sentinel-sent005-review.* directory. VERIFIED result: exit 1; LocalRateLimiter.java:12: error: unexpected type; required: variable; found: value. Incrementing a method's return value is not valid Java.

Additional VERIFIED source findings, not executed runtime outcomes: get() returns null for an absent client, which would be unboxed into int and throw NullPointerException if reached after compilation is corrected. Separate get/check/update steps do not provide the required atomic admission decision. No missing-client initialization is implemented; the map field is package-visible rather than private. Runtime behavior and concurrency correctness remain UNKNOWN because compilation failed; no limiter tests were run.

Next action at this first-review checkpoint: the owner revises the method around compute(), handling the missing entry and preserving the count on rejection, with review before tests. No complete admission algorithm had been supplied at that point. Codex modified only progress documentation during that review; owner source was preserved. No dependency, architectural decision, staging, commit, or push changed.

19. SENT-005 Explicitly Requested Compute Rewrite — 2026-10-31

DOCUMENTED authorization: after several owner-written revisions and reviews, the owner explicitly requested, "rewrite compute for me". This authorizes the assisted algorithm rewrite under the learning protocol's explicit-request exception; no independent implementation achievement is inferred.

VERIFIED changes: Codex updated only the method's compute() callback, added a request-local AtomicBoolean holder/import, and replaced the unsafe post-compute map read with the holder's result. An absent count is interpreted as zero. Counts at or above five remain unchanged and the call's admission flag remains false. Otherwise, the callback records admission and returns count + 1. Both the state update and admission decision occur inside the atomic per-key operation. The holder belongs to one method invocation, not shared limiter state. AtomicBoolean is part of the JDK; no dependency was introduced. The owner's class-level map declaration and interface were preserved; private visibility remains a recommended owner edit.

VERIFIED compilation: executed the Temurin 21.0.11 javac with --release 21 against RateLimiter.java and LocalRateLimiter.java, with class output in a fresh /private/tmp/sentinel-sent005-review.* directory. Exit 0 with no diagnostics. Sequential and concurrent tests were not executed; runtime correctness remains unverified. No performance measurements or learning assessment were performed.

Next action: explain the two callback paths and why its integer return differs from the outer method's boolean return, then create and run boundary, rejection-state, independent-client, and synchronized concurrent tests with mentoring. SENT-005 remains In Progress pending verification, understanding, review, and explicit closure approval. Files changed during this rewrite: LocalRateLimiter.java, CURRENT_STATE.md, and PROJECT_ROADMAP.md. No production gateway source, architectural decisions, dependencies, staging, commits, or pushes changed.

20. SENT-005 First Sequential Harness Review — 2026-10-31

DOCUMENTED owner run: javac LocalRateLimiterTest.java followed by java LocalRateLimiterTest.java printed True five times and False once. VERIFIED source: the harness uses an instance main method, loops from zero through five inclusive, and prints results for one client without assertions. It does not check the seventh request, another client, or stored counts. The limiter implementation is unchanged.

VERIFIED commands: default java -version reports Temurin 25.0.3. Explicit Temurin 21.0.11 javac --release 21 compiled the interface, limiter, and test into a fresh /private/tmp/sentinel-sent005-test.* directory. Launching that compiled test with Java 21 failed with: Main method is not static in class LocalRateLimiterTest. This is a harness entry-point issue; no limiter behavior was executed by that Java 21 launch. The owner's printed output is a demonstration, not evidence that all required tests passed.

Next at this first-harness review checkpoint: owner fixes main and adds sequential assertions before concurrency tests. Java static entry points and AssertionError must be explained before use; concurrency tools will be introduced only with explanations. No limiter source or test source changed by Codex during that review. Only progress documents updated; no dependency, integration, staging, commit, or push changes. Owner-generated .class files are present alongside exercise source and were left untouched.

21. SENT-005 Sequential and Concurrent Verification — 2026-10-31

DOCUMENTED authorization: the owner explicitly requested, "write tests. i am not sdet". Codex replaced LocalRateLimiterTest.java with a standalone JDK-only harness. ExecutorService (worker pool), CountDownLatch (starting gate), Future (task outcomes/exceptions), and AssertionError (automatic failure) were explained before use. Outcomes are counted on the main thread; no AtomicInteger or new dependency is needed. This is assisted testing, not owner-authored test code or independently assessed learning.

VERIFIED: the sequential test checks each of A's first five admissions and stored counts, sixth/seventh rejections with the count staying at five, and B's independent five admissions and sixth rejection using the same limiter. Concurrent tests use 16 worker threads, a gate releasing the first 16 runnable tasks together, interleaved clients, timed waits, collected Future results that propagate worker errors, and executor cleanup in finally. Each concurrent scenario uses a fresh limiter; the multiple-client scenario shares that limiter across A, B, and C.

Executed from the repository root with Temurin 21.0.11:

```sh
sent005_test_classes=$(mktemp -d /private/tmp/sentinel-sent005-tests.XXXXXX)
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/javac --release 21 -d "$sent005_test_classes" excercise/sent-005-in-memory-rate-limiter/RateLimiter.java excercise/sent-005-in-memory-rate-limiter/LocalRateLimiter.java excercise/sent-005-in-memory-rate-limiter/LocalRateLimiterTest.java
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java -cp "$sent005_test_classes" LocalRateLimiterTest
```

VERIFIED: compilation and execution exited 0. Observed output:

```text
PASS sequential: A allowed=5 rejected=2 count=5; B allowed=5 rejected=1 count=5
PASS concurrent same client: A allowed=5 rejected=95 count=5
PASS concurrent independent clients: A allowed=5 rejected=95 count=5
PASS concurrent independent clients: B allowed=5 rejected=95 count=5
PASS concurrent independent clients: C allowed=5 rejected=95 count=5
ALL TESTS PASSED
```

Atomicity explanation: compute() executes the check and counter update atomically for the client key. With count four, one request stores five and is admitted; the next operation on that client sees five, preserves it, and rejects. The admission holder is local to one invocation and records the result inside compute(), avoiding a separate post-update map read. Correctness assumes callers do not mutate the package-visible map directly; the tests only read it after relevant operations complete.

Limitations: per-limiter-instance/process state, no shared distributed limit, no reset or expiration, no client-entry eviction, loss of state on process restart, and no defined identity validation policy. Null/invalid client behavior, persistence, HTTP integration, and performance were not tested. These are bounded correctness checks, not exhaustive coverage of thread schedules or a benchmark.

Hash comparison VERIFIED that LocalRateLimiter.java, RateLimiter.java, gateway configuration/entry point, and the Git index stayed unchanged during this test task. Only LocalRateLimiterTest.java and the two progress documents were changed by Codex. Class output went to /private/tmp; existing owner-generated class files were untouched. No dependencies, architecture, staging, commits, or pushes changed. SENT-005 remained In Progress at that verification checkpoint; subsequent owner-approved closure is recorded below.

22. SENT-005 Closure and SENT-006 Preparation — 2026-10-31

DOCUMENTED: the owner approved SENT-005 closure specifically as a standalone assisted implementation with successful sequential/concurrent tests and explicitly started SENT-006. Integration behavior is owner-written. SENT-006 remains In Progress.

VERIFIED inspection: AGENTS.md, CURRENT_STATE.md, DECISIONS.md, PROJECT_ROADMAP.md, the existing limiter/interface, gateway POM, application entry point, route configuration, and Git status were inspected. Existing progress edits and the untracked SENT-005 exercise were preserved. The gateway currently has only its application entry point plus configuration for hello-route (Path=/hello, Method=GET, target http://127.0.0.1:8081) and health exposure. No integrated limiter/filter source exists. No source was generated, build executed, or HTTP request performed during preparation.

PROPOSED minimal implementation layout: add RateLimiter.java and LocalRateLimiter.java under com.sentinel.gateway.ratelimit, add DemoRateLimitGatewayFilterFactory.java under com.sentinel.gateway.filter, and add one filter reference to the existing route in application.properties. Keep the standalone exercise as its own artifact. Register LocalRateLimiter and the factory as Spring components; inject RateLimiter through the factory constructor. The default singleton scope shares one limiter bean in this application context, not across gateway processes. No dependency or application-entry-point change is expected.

Verified official API guidance: [Gateway 5.0.4 developer guide](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/developer-guide.html) describes AbstractGatewayFilterFactory, its apply() callback, and the GatewayFilterFactory naming suffix. Proposed class DemoRateLimitGatewayFilterFactory is referenced as DemoRateLimit in route configuration. Admission must execute for each request inside the returned filter, before delegating to the chain; it must not run once during factory construction. On rejection, set HTTP 429 and complete the response without invoking the forwarding chain. [Spring bean scopes](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html) documents default singleton scope; [Boot package guidance](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html) supports subpackages beneath com.sentinel.gateway for component scanning.

Pending runtime evidence: 200 for requests one through five; 429 for six/seven; backend logs/counts showing exactly five forwarded requests and no receipts for rejects; health UP; a fresh allowance after gateway restart; successful Java 21 application build. All remain UNKNOWN until executed against the integration. Fixed demo-client intentionally combines all callers into one temporary quota; no header identity, production authentication, distributed guarantee, time reset, or Redis is implied. The historical editor-only duplicate-class warning was not reproduced as a compiler error; the new gateway classes will use named packages.

Only CURRENT_STATE.md and PROJECT_ROADMAP.md were updated for this closure/preparation checkpoint. Architectural decision documents and application/exercise source were unchanged; no staging, commit, push, dependency addition, or further ticket closure occurred.

VERIFIED final Git inspection: HEAD is 62e4a63 (In memory limiter code written with test.). The SENT-005 source and existing .class files are now tracked; final status shows only the two modified progress documents. This differs from the initial inspection's untracked exercise directory; no commit was made by Codex and no explanation or push is inferred. git diff --check passed.

23. SENT-006 Assisted Coding: Spring-Managed Limiter — 2026-11-01

DOCUMENTED: the owner explicitly requested that Codex write code and explain decisions one class/method at a time, with questions permitted to strengthen understanding. This supersedes the earlier owner-only coding preference for this assisted workflow; it does not authorize skipping explanations or implementing future features.

VERIFIED changes: added gateway/src/main/java/com/sentinel/gateway/ratelimit/RateLimiter.java with the existing boolean processRequest(String clientId) contract, and LocalRateLimiter.java with the existing compute() admission logic. Both use a named package beneath the application package. The implementation adds @Component registration, @Override, and private final map visibility; the standalone exercise is unchanged. @Component and default singleton scope are intended to provide a shared bean for constructor injection; runtime bean creation has not yet been verified. Spring's discovery and shared-instance role were explained before writing these files.

Executed from gateway/: env JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw -o -B -ntp compile. VERIFIED result: exit 0, BUILD SUCCESS, three application sources compiled with release 21. The sandbox initially blocked Maven cache locking; the same offline command succeeded with required execution permission. No dependency download or dependency declaration change occurred. No tests, HTTP requests, application restart, or gateway enforcement verification occurred in this step.

Next teaching step: confirm understanding of shared counter lifetime, then explain/write the filter factory and its constructor injection. Only the two new gateway classes and progress documents changed; route configuration, application entry point, exercise, and architectural decision documents were preserved. No staging, commit, push, or ticket closure occurred. SENT-006 remains In Progress.

Subsequent guided step, 2026-11-01: VERIFIED comprehension response: the owner correctly explained that creating a new limiter per request would make each request appear to be the first. This is a specific guided response, not an independent proficiency assessment. Codex then added DemoRateLimitGatewayFilterFactory.java with @Component, a private final RateLimiter field, and a constructor storing the supplied dependency. It is deliberately only a plain component scaffold at this step; no Gateway factory inheritance, filter callback, route attachment, or HTTP enforcement exists yet. The same offline Java 21 Maven compile exited 0 with BUILD SUCCESS and four application sources compiled. No tests or HTTP checks ran. Next: explain the constructor/interface choice and introduce the Gateway API before implementation. Only the new scaffold and progress records changed in this step.

Subsequent filter-method step, 2026-11-01: the owner explained that depending on the interface permits replacing the implementation without rewriting the filter; clarified that constructor injection supplies one implementation, not all implementations. This was guided discussion, not a verified independent architecture exercise. Explained exchange, filter chain delegation, AbstractGatewayFilterFactory, its empty Config type, and Mono<Void> completion before adding the Gateway API and apply() method. The callback checks the injected limiter once per request for fixed demo-client, returns chain.filter(exchange) when allowed, and otherwise sets HTTP 429 and returns response.setComplete() without invoking the chain. The empty public static Config class exists only for the factory API; no configurable failure or time-window policy was selected.

VERIFIED: the offline Maven compile with explicit Java 21 again exited 0 with BUILD SUCCESS and four sources compiled. The route configuration was untouched, so this filter is not yet attached to HTTP traffic. No tests, app startup, forwarding/429 checks, backend receipt assertions, health checks, or restart-reset verification occurred. Only the filter class and progress records changed in this step; dependencies, standalone exercise, route, and architectural decisions were preserved. SENT-006 remains In Progress.

24. SENT-006 Route Attachment and Runtime Verification — 2026-11-01

VERIFIED: added spring.cloud.gateway.server.webflux.routes[0].filters[0]=DemoRateLimit to the existing hello-route. The existing Path=/hello, Method=GET, backend URI, and health settings were preserved. DemoRateLimit resolves the custom DemoRateLimitGatewayFilterFactory. Its constructor receives the Spring-managed limiter; admission executes inside the per-request filter callback using fixed demo-client. All callers intentionally share this temporary identity. No production identity or distributed-rate policy is implied.

Guided comprehension: the owner explained that a rejected request should stop. Clarified that chain.filter(exchange) means continue processing, not perform the admission check; rejection completes the response without delegating. This is guided discussion, not independent proficiency evidence.

Stopped the old SENT-004 gateway process before rebuilding. Executed from gateway/:

```sh
env JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw -o -B -ntp package
/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home/bin/java -jar target/sentinel-gateway-0.0.1-SNAPSHOT.jar
```

VERIFIED package result: exit 0, BUILD SUCCESS, executable JAR produced. Maven reported No tests to run; no persistent gateway test suite was created. HTTP assertions below were executed separately using Python's standard-library urllib, sequential requests with ten-second timeouts, and assertions on status/body. No dependency was added or downloaded. The running backend was the existing temporary Python fixture on 127.0.0.1:8081 (PID 31355), with its pending access logs captured as a baseline before issuing requests.

VERIFIED first gateway run: PID 40288 used Java 21.0.11 and logged successful Netty startup on port 8080. Seven sequential GET requests to http://127.0.0.1:8080/hello returned:

| Request | Actual HTTP status | Actual body |
| --- | --- | --- |
| 1 | 200 | {"message":"Hello from backend"} |
| 2 | 200 | {"message":"Hello from backend"} |
| 3 | 200 | {"message":"Hello from backend"} |
| 4 | 200 | {"message":"Hello from backend"} |
| 5 | 200 | {"message":"Hello from backend"} |
| 6 | 429 | Empty |
| 7 | 429 | Empty |

After those requests, GET http://127.0.0.1:8080/actuator/health returned HTTP 200 and {"status":"UP"}. The assertion command exited 0. The newly captured backend log contained exactly five GET /hello HTTP/1.1 entries, all status 200. Across this isolated seven-request sequence, the backend received only the admitted requests, with no receipt for the two rejections. This is the observed local test, not a guarantee under all future filters/configurations.

VERIFIED restart/reset: stopped PID 40288, confirmed port 8080 had no listener, and started the same JAR with the same Java 21 command as PID 40317. The backend remained running. Repeated all seven requests and the health check. Observed the identical 200,200,200,200,200,429,429 sequence and bodies; health again returned HTTP 200 and {"status":"UP"}. Assertions exited 0. Exactly five additional backend receipts were logged. This verifies that restarting the gateway reset the exhausted in-memory allowance and that the cap/rejection path still operated afterward.

VERIFIED final process inspection: gateway PID 40317 listens on 8080; backend PID 31355 listens on 8081. Both were left running. The final gateway's demo-client count is exhausted by the seven-request verification; subsequent /hello requests should be rejected until its limiter state is recreated. No expiration or scheduled reset exists.

Observed nonblocking runtime diagnostics: HV000271 validation deprecations for routes/predicates/filters, and a Netty error loading the macOS native DNS provider followed by fallback to system defaults. Literal-IP backend forwarding succeeded; hostname DNS behavior was not verified. No dependency was added to address these diagnostics. The old process reported graceful shutdown complete when stopped before rebuilding; this does not establish zero downtime or in-flight recovery guarantees.

Limitations: fixed identity combines all callers; counters are local to one application instance, reset on restart, and have no expiration/eviction. Standalone concurrency coverage belongs to SENT-005; concurrent HTTP admission, backend-failure allowance behavior, production identity, distributed state, load/performance, and automated regression integration were not newly tested. No Redis, authentication, time-window logic, or additional dependencies were added.

SENT-006 remained In Progress at this runtime-verification checkpoint, pending owner review and explicit closure approval. This step changed only route configuration and progress records; integration classes were added in the preceding approved teaching steps. The application entry point, POM, standalone exercises, AGENTS.md, and architectural decision documents were preserved. No staging, commit, push, or additional ticket closure occurred.

25. SENT-006 Final Review and Approved Closure — 2026-11-01

DOCUMENTED authorization: after guided review of the request flow, the owner explicitly requested finalization and approved closure conditional on sufficient existing verification evidence. The condition is satisfied. SENT-006 is Completed and approved. This closes only SENT-006; no phase completion, SENT-003/SENT-004 closure, or new ticket transition is inferred.

VERIFIED review: read AGENTS.md, CURRENT_STATE.md, DECISIONS.md, PROJECT_ROADMAP.md, the integrated interface/limiter/filter, route configuration, and POM. The source retains the reviewed compute() implementation, constructor-injected shared limiter, fixed demo-client, allow-chain branch, and 429 response completion without forwarding. Compared the packaged application.properties with the source configuration: identical. Compared all packaged application class entries with the existing compiled class files: identical. Previously captured backend log evidence is still available and contains exactly five receipts for each seven-request run. The source and configuration match the implementation observed during the earlier successful execution; no new source discrepancy or acceptance blocker was found.

| Acceptance criterion | Reviewed actual evidence | Classification |
| --- | --- | --- |
| Requests 1–5 return HTTP 200 | Five 200 responses with {"message":"Hello from backend"} in each run | VERIFIED: section 24 executed assertions |
| Requests 6–7 return HTTP 429 | Two 429 responses with empty bodies in each run | VERIFIED: section 24 executed assertions |
| Rejected requests do not reach backend | Exactly five new backend receipts for each seven-request sequence | VERIFIED: captured backend access logs, sections 24–25 |
| Health remains UP | HTTP 200 and {"status":"UP"} after exhaustion in both runs | VERIFIED: section 24 executed assertions |
| Gateway restart resets counters | Exhausted first process replaced; same JAR admitted five requests again before rejecting | VERIFIED: section 24 restart and HTTP sequence |
| Maven build succeeds | Java 21 offline package exited 0 with BUILD SUCCESS | VERIFIED: section 24 build execution; Maven reported No tests to run |

No runtime tests or builds were repeated during closure. Earlier tool execution evidence, current source review, and artifact/configuration inspection were sufficient. The runtime checks are recorded local observations, not an automated regression suite or exhaustive concurrency/production proof.

Guided learning review: the owner explained the interface/implementation relationship, boolean admission result, shared-counter lifetime, forwarding path, and rejection path. Clarified the separate roles of compute() and AtomicBoolean, one injected implementation, factory apply() versus per-request callback, and the currently empty Config. This supported the guided ticket review; independent mastery was not assessed or claimed.

Known limitations and nonblocking diagnostics remain those in section 24: fixed shared identity, process-local/no-expiration state, validation deprecations, and macOS native-DNS-provider fallback. Existing architecture/decision-log metadata discrepancies remain documented and were not silently edited. Live process/remote availability and any new test results are UNKNOWN at closure because they were not queried. No acceptance verification is missing for this ticket's approved scope.

Only CURRENT_STATE.md and PROJECT_ROADMAP.md changed during closure. Existing application/exercise changes were preserved; no working application code, dependency, architectural decision, staging, commit, or push changed. PROPOSED next milestone is the three-step regression workflow in section 8; it has not been started.
