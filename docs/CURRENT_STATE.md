Sentinel — Current Project State

Last Updated: 2026-10-29
Repository: RateLimiter
Project: Sentinel — Distributed Rate Limiter & API Gateway

⸻

1. Current Position

* Phase: 1 — Java and Gateway Foundations
* Phase Status: In Progress
* Roadmap Weeks: 2–3 (planned learning sequence, not elapsed-time verification)
* Milestone: Owner-written GET /hello route reviewed and HTTP-verified; unknown-path, backend-outage, and restoration checks recorded
* Active Ticket: SENT-004 — Basic HTTP Routing
* Status: In Progress

2. Current Objective

SENT-004 is In Progress under the owner's explicit instruction (DOCUMENTED, 2026-10-29). The owner's GET /hello route was reviewed, packaged with Java 21, and exercised through Sentinel on port 8080 (VERIFIED). Forwarding returned HTTP 200 and the backend JSON; /unknown returned 404; stopping the backend caused HTTP 500 with a connection-refused log; restarting it restored HTTP 200. See section 16. Owner explanation, review of results, and ticket closure approval remain pending. SENT-003's scaffold milestone is verified in section 14; formal SENT-003 closure approval is not recorded.

The project owner has approved the high-level architectural direction and instructed closure of SENT-002 (DOCUMENTED). The readiness review verified the required Phase 0 artifacts and consistency with REQUIREMENTS.md. Phase 0 is Completed against the roadmap's documented deliverables and review/approval rules; no separate Phase 0 acceptance checklist is present. No independent proficiency assessment or runtime validation is inferred from this completion.

The owner authorized and wrote a standalone exercise in excercise/sent-003-java-basics/src with file-by-file mentoring (DOCUMENTED: owner instruction; VERIFIED: reviewed source). Java 21 compilation and execution succeeded for both always-allow and always-reject implementations. This is guided practical evidence, not an independent proficiency assessment or a completed production rate limiter. See section 13 for commands, results, and limitations.

Strict shared enforcement applies during normal operation when Redis is available; when Redis is unavailable, the configured failure policy applies. Fail-open and local fallback may weaken global guarantees. The algorithm, identity mechanism, load balancer, and Redis-failure policy remain explicitly undecided. For this approved scaffold, Spring Boot 4.1.2, Spring Cloud 2025.1.4, and Gateway Server WebFlux are selected and verified. SENT-004 authorizes owner-written basic routing and a local test backend; rate limiting, Redis, authentication, and custom filters remain outside this task.

3. Completed Work

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
* SENT-004 authorizes a local test backend, mentoring/review of owner-written basic route configuration, and HTTP verification of forwarding, unknown paths, and backend unavailability. No additional dependencies, rate limiting, Redis, authentication, or custom filters are authorized for this task.
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

Next action: the owner explains how the Path and Method predicates combine, how the URI determines the backend, and why an unmatched path differs from an unavailable backend. Review the verified 200/404/500 results before requesting SENT-004 closure approval. No further implementation or ticket transition is authorized by this checkpoint.

Official Spring documentation and release records establish the approved compatibility line; exact parent/BOM artifact downloads returned HTTP 200, and the resulting build and startup passed. Section 14 records sources and actual results. Constructor null-validation and independent compiler/runtime explanation remain unverified; they were not silently marked complete when the owner requested moving to the actual project.

The roadmap records the owner-approved learning scope. Further behavior and open architectural decisions remain subject to the learning protocol and applicable owner authorization.

Phase 0 and SENT-002 remain Completed. Phase 1 and the active SENT-004 are In Progress. SENT-003 closure is not recorded; moving to the explicitly owner-authorized SENT-004 does not close it. Routing and automated-test deliverables are not complete.

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

SENT-004 remains In Progress pending the owner's explanation of predicates, URI, and the observed response differences, review of results, and explicit closure approval. SENT-003 formal closure remains unrecorded. Codex modified only CURRENT_STATE.md and PROJECT_ROADMAP.md during this verification; generated build output is under ignored gateway/target/. Existing changes, the owner route, and the standalone exercise were preserved. No files were staged, committed, or pushed.
