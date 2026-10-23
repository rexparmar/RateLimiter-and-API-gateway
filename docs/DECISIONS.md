Sentinel — Engineering Decision Log

This document records important project decisions and their reasoning.

Decisions must not be silently changed by Codex.

Each decision should include its motivation, alternatives, trade-offs, and approval status.

⸻

DEC-001 — Primary Programming Language

Status: Approved
Date: 2026-10-22

Decision

Use Java 21 as the primary programming language.

Context

The project is intended to strengthen existing Java knowledge while developing backend engineering skills.

Alternatives Considered

* Go
* JavaScript / Node.js

Reasoning

Java provides opportunities to practice:

* Object-oriented programming
* Concurrency
* Thread safety
* JVM fundamentals
* Backend architecture

Using an already familiar language reduces the additional learning burden.

Trade-offs

* More framework and configuration complexity than some alternatives.
* Java does not inherently produce better distributed-system designs.
* Requires understanding JVM and concurrency behavior.

⸻

DEC-002 — Backend Framework

Status: Approved
Date: 2026-10-22

Decision

Use Spring Boot with Spring Cloud Gateway.

Context

The system must route HTTP requests and enforce rate limits before forwarding requests to backend services.

Reasoning

Spring Boot supports learning dependency injection, configuration, testing, and Java backend development.

Spring Cloud Gateway provides established request-routing functionality, allowing us to focus on rate-limiting and distributed coordination.

Trade-offs

* Introduces Spring framework complexity.
* Reactive and blocking execution models must be handled carefully.
* Spring Boot and Spring Cloud versions must be compatible.

Open Questions

* Which compatible Spring Boot and Spring Cloud versions should we use?
* Which Spring Cloud Gateway variant best fits our requirements?

These questions must be resolved before application initialization.

⸻

DEC-003 — Distributed State

Status: Approved in principle
Date: 2026-10-22

Decision

Use Redis to coordinate rate-limiting state across multiple gateway instances.

Context

Independent in-memory rate limiters cannot enforce one shared global limit across separate gateway processes.

Reasoning

Redis provides a shared state store and supports atomic server-side operations.

Trade-offs

* Adds network latency.
* Introduces an external dependency.
* Requires explicit failure-handling policies.

Open Questions

* Redis key structure
* State expiration strategy
* Atomic token update implementation
* Behavior during Redis failures

These will be resolved during the relevant project phases.

⸻

DEC-004 — Learning-First Development

Status: Approved
Date: 2026-10-22

Decision

The project owner will implement core functionality independently with Codex acting as a mentor and reviewer.

Reasoning

The primary objective is engineering skill development rather than rapid project completion.

Rules

* One micro-task at a time.
* No autonomous feature implementation.
* No unexplained architectural changes.
* No advancing without understanding.
* Testing and review are required.
* Project owner approves task completion.

Trade-offs

Development will take longer than an AI-generated implementation.

This is an intentional trade-off.

⸻

DEC-005 — Repository-Based Context Management

Status: Approved
Date: 2026-10-22

Decision

Maintain persistent project context using:

* AGENTS.md
* docs/CURRENT_STATE.md
* docs/DECISIONS.md

Reasoning

Long AI conversations may lose context or produce inaccurate recollections.

Version-controlled project documentation allows new sessions to reconstruct project state.

Rules

* Codex must inspect relevant repository state.
* Documentation must not be treated as automatically verified.
* Contradictions must be reported.
* State updates require project-owner approval.
* Important decisions must be recorded.

Trade-offs

Documentation requires maintenance and review.

⸻

DEC-006 — Performance Claims

Status: Approved
Date: 2026-10-22

Decision

All performance claims must be supported by reproducible measurements.

Requirements

Benchmarks must document:

* Test environment
* Hardware limitations
* Traffic generation method
* Offered load
* Achieved throughput
* Latency percentiles
* Error rates
* Rejection rates
* Relevant system configuration

Reasoning

Configured load does not prove achieved throughput.

Performance claims without evidence must be marked unverified.

⸻

Future Decisions

Additional decisions will be recorded when we reach the relevant engineering phase.

Do not make premature architectural decisions solely to populate this document.