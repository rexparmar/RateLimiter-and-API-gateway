# Sentinel — Persistent Project Roadmap

Last Updated: 2026-10-23

## Objective and planning horizon

Build Sentinel, a distributed rate limiter and API gateway, while developing independent software engineering skills. Learning is the primary objective; implementation speed is secondary. The project owner writes and understands application code, with Codex acting as a mentor and reviewer.

The DOCUMENTED, project-owner-approved plan targets 12 weeks at approximately 8–10 hours per week, with an optional two-week buffer. Weeks describe the intended sequence, not automatic deadlines or permission to advance. The buffer allows additional practice, verification, or unfinished work within the agreed scope.

## Current position and evidence

| Item | Current value | Evidence |
| --- | --- | --- |
| Active phase | Phase 0 — Engineering Foundations | DOCUMENTED: current project state and owner instruction |
| Active ticket | SENT-002 — High-Level System Architecture | DOCUMENTED: explicit owner instruction |
| Ticket status | In Progress; architecture drafting has not begun | DOCUMENTED: owner-authorized transition; VERIFIED: synchronization changed documentation only |
| Latest approved milestones | SENT-000.7, SENT-000.8, and SENT-001 — Completed and approved | DOCUMENTED: explicit project-owner closure approval |
| Implementation | No application code or build configuration present at the SENT-000.8 inspection | VERIFIED: repository file inventory |
| Foundation work | Context-management instructions, current-state documentation, and decision log exist and contain content | VERIFIED: inspected repository files |

The topics and implementation deliverables below describe planned work. Their inclusion does not mean a feature is implemented, a concept is mastered, or a test has passed. The requirements-document milestone is Completed: [REQUIREMENTS.md](REQUIREMENTS.md) records the owner-approved requirements direction with the normal-operation and Redis-unavailable clarification. No failure policy has been selected. The architecture-diagram and system-boundary deliverables remain Not Started; context-management artifacts alone do not satisfy them.

Evidence labels follow [AGENTS.md](../AGENTS.md): VERIFIED means confirmed by inspection or executed commands; DOCUMENTED means recorded in project documentation; PROPOSED means suggested but not approved; UNKNOWN means evidence is insufficient. A missing execution result is not a passing test.

## Phase overview

| Phase | Target weeks | Status |
| --- | --- | --- |
| 0 — Engineering Foundations | 1 | In Progress |
| 1 — Java and Gateway Foundations | 2–3 | Not Started |
| 2 — Rate Limiting and Concurrency | 4–5 | Not Started |
| 3 — Distributed Rate Limiting | 6–7 | Not Started |
| 4 — Reliability Engineering | 8–9 | Not Started |
| 5 — Observability and Performance | 10–11 | Not Started |
| 6 — Production Readiness | 12 | Not Started |

## Phase 0 — Engineering Foundations (Week 1)

Status: In Progress

Topics:

- Problem definition
- HTTP fundamentals
- Functional requirements
- Nonfunctional requirements
- API gateway vs reverse proxy vs load balancer
- High-level architecture
- Design reviews
- Git and engineering workflows

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Requirements document | Completed | [REQUIREMENTS.md](REQUIREMENTS.md), all 16 requirements and operating-mode clarification; explicit owner approval of SENT-001 closure, 2026-10-23 |
| Initial architecture diagram | Not Started | Not yet recorded |
| Approved system boundaries | Not Started | Not yet recorded |

## Phase 1 — Java and Gateway Foundations (Weeks 2–3)

Status: Not Started

Topics:

- Java 21 fundamentals relevant to backend systems
- JVM basics
- Maven
- OOP and interfaces
- Dependency injection
- Spring Boot fundamentals
- HTTP request lifecycle
- Reverse proxy concepts
- Spring Cloud Gateway
- Routes and filters

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Minimal Spring Boot application | Not Started | Not yet recorded |
| Working request forwarding | Not Started | Not yet recorded |
| Initial automated tests | Not Started | Not yet recorded |

## Phase 2 — Rate Limiting and Concurrency (Weeks 4–5)

Status: Not Started

Topics:

- Fixed window
- Sliding window
- Leaky bucket
- Token bucket
- Algorithm trade-offs
- Java concurrency
- Race conditions
- Thread safety
- HTTP 429 behavior

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Correct local rate limiter | Not Started | Not yet recorded |
| Unit tests for edge cases | Not Started | Not yet recorded |
| Concurrency tests | Not Started | Not yet recorded |

## Phase 3 — Distributed Rate Limiting (Weeks 6–7)

Status: Not Started

Topics:

- Horizontal scaling
- Shared state
- Redis fundamentals
- Atomicity
- Lua scripts
- Distributed correctness
- Expiration and time semantics

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Redis-backed distributed rate limiter | Not Started | Not yet recorded |
| Multiple gateway instances | Not Started | Not yet recorded |
| Integration tests | Not Started | Not yet recorded |

## Phase 4 — Reliability Engineering (Weeks 8–9)

Status: Not Started

Topics:

- Timeouts
- Partial failures
- Retries
- Fail-open vs fail-closed
- Circuit breakers
- Failure injection
- Recovery behavior

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Explicit failure policies | Not Started | Not yet recorded |
| Resilience tests | Not Started | Not yet recorded |
| Documented trade-offs | Not Started | Not yet recorded |

## Phase 5 — Observability and Performance (Weeks 10–11)

Status: Not Started

Topics:

- Metrics
- Micrometer
- Prometheus
- Grafana
- Throughput
- Latency percentiles
- Load generation with Locust
- Bottleneck analysis
- Performance tuning

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Metrics and dashboards | Not Started | Not yet recorded |
| Reproducible load tests | Not Started | Not yet recorded |
| Evidence-backed performance report | Not Started | Not yet recorded |

No throughput figure is claimed or promised. Under DEC-006, performance reports must document the environment, hardware limitations, traffic generation method, offered load, achieved throughput, latency percentiles, error rates, rejection rates, and relevant system configuration. Configured load is not evidence of achieved throughput.

## Phase 6 — Production Readiness (Week 12)

Status: Not Started

Topics:

- Regression testing
- CI with GitHub Actions
- Security review
- Configuration management
- Documentation
- Operational runbooks
- Release preparation
- System design interview practice
- Retrospective

| Planned deliverable | Status | Completion evidence |
| --- | --- | --- |
| Final documented project | Not Started | Not yet recorded |
| Reproducible test and build workflow | Not Started | Not yet recorded |
| Portfolio-ready demonstration | Not Started | Not yet recorded |
| Architecture explanation | Not Started | Not yet recorded |

## Alignment with approved decisions

[DECISIONS.md](DECISIONS.md) remains the source of architectural decisions. This roadmap records the agreed learning sequence and deliverables; it selects no additional algorithms, dependencies, versions, or failure policies.

- DEC-001: Java 21 remains the primary language.
- DEC-002: Spring Boot and Spring Cloud Gateway remain the framework direction. Compatible versions and the Gateway variant must be resolved before application initialization.
- DEC-003: Redis is approved in principle for shared state. Key structure, expiration, atomic token updates, and Redis-failure behavior remain open for their relevant phases. Studying algorithms or Lua scripts does not resolve these decisions.
- DEC-004: The owner implements core functionality independently. Work proceeds one micro-task at a time with explanation, review, testing, demonstrated understanding, and owner-approved closure.
- DEC-005: Repository documentation preserves context. Automatic progress updates operate under the standing owner authorization in AGENTS.md.
- DEC-006: Performance claims require reproducible measurements.

Other DOCUMENTED stack choices in [CURRENT_STATE.md](CURRENT_STATE.md), including Maven, Docker Compose, JUnit, Mockito, Testcontainers, Micrometer, Prometheus, Grafana, Locust, and GitHub Actions, remain unchanged. Recording them here does not authorize installation or implementation.

## Progress and maintenance rules

1. Use only Not Started, In Progress, Blocked, or Completed for progress status. When work is Blocked, record the blocker and the evidence needed to resolve it in CURRENT_STATE.md.
2. Do not mark phases or deliverables Completed without verified evidence. Record an artifact or verification reference and project-owner approval before marking completion. Never infer completion from elapsed time or a status in another document.
3. Keep the active phase and ticket visible here and synchronized with CURRENT_STATE.md. Ticket closure requires explicit project-owner approval.
4. Distinguish planned features from implemented features; never invent completed work, test results, benchmarks, or learning achievements.
5. Keep detailed daily progress, partial work, blockers, and verification results in CURRENT_STATE.md.
6. Keep architectural decisions in DECISIONS.md. Report contradictions rather than silently changing decisions.
7. Update roadmap progress automatically after approved milestones, consistent with AGENTS.md checkpointing rules. Preserve unrelated user changes; do not checkpoint every trivial interaction.
8. Do not change the agreed scope or learning objectives without project-owner approval.
9. Do not advance phases simply because the calendar says to. Advance only after relevant evidence, review, understanding, and owner approval.
10. Prioritize understanding, correctness, and testing. Dependencies, application code, commits, pushes, and destructive operations still require explicit authorization.

## Next micro-task

Begin SENT-002 — High-Level System Architecture with the project owner’s proposed main components, trust boundaries, and request flow based on the approved requirements direction. Phase 0 remains In Progress; architectural decisions and subsequent ticket or phase transitions require project-owner approval.
