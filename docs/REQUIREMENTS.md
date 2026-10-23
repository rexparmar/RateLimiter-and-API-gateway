# Sentinel — Requirements

Date: 2026-10-23
Ticket: SENT-001 — Define Sentinel Requirements
Status: DOCUMENTED — requirements direction approved by the project owner with the operating-mode clarification below; SENT-001 completed and approved

## Objective and scope

Build Sentinel, a distributed rate limiter and API gateway, while developing independent software engineering skills. Learning, correctness, understanding, and testing take priority over implementation speed. The project owner implements core functionality with Codex acting as a mentor and reviewer.

The intended system identifies clients through a trusted mechanism, makes rate-limit admission decisions, rejects exhausted requests, and forwards admitted HTTP requests to a configured backend. During normal operation, when the shared state dependency is available, the target distributed system coordinates a shared logical limit across multiple gateway instances. Earlier learning phases may use a local limiter; that milestone alone does not satisfy the distributed requirements for normal operation.

This document specifies desired behavior. It does not select an algorithm, identity mechanism, time model, configuration mechanism, or Redis-failure policy. It does not authorize application implementation.

## Approval status and existing decisions

The project owner has approved the FR and NFR requirements direction with the operating-mode clarification below and authorized closure of SENT-001. Assumptions still require validation, initial exclusions remain proposed boundaries, and verification approaches remain proposals for future checks. This requirements milestone does not select architectural mechanisms or claim implemented behavior or passing tests.

[DECISIONS.md](DECISIONS.md) remains the source of approved architectural decisions:

- DOCUMENTED: DEC-001 approves Java 21.
- DOCUMENTED: DEC-002 approves Spring Boot with Spring Cloud Gateway; compatible versions and the Gateway variant remain open.
- DOCUMENTED: DEC-003 approves Redis in principle for shared state; key structure, expiration, atomic updates, and failure behavior remain open.
- DOCUMENTED: DEC-004 establishes learning-first development, independent owner implementation, and owner-approved task closure.
- DOCUMENTED: DEC-005 establishes repository-based context management, with standing progress-update approval described in [AGENTS.md](../AGENTS.md).
- DOCUMENTED: DEC-006 requires reproducible evidence for performance claims.

VERIFIED means confirmed through inspected artifacts or executed commands. DOCUMENTED means recorded in project documentation. PROPOSED means suggested but not approved. UNKNOWN means insufficient evidence. Operational behavior, achieved latency, and throughput are UNKNOWN until implementation and relevant verification provide evidence.

## Operating modes and enforcement guarantees

Strict shared rate-limit enforcement, including the global allowance and atomicity guarantees in FR-006 and FR-007, applies during normal operation when the shared state dependency, Redis, is available. NFR-001 and NFR-002 are evaluated against those normal-operation guarantees and the behavior defined for the configured failure mode.

When Redis is unavailable, Sentinel follows its configured failure policy under NFR-003. Fail-open and local fallback may weaken global enforcement guarantees; local enforcement alone does not establish a shared global limit. Outage behavior must therefore be evaluated against the chosen policy rather than claimed to provide the normal-operation global guarantee.

No failure policy has been selected or implemented. Supported policies, defaults, configuration, recovery behavior, and the guarantees of each mode remain architectural questions. Naming fail-open and local fallback here does not approve either as a supported policy or a default.

## Functional requirements

The requirements direction is owner-approved with the operating-mode qualification above. Verification approaches describe future checks, not executed tests.

| ID | Requirement | Proposed verification approach |
| --- | --- | --- |
| FR-001 | Enforce independent rate limits per identified client. | Exhaust one client's allowance and verify another client's independent allowance is unaffected. |
| FR-002 | Derive client identity from a trusted source. Do not blindly trust client-controlled identity headers. | Once the trust model is approved, verify that forged or changed untrusted headers cannot impersonate another client or reset its effective identity. |
| FR-003 | Reject requests exceeding their configured limit with HTTP 429 without forwarding them to the backend. | Exhaust the applicable allowance, then verify a 429 response and no corresponding backend invocation. |
| FR-004 | Consume allowance when a request is admitted, regardless of its eventual backend response status. | Admit requests whose backend responses include success, client-error, and server-error statuses; verify allowance was consumed for each admission. |
| FR-005 | Do not consume additional allowance for requests rejected due to rate-limit exhaustion. | Verify repeated exhausted requests do not consume more allowance. Rejection metrics may still be recorded. |
| FR-006 | During normal operation when Redis is available, enforce a shared logical limit across multiple gateway instances. | With Redis available, distribute requests for one client across multiple gateways and verify their aggregate admissions respect the same configured allowance under the agreed time model. Verify Redis-unavailable behavior against the configured failure policy under NFR-003. |
| FR-007 | During normal operation when Redis is available, ensure concurrent rate-limit decisions are atomic so simultaneous requests cannot exceed the configured shared allowance. | With Redis available, send simultaneous requests with limited allowance remaining; verify aggregate admissions do not exceed it, including requests handled by different gateways. Verify any weakened outage guarantees against the configured failure policy under NFR-003. |
| FR-008 | Forward admitted HTTP requests to the configured backend service. | Verify admitted requests reach the configured backend and their responses return through the gateway. Exact forwarding semantics require clarification below. |

Admission means the rate-limit decision permits a request to proceed toward the backend. Backend response status does not reverse consumption under FR-004. Rejection for exhaustion means the request was not admitted and must satisfy both FR-003 and FR-005. These rules do not yet define accounting for gateway transport failures or retries; those cases require an explicit decision.

## Nonfunctional requirements

The requirements direction is owner-approved with the operating-mode qualification above. Qualitative goals will need reviewable measurement or verification criteria as the relevant decisions are resolved.

| ID | Requirement | Proposed verification or clarification |
| --- | --- | --- |
| NFR-001 | Correctness under concurrent requests within the guarantees of the operating mode. | Use reproducible concurrency tests to check allowance accounting and FR-007 during normal operation; check outage behavior against the configured failure policy. |
| NFR-002 | Consistent shared rate-limit enforcement across gateway instances during normal operation when Redis is available. | With Redis available, use integration tests that distribute one client's requests across gateways and check shared accounting. Outage tests assert the configured policy's documented guarantees. |
| NFR-003 | Explicit and configurable behavior when Redis is unavailable. Fail-open and local fallback may weaken global enforcement guarantees. The exact policies remain undecided. | Approve the supported policies, configuration mechanism, guarantees, and recovery semantics before failure-injection tests assert the selected behavior. No policy is selected here. |
| NFR-004 | Low gateway latency overhead. No numerical target is established. | Define the measurement boundary and environment; compare a documented baseline with requests passing through the gateway. Select any numerical acceptance target through owner review. |
| NFR-005 | Observable request counts, rejection rates, errors, and latency. | Verify recorded metrics against controlled traffic; define counting boundaries and error categories before implementation. |
| NFR-006 | Reproducible tests and performance measurements. | Document setup, configuration, commands, workload, and actual results. Performance reports must satisfy DEC-006. |
| NFR-007 | Secure handling of client identity and gateway configuration. | Review trust boundaries and configuration access; test identity-spoofing and configuration-validation cases once their expected behavior is approved. |
| NFR-008 | Clear operational documentation and failure behavior. | Review setup instructions, failure-policy documentation, and recovery procedures against observed behavior when an implementation exists. |

No throughput, latency result, unconditional availability guarantee, test result, or learning achievement is claimed by this document.

## Assumptions requiring validation

These are PROPOSED assumptions, not approved architectural decisions:

- The gateway receives HTTP traffic, while a separate backend service handles admitted requests.
- A stable, trustworthy client identity can be made available at the admission boundary. Its source, validation, and trust boundary are unresolved; header presence alone establishes no trust.
- A client can send traffic through more than one gateway instance, so independent per-instance counters cannot alone satisfy FR-006.
- A configured allowance and time model will be defined sufficiently precisely to evaluate admission behavior. No particular window, refill strategy, or algorithm is assumed.
- Controlled backend services, multiple gateway instances, and Redis can be used in reproducible verification environments. Their setup has not been implemented or verified.
- Redis is the intended shared-state direction under DEC-003's approval in principle. This proposal assumes no uptime guarantee and selects no outage policy.

## Proposed exclusions from the initial implementation

The following boundaries require owner review; they do not remove the approved roadmap's later reliability, observability, testing, or production-readiness work:

- Billing, subscriptions, and monetized API plans.
- A client-management portal or administrative web UI.
- Building a new identity-provider or user-account system; trustworthy client identification remains required by FR-002.
- Multi-region coordination and geographic traffic management; shared enforcement across multiple gateway instances remains required by FR-006.
- Proxying protocols beyond the agreed HTTP scope. Support for streaming or upgraded connections within that scope remains an open clarification.

## Open questions requiring decisions

These questions are UNKNOWN or unresolved. Proposed answers must be reviewed and approved before being recorded as decisions in DECISIONS.md.

| Question | Requirements affected |
| --- | --- |
| Which rate-limiting algorithm will we implement, and what burst behavior is allowed? | FR-001, FR-003–FR-007, NFR-001–NFR-002 |
| How will clients be authenticated or identified? Which component establishes trust, and what happens when identity is missing or invalid? | FR-001–FR-002, NFR-007 |
| What exactly defines the rate-limit time window or refill interval? Which clock and boundary/expiration semantics apply? | FR-003–FR-007, NFR-001–NFR-002 |
| How will limits be configured, validated, and applied? What are the defaults, client scope, and behavior when configuration changes? | FR-001, FR-003–FR-007, NFR-007 |
| What happens when Redis is unavailable, slow, or recovering? Which configurable policies will exist? | FR-003, FR-006–FR-008, NFR-003, NFR-008 |
| Which failure policies will be supported, which guarantees will each provide, and when may a policy reject otherwise admissible requests? | FR-006–FR-008, NFR-002–NFR-003 |
| Which Spring Boot and Spring Cloud release versions are compatible with the chosen Spring Cloud Gateway version and Java 21? | FR-008 and application initialization |
| Which Spring Cloud Gateway variant will we use? | FR-008 and application initialization |
| What Redis key structure, expiration, and atomic state-update mechanism will implement the approved algorithm? | FR-001, FR-005–FR-007 |
| What accounting applies when forwarding fails, times out, or is retried after admission? | FR-004–FR-005, NFR-003, NFR-008 |
| Which HTTP methods, paths, query parameters, headers, bodies, response behavior, and streaming or upgrade cases must forwarding support? | FR-008, NFR-007–NFR-008 |
| What precisely defines gateway latency overhead, metric counting boundaries, and reproducible performance acceptance criteria? | NFR-004–NFR-006 |

The normal-operation scope of FR-006 and FR-007 is approved as clarified above. During a Redis outage, configured fail-open or local fallback behavior may weaken global enforcement. The policy selection and its exact guarantees remain undecided and require explicit architectural approval; this document selects no policy.

Version compatibility has not been researched or verified as part of this documentation task. No version is selected here.

## Acceptance criteria for this requirements document

These criteria concern document review, not application correctness:

1. The objective and proposed scope reflect the learning-first project and agreed roadmap.
2. FR-001 through FR-008 and NFR-001 through NFR-008 are present, uniquely identified, and traceable to the owner's discovery input.
3. Admission, rejection, and allowance consumption are described consistently; proposed verification approaches make the intended behavior reviewable.
4. Assumptions and initial exclusions are explicit proposals awaiting validation, with no silent reduction of agreed scope.
5. Architectural choices and unresolved measurement criteria are listed as open questions rather than invented decisions or numerical targets.
6. Existing decision approvals, including Redis's approval in principle, are preserved. The approved requirements direction and remaining proposals are clearly distinguished from architectural decisions and implemented behavior.
7. Documentation verification reports the actual results; no application tests, performance results, or learning achievements are inferred from writing the document.
8. The project owner reviews the proposal, resolves or explicitly accepts deferral of open questions, confirms understanding of the important distinctions, and explicitly approves requirements acceptance and closure of SENT-001.

DOCUMENTED: The project owner approved the requirements direction with the normal-operation and Redis-unavailable clarification and authorized SENT-001 completion. Architectural selections and remaining proposals are deferred, not resolved by this approval. The next ticket is SENT-002 — High-Level System Architecture; this record does not claim implementation or proficiency verification.
