# Sentinel Redis Shared-State Design

Status: **APPROVED FOR SENT-011 IMPLEMENTATION — not yet implemented or verified**

Ticket: SENT-010 — Redis Foundation and Shared-State Design

## Purpose

Sentinel's current `LocalRateLimiter` keeps each client's token bucket in one gateway JVM. That is correct for one process but cannot enforce one shared allowance across multiple gateway instances. The proposed Redis design gives every gateway access to the same authoritative bucket state while preserving the existing token-bucket rules:

- capacity: five tokens;
- refill: one token per second;
- admitted requests consume one token;
- rejected requests consume no token;
- clients have independent buckets.

SENT-010 does not replace the local limiter or implement this design.

## Proposed bucket storage

One Redis hash would represent one client's bucket. A possible key shape is:

```text
sentinel:rate-limit:v1:{client-identity}
```

The braces form a Redis Cluster hash tag so one bucket's fields remain in one cluster slot if clustering is introduced later. The exact identity encoding and key format remain open.

For SENT-011, Java constructs the agreed key using the existing resolved numeric-IP identity and passes it as `KEYS[1]`. Lua must not construct or reinterpret client identity. Broader identity encoding remains open beyond this milestone.

Proposed hash fields:

| Field | Meaning |
| --- | --- |
| `available_units` | Remaining tokens represented as integer fixed-point units, including fractional credit |
| `last_refill_us` | Redis server timestamp, in microseconds, used by the last refill calculation |

Using integer units avoids losing fractional replenishment. For example, one token could equal 1,000,000 units. At one token per second, one elapsed microsecond adds one unit. Capacity, refill rate, and request cost should be supplied to the future admission operation rather than hidden as unrelated constants.

An expiration should eventually remove inactive client buckets. The exact expiration rule is still open and must not remove a bucket before its stored state is no longer relevant.

## Shared time source

`System.nanoTime()` is appropriate for elapsed time inside one JVM, but its origin is local to that JVM. Values from two gateway processes cannot be compared safely or stored as a shared timestamp.

The proposed atomic operation would use Redis `TIME`. Redis then supplies the clock for every gateway instance. The script would derive one microsecond timestamp from the returned seconds and microseconds.

## Why GET followed by SET is unsafe

Suppose one token remains:

1. Gateway A reads one token.
2. Gateway B reads the same one token before A writes.
3. Both gateways admit their requests.
4. Both write zero tokens.

Two requests consumed one logical token. A transaction assembled from separate client-side reads and writes has the same decision gap unless the entire conditional operation is protected correctly.

## Proposed atomic Lua admission operation

A future short Lua script would perform one indivisible Redis operation:

1. Receive the bucket key plus capacity, refill rate, request cost, and an approved expiration value.
2. Read Redis server time.
3. Read the bucket fields, or initialize a new full bucket.
4. Calculate elapsed time and add fractional refill units without exceeding capacity.
5. If at least one request cost is available, subtract it and return allowed.
6. Otherwise preserve the refilled balance and return rejected.
7. Store the updated balance and refill timestamp.
8. Apply the approved inactive-bucket expiration.
9. Return the admission result and useful state such as remaining units.

Redis executes a Lua script atomically, so another admission operation cannot interleave between these steps. The script must remain short because Redis cannot process unrelated commands while it is executing.

## Boundaries and open questions

- Redis is shared state for admission decisions; it is not an HTTP proxy.
- The rate limiter owns the admission policy. Redis may execute the atomic state transition.
- `LocalRateLimiter` remains the active implementation during SENT-010.
- `RedisRateLimiter` remains unannotated and disconnected from gateway wiring during SENT-011. `LocalRateLimiter` stays active until a separately approved cutover.
- The existing synchronous RateLimiter contract may use a bounded three-second wait only in isolated SENT-011 tests. It must not block a Netty event-loop thread.
- Redis errors propagate from isolated tests. No fallback or outage policy is selected.
- No Redis failure policy is selected here.
- No durability guarantee, failover behavior, or zero-downtime claim is made.
- Exact key encoding, expiration, numeric scale, Lua return structure, script loading strategy, and Redis deployment topology remain open.
- The Compose service is a local development fixture. Its execution has not been verified because Docker is unavailable in the current environment.

## SENT-010 connectivity evidence

- Root `compose.yaml` pins `redis:7.4.11-alpine`, publishes `127.0.0.1:6379:6379`, and defines a `redis-cli ping` health check. The file was inspected but not executed.
- Native Redis 8.8.0 was started on `127.0.0.1:6379` with persistence disabled for the check; native `redis-cli ping` returned `PONG`.
- Spring Boot resolved `spring-boot-starter-data-redis-reactive` 4.1.2 and Lettuce 7.5.2.RELEASE.
- `RedisConnectivityChecks` started the real Spring application context on an ephemeral port, obtained the auto-configured `ReactiveStringRedisTemplate`, and received `PONG` within a three-second bound.
- The Java 21 Maven package and complete existing gateway regression suite passed after the Redis dependency/configuration change.

These results verify local connectivity and regression safety. They do not verify Docker Compose, distributed admission, Lua correctness, Redis failure handling, persistence, or multi-instance enforcement.

## SENT-011 approved implementation parameters

The project owner approved the following for SENT-011:

- key: `sentinel:rate-limit:v1:{clientId}`, constructed centrally in Java from the existing numeric-IP identity;
- fields: `available_units` and `last_refill_us`;
- scale: 1,000,000 integer units per token;
- capacity: 5,000,000 units;
- refill: 1,000,000 units per second using Redis server time;
- request cost: 1,000,000 units;
- inactivity TTL: ten seconds, with verification that it cannot reset quota prematurely;
- Lua result: integer 1 for admitted or 0 for rejected;
- isolated synchronous wait bound: three seconds;
- Redis errors propagate during tests, with no fallback;
- no gateway wiring or Lua implementation outside the bounded SENT-011 operation.
