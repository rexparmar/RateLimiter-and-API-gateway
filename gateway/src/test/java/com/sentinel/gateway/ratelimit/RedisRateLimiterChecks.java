package com.sentinel.gateway.ratelimit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sentinel.gateway.SentinelGatewayApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

public final class RedisRateLimiterChecks {
    private static final Duration REDIS_TIMEOUT = Duration.ofSeconds(3);
    private static final String CLIENT_A = "192.0.2.10";
    private static final String CLIENT_B = "192.0.2.11";
    private static final String CONCURRENT_CLIENT = "192.0.2.12";
    private static final String EXPIRATION_CLIENT = "192.0.2.13";
    private static final long REQUEST_COST_UNITS = 1_000_000L;
    private static final long MAXIMUM_REFILL_MILLIS = 5_000L;

    private RedisRateLimiterChecks() {
    }

    public static void main(String[] args) throws Exception {
        try (ConfigurableApplicationContext context =
                     new SpringApplicationBuilder(SentinelGatewayApplication.class)
                             .run("--server.port=0", "--spring.main.banner-mode=off")) {
            ReactiveStringRedisTemplate redis =
                    context.getBean(ReactiveStringRedisTemplate.class);
            RedisRateLimiter limiter = new RedisRateLimiter(redis);

            deleteBucket(redis, CLIENT_A);
            deleteBucket(redis, CLIENT_B);
            try {
                verifyInitialCapacity(limiter);
                verifyIndependentClient(limiter);
                verifySharedConcurrentQuota(redis);
                verifyRejectedRequestAndExpiration(redis, limiter);
            } finally {
                deleteBucket(redis, CLIENT_A);
                deleteBucket(redis, CLIENT_B);
                deleteBucket(redis, EXPIRATION_CLIENT);
            }
        }
    }

    private static void verifyInitialCapacity(RedisRateLimiter limiter) {
        long startedAt = System.nanoTime();

        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest(CLIENT_A),
                    "Request " + request + " should be admitted");
        }
        check(!limiter.processRequest(CLIENT_A),
                "The sixth immediate request should be rejected");

        long elapsedNanos = System.nanoTime() - startedAt;
        check(elapsedNanos < 1_000_000_000L,
                "Sequential check took long enough to refill a complete token");

        System.out.println("PASS: Redis limiter initial capacity");
    }

    private static void verifyIndependentClient(RedisRateLimiter limiter) {
        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest(CLIENT_B),
                    "Client B request " + request + " should be admitted");
        }
        check(!limiter.processRequest(CLIENT_B),
                "Client B's sixth immediate request should be rejected");

        System.out.println("PASS: Redis limiter independent clients");
    }

    private static void verifySharedConcurrentQuota(
            ReactiveStringRedisTemplate redis) throws Exception {
        RedisRateLimiter firstLimiter = new RedisRateLimiter(redis);
        RedisRateLimiter secondLimiter = new RedisRateLimiter(redis);
        CountDownLatch startGate = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<Boolean>> results = new ArrayList<>();

        deleteBucket(redis, CONCURRENT_CLIENT);
        try {
            for (int request = 0; request < 100; request++) {
                RedisRateLimiter selectedLimiter = request % 2 == 0
                        ? firstLimiter
                        : secondLimiter;
                results.add(executor.submit(() -> {
                    startGate.await();
                    return selectedLimiter.processRequest(CONCURRENT_CLIENT);
                }));
            }

            long startedAt = System.nanoTime();
            startGate.countDown();

            int admitted = 0;
            for (Future<Boolean> result : results) {
                if (result.get()) {
                    admitted++;
                }
            }
            long elapsedNanos = System.nanoTime() - startedAt;
            int rejected = results.size() - admitted;

            check(elapsedNanos < 1_000_000_000L,
                    "Concurrent check took long enough to refill a complete token");
            check(admitted == 5,
                    "Expected exactly 5 admissions but observed " + admitted);
            check(rejected == 95,
                    "Expected exactly 95 rejections but observed " + rejected);

            System.out.println("PASS: Redis limiter shared concurrent quota");
        } finally {
            startGate.countDown();
            executor.shutdownNow();
            executor.awaitTermination(3, TimeUnit.SECONDS);
            deleteBucket(redis, CONCURRENT_CLIENT);
        }
    }

    private static void verifyRejectedRequestAndExpiration(
            ReactiveStringRedisTemplate redis,
            RedisRateLimiter limiter) {
        deleteBucket(redis, EXPIRATION_CLIENT);

        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest(EXPIRATION_CLIENT),
                    "Setup request " + request + " should be admitted");
        }

        long unitsBeforeRejection = readAvailableUnits(redis, EXPIRATION_CLIENT);
        check(!limiter.processRequest(EXPIRATION_CLIENT),
                "An immediate request against the exhausted bucket should be rejected");
        long unitsAfterRejection = readAvailableUnits(redis, EXPIRATION_CLIENT);
        Duration remainingTtl = redis.getExpire(
                        RedisRateLimiter.keyFor(EXPIRATION_CLIENT))
                .block(REDIS_TIMEOUT);

        check(unitsAfterRejection >= unitsBeforeRejection,
                "A rejected request consumed stored token credit");
        check(unitsAfterRejection < REQUEST_COST_UNITS,
                "The rejection check took long enough to refill a complete token");
        check(remainingTtl != null
                        && remainingTtl.toMillis() > MAXIMUM_REFILL_MILLIS,
                "Bucket TTL could expire before an empty bucket naturally refills");

        System.out.println("PASS: rejected request preserves credit and TTL is safe");
    }

    private static long readAvailableUnits(
            ReactiveStringRedisTemplate redis,
            String clientId) {
        Object value = redis.opsForHash()
                .get(RedisRateLimiter.keyFor(clientId), "available_units")
                .block(REDIS_TIMEOUT);
        check(value != null, "Redis bucket is missing available_units");
        return Long.parseLong(value.toString());
    }

    private static void deleteBucket(
            ReactiveStringRedisTemplate redis,
            String clientId) {
        redis.delete(RedisRateLimiter.keyFor(clientId))
                .block(REDIS_TIMEOUT);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
