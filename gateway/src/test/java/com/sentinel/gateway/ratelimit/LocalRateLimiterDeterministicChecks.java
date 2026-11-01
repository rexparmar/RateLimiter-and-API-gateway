package com.sentinel.gateway.ratelimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class LocalRateLimiterDeterministicChecks {
    private static final long ONE_SECOND_NANOS = 1_000_000_000L;
    private static final long HALF_SECOND_NANOS = 500_000_000L;

    public static void main(String[] args) throws Exception {
        allowsInitialCapacityAndRejectsNextRequest();
        System.out.println("PASS: initial capacity");

        replenishesOneTokenAfterOneSecond();
        System.out.println("PASS: one-second refill");

        accumulatesFractionalRefill();
        System.out.println("PASS: fractional refill");

        capsRefillAtCapacity();
        System.out.println("PASS: capacity cap");

        keepsClientBucketsIndependent();
        System.out.println("PASS: independent clients");

        rejectedRequestsPreserveCredit();
        System.out.println("PASS: rejected requests preserve credit");

        admitsExactlyCapacityUnderConcurrency();
        System.out.println("PASS: concurrent admission");
    }

    private static void allowsInitialCapacityAndRejectsNextRequest() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);

        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest("client-a"),
                    "request " + request + " should be admitted");
        }

        check(!limiter.processRequest("client-a"),
                "request 6 should be rejected");
    }

    private static void replenishesOneTokenAfterOneSecond() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);

        exhaustBucket(limiter, "client-a");

        check(!limiter.processRequest("client-a"),
                "request before refill should be rejected");

        clock.advance(ONE_SECOND_NANOS);

        check(limiter.processRequest("client-a"),
                "one request should be admitted after one second");
        check(!limiter.processRequest("client-a"),
                "only one token should be available after one second");
    }

    private static void accumulatesFractionalRefill() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);
        exhaustBucket(limiter, "client-a");

        clock.advance(HALF_SECOND_NANOS);
        check(!limiter.processRequest("client-a"),
                "half a token should not admit a request");

        clock.advance(HALF_SECOND_NANOS);
        check(limiter.processRequest("client-a"),
                "two half-second refills should produce one token");
        check(!limiter.processRequest("client-a"),
                "the accumulated token should be consumed once");
    }

    private static void capsRefillAtCapacity() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);

        check(limiter.processRequest("client-a"),
                "first request should create the bucket and be admitted");
        clock.advance(10 * ONE_SECOND_NANOS);

        exhaustBucket(limiter, "client-a");
        check(!limiter.processRequest("client-a"),
                "a long refill must not exceed five-token capacity");
    }

    private static void keepsClientBucketsIndependent() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);

        exhaustBucket(limiter, "client-a");
        check(!limiter.processRequest("client-a"),
                "client-a should be exhausted");

        exhaustBucket(limiter, "client-b");
        check(!limiter.processRequest("client-b"),
                "client-b should have its own five-token bucket");
    }

    private static void rejectedRequestsPreserveCredit() {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);
        exhaustBucket(limiter, "client-a");

        clock.advance(HALF_SECOND_NANOS);
        for (int request = 1; request <= 10; request++) {
            check(!limiter.processRequest("client-a"),
                    "rejected request " + request + " must remain rejected");
        }

        clock.advance(HALF_SECOND_NANOS);
        check(limiter.processRequest("client-a"),
                "rejections must preserve the accumulated half token");
        check(!limiter.processRequest("client-a"),
                "the replenished token should be consumed only once");
    }

    private static void admitsExactlyCapacityUnderConcurrency() throws Exception {
        MutableNanoTime clock = new MutableNanoTime();
        LocalRateLimiter limiter = new LocalRateLimiter(clock);
        int requestCount = 100;
        int workerCount = 20;
        CountDownLatch ready = new CountDownLatch(workerCount);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        List<Future<Boolean>> results = new ArrayList<>();

        try {
            for (int request = 0; request < requestCount; request++) {
                results.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    return limiter.processRequest("client-a");
                }));
            }

            check(ready.await(10, TimeUnit.SECONDS),
                    "worker threads did not reach the start gate");
            start.countDown();

            int admitted = 0;
            for (Future<Boolean> result : results) {
                if (result.get(10, TimeUnit.SECONDS)) {
                    admitted++;
                }
            }

            check(admitted == 5,
                    "expected 5 concurrent admissions but got " + admitted);
            check(requestCount - admitted == 95,
                    "expected 95 concurrent rejections");
        } finally {
            start.countDown();
            executor.shutdownNow();
            check(executor.awaitTermination(10, TimeUnit.SECONDS),
                    "concurrency test executor did not stop");
        }
    }

    private static void exhaustBucket(LocalRateLimiter limiter, String clientId) {
        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest(clientId),
                    clientId + " request " + request + " should be admitted");
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static final class MutableNanoTime implements LongSupplier {
        private long now;

        @Override
        public long getAsLong() {
            return now;
        }

        private void advance(long nanoseconds) {
            now += nanoseconds;
        }
    }
}
