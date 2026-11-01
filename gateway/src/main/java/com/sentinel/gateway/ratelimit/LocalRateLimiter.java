package com.sentinel.gateway.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.LongSupplier;

import org.springframework.stereotype.Component;

@Component
public class LocalRateLimiter implements RateLimiter {
    private static final long TOKEN_UNITS = 1_000_000_000L;
    private static final long CAPACITY_UNITS = 5 * TOKEN_UNITS;

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final LongSupplier nanoTime;

    public LocalRateLimiter() {
        this(System::nanoTime);
    }

    LocalRateLimiter(LongSupplier nanoTime) {
        this.nanoTime = nanoTime;
    }

    private static class Bucket {
        private final long availableUnits;
        private final long lastRefillNanos;

        private Bucket(long availableUnits, long lastRefillNanos) {
            this.availableUnits = availableUnits;
            this.lastRefillNanos = lastRefillNanos;
        }
    }

    @Override
    public boolean processRequest(String clientId) {
        AtomicBoolean admitted = new AtomicBoolean(false);

        buckets.compute(clientId, (key, currentBucket) -> {
            long now = nanoTime.getAsLong();
            Bucket bucket = currentBucket == null
                    ? new Bucket(CAPACITY_UNITS, now)
                    : currentBucket;

            long elapsedNanos = Math.max(0L, now - bucket.lastRefillNanos);
            long missingUnits = CAPACITY_UNITS - bucket.availableUnits;
            long refilledUnits = bucket.availableUnits
                    + Math.min(elapsedNanos, missingUnits);

            if (refilledUnits < TOKEN_UNITS) {
                return new Bucket(refilledUnits, now);
            }

            admitted.set(true);
            return new Bucket(
                    refilledUnits - TOKEN_UNITS,
                    now);
        });

        return admitted.get();
    }
}
