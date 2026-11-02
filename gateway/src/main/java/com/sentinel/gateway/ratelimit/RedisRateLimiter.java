package com.sentinel.gateway.ratelimit;

import java.time.Duration;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public final class RedisRateLimiter implements RateLimiter {
    private static final long CAPACITY_UNITS = 5_000_000L;
    private static final long REQUEST_COST_UNITS = 1_000_000L;
    private static final long BUCKET_TTL_MILLISECONDS = 10_000L;
    private static final String KEY_PREFIX = "sentinel:rate-limit:v1:";

    private final ReactiveStringRedisTemplate redis;
    private final DefaultRedisScript<Long> tokenBucketScript;

    public RedisRateLimiter(ReactiveStringRedisTemplate redis) {
        this.redis = redis;
        this.tokenBucketScript = new DefaultRedisScript<>();
        this.tokenBucketScript.setLocation(
                new ClassPathResource("scripts/token_bucket.lua"));
        this.tokenBucketScript.setResultType(Long.class);
    }

    static String keyFor(String clientId) {
        return KEY_PREFIX + "{" + clientId + "}";
    }

    @Override
    public boolean processRequest(String clientId) {
        Long result = redis.execute(
                        tokenBucketScript,
                        List.of(keyFor(clientId)),
                        Long.toString(CAPACITY_UNITS),
                        Long.toString(REQUEST_COST_UNITS),
                        Long.toString(BUCKET_TTL_MILLISECONDS))
                .next()
                .block(Duration.ofSeconds(3));

        if (result == null) {
            throw new IllegalStateException("Redis token bucket returned no result");
        }

        return result == 1L;
    }
}
