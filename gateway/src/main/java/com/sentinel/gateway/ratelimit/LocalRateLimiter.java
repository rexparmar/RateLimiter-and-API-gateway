package com.sentinel.gateway.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

@Component
public class LocalRateLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, Integer> requestMap = new ConcurrentHashMap<>();

    @Override
    public boolean processRequest(String clientId) {
        AtomicBoolean admitted = new AtomicBoolean(false);

        requestMap.compute(clientId, (key, currentCount) -> {
            int count = currentCount == null ? 0 : currentCount;
            if (count >= 5) {
                return count;
            }

            admitted.set(true);
            return count + 1;
        });

        return admitted.get();
    }
}
