package com.sentinel.gateway.ratelimit;

public interface RateLimiter {
    boolean processRequest(String clientId);
}
