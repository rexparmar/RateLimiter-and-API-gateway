package com.sentinel.gateway.filter;

import java.net.InetSocketAddress;

import com.sentinel.gateway.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class DemoRateLimitGatewayFilterFactory
        extends AbstractGatewayFilterFactory<DemoRateLimitGatewayFilterFactory.Config> {
    private final RateLimiter rateLimiter;

    public DemoRateLimitGatewayFilterFactory(RateLimiter rateLimiter) {
        super(Config.class);
        this.rateLimiter = rateLimiter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress == null || remoteAddress.getAddress() == null) {
                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                return exchange.getResponse().setComplete();
            }

            String clientId = remoteAddress.getAddress().getHostAddress();
            boolean admitted = rateLimiter.processRequest(clientId);
            if (admitted) {
                return chain.filter(exchange);
            }

            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        };
    }

    public static class Config {
        // This demo filter does not require configuration properties.
    }
}
