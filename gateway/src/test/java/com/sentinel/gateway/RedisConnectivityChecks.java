package com.sentinel.gateway;

import java.time.Duration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

public final class RedisConnectivityChecks {

    private RedisConnectivityChecks() {
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context =
                     new SpringApplicationBuilder(SentinelGatewayApplication.class)
                             .run("--server.port=0", "--spring.main.banner-mode=off")) {
            ReactiveStringRedisTemplate redis =
                    context.getBean(ReactiveStringRedisTemplate.class);

            String response = ping(redis);
            if (!"PONG".equals(response)) {
                throw new AssertionError("Expected Redis PONG but received: " + response);
            }

            System.out.println("PASS: Spring Redis connectivity returned PONG");
        }
    }

    private static String ping(ReactiveStringRedisTemplate redis) {
        return redis.getConnectionFactory()
                .getReactiveConnection()
                .ping()
                .block(Duration.ofSeconds(3));
    }
}
