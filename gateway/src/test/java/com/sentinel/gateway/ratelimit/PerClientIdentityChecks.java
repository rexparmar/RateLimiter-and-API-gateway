package com.sentinel.gateway.ratelimit;

import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import com.sentinel.gateway.filter.DemoRateLimitGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class PerClientIdentityChecks {
    public static void main(String[] args) throws Exception {
        sameRemoteIpSharesQuotaAndExcludesPort();
        System.out.println("PASS: same remote IP shares quota");

        differentRemoteIpsHaveIndependentQuotas();
        System.out.println("PASS: different remote IPs are independent");

        spoofedHeadersDoNotChangeIdentity();
        System.out.println("PASS: spoofed identity headers are ignored");

        missingRemoteAddressFailsClosed();
        System.out.println("PASS: missing remote address returns 503");

        unresolvedRemoteAddressFailsClosed();
        System.out.println("PASS: unresolved remote address returns 503");
    }

    private static void sameRemoteIpSharesQuotaAndExcludesPort() throws Exception {
        GatewayFilter filter = filterWithFrozenLimiter();
        InetAddress address = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});

        for (int request = 1; request <= 5; request++) {
            ExchangeFixture fixture = new ExchangeFixture(
                    new InetSocketAddress(address, 10_000 + request), new HttpHeaders());
            fixture.run(filter);
            check(fixture.chainCalls.get() == 1,
                    "same-IP request " + request + " should be forwarded");
            check(fixture.status == null,
                    "admitted request should not set an error status");
        }

        ExchangeFixture rejected = new ExchangeFixture(
                new InetSocketAddress(address, 20_000), new HttpHeaders());
        rejected.run(filter);
        check(rejected.status == HttpStatus.TOO_MANY_REQUESTS,
                "sixth same-IP request should return 429");
        check(rejected.chainCalls.get() == 0,
                "429 request must not call the forwarding chain");
    }

    private static void differentRemoteIpsHaveIndependentQuotas() throws Exception {
        GatewayFilter filter = filterWithFrozenLimiter();
        InetAddress firstAddress = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});
        InetAddress secondAddress = InetAddress.getByAddress(new byte[] {127, 0, 0, 2});

        exhaustAndCheckRejection(filter, firstAddress, "first IP");
        exhaustAndCheckRejection(filter, secondAddress, "second IP");
    }

    private static void spoofedHeadersDoNotChangeIdentity() throws Exception {
        GatewayFilter filter = filterWithFrozenLimiter();
        InetAddress address = InetAddress.getByAddress(new byte[] {127, 0, 0, 1});

        for (int request = 1; request <= 6; request++) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Client-ID", "spoof-client-" + request);
            headers.set("X-Forwarded-For", "198.51.100." + request);
            headers.set("Forwarded", "for=203.0.113." + request);
            ExchangeFixture fixture = new ExchangeFixture(
                    new InetSocketAddress(address, 30_000 + request), headers);
            fixture.run(filter);

            if (request <= 5) {
                check(fixture.chainCalls.get() == 1,
                        "spoofed headers must not block admitted request " + request);
            } else {
                check(fixture.status == HttpStatus.TOO_MANY_REQUESTS,
                        "changing spoofed headers must not reset the quota");
                check(fixture.chainCalls.get() == 0,
                        "spoofed-header 429 must not call the forwarding chain");
            }
        }
    }

    private static void missingRemoteAddressFailsClosed() {
        CountingRateLimiter limiter = new CountingRateLimiter();
        GatewayFilter filter = new DemoRateLimitGatewayFilterFactory(limiter)
                .apply(new DemoRateLimitGatewayFilterFactory.Config());
        ExchangeFixture fixture = new ExchangeFixture(null, new HttpHeaders());

        fixture.run(filter);

        check(fixture.status == HttpStatus.SERVICE_UNAVAILABLE,
                "missing remote address should return 503");
        check(fixture.responseCompletions.get() == 1,
                "missing remote address should complete the response");
        check(limiter.calls.get() == 0,
                "missing remote address must not invoke the limiter");
        check(fixture.chainCalls.get() == 0,
                "missing remote address must not call the forwarding chain");
    }

    private static void unresolvedRemoteAddressFailsClosed() {
        CountingRateLimiter limiter = new CountingRateLimiter();
        GatewayFilter filter = new DemoRateLimitGatewayFilterFactory(limiter)
                .apply(new DemoRateLimitGatewayFilterFactory.Config());
        InetSocketAddress unresolved = InetSocketAddress.createUnresolved(
                "unresolved.invalid", 40_000);
        ExchangeFixture fixture = new ExchangeFixture(unresolved, new HttpHeaders());

        fixture.run(filter);

        check(fixture.status == HttpStatus.SERVICE_UNAVAILABLE,
                "unresolved remote address should return 503");
        check(fixture.responseCompletions.get() == 1,
                "unresolved remote address should complete the response");
        check(limiter.calls.get() == 0,
                "unresolved remote address must not invoke the limiter");
        check(fixture.chainCalls.get() == 0,
                "unresolved remote address must not call the forwarding chain");
    }

    private static GatewayFilter filterWithFrozenLimiter() {
        LocalRateLimiter limiter = new LocalRateLimiter(() -> 0L);
        return new DemoRateLimitGatewayFilterFactory(limiter)
                .apply(new DemoRateLimitGatewayFilterFactory.Config());
    }

    private static void exhaustAndCheckRejection(
            GatewayFilter filter, InetAddress address, String description) {
        for (int request = 1; request <= 5; request++) {
            ExchangeFixture fixture = new ExchangeFixture(
                    new InetSocketAddress(address, 50_000 + request), new HttpHeaders());
            fixture.run(filter);
            check(fixture.chainCalls.get() == 1,
                    description + " request " + request + " should be forwarded");
        }

        ExchangeFixture rejected = new ExchangeFixture(
                new InetSocketAddress(address, 60_000), new HttpHeaders());
        rejected.run(filter);
        check(rejected.status == HttpStatus.TOO_MANY_REQUESTS,
                description + " sixth request should return 429");
        check(rejected.chainCalls.get() == 0,
                description + " rejected request must not be forwarded");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static final class CountingRateLimiter implements RateLimiter {
        private final AtomicInteger calls = new AtomicInteger();

        @Override
        public boolean processRequest(String clientId) {
            calls.incrementAndGet();
            return true;
        }
    }

    private static final class ExchangeFixture {
        private final InetSocketAddress remoteAddress;
        private final HttpHeaders headers;
        private final AtomicInteger chainCalls = new AtomicInteger();
        private final AtomicInteger responseCompletions = new AtomicInteger();
        private HttpStatusCode status;

        private ExchangeFixture(InetSocketAddress remoteAddress, HttpHeaders headers) {
            this.remoteAddress = remoteAddress;
            this.headers = headers;
        }

        private void run(GatewayFilter filter) {
            GatewayFilterChain chain = exchange -> {
                chainCalls.incrementAndGet();
                return Mono.empty();
            };
            filter.filter(exchange(), chain).block();
        }

        private ServerWebExchange exchange() {
            ServerHttpRequest request = proxy(ServerHttpRequest.class, (proxy, method, args) -> {
                return switch (method.getName()) {
                    case "getRemoteAddress" -> remoteAddress;
                    case "getHeaders" -> headers;
                    case "toString" -> "ControlledServerHttpRequest";
                    default -> throw new AssertionError(
                            "Unexpected request method: " + method.getName());
                };
            });

            ServerHttpResponse response = proxy(ServerHttpResponse.class, (proxy, method, args) -> {
                return switch (method.getName()) {
                    case "setStatusCode" -> {
                        status = (HttpStatusCode) args[0];
                        yield true;
                    }
                    case "setComplete" -> {
                        responseCompletions.incrementAndGet();
                        yield Mono.empty();
                    }
                    case "toString" -> "ControlledServerHttpResponse";
                    default -> throw new AssertionError(
                            "Unexpected response method: " + method.getName());
                };
            });

            return proxy(ServerWebExchange.class, (proxy, method, args) -> {
                return switch (method.getName()) {
                    case "getRequest" -> request;
                    case "getResponse" -> response;
                    case "toString" -> "ControlledServerWebExchange";
                    default -> throw new AssertionError(
                            "Unexpected exchange method: " + method.getName());
                };
            });
        }

        private static <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler) {
            return type.cast(Proxy.newProxyInstance(
                    type.getClassLoader(), new Class<?>[] {type}, handler));
        }
    }
}
