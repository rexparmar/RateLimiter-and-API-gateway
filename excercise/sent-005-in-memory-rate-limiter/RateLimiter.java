public interface RateLimiter {
    boolean processRequest(String clientId);
}
