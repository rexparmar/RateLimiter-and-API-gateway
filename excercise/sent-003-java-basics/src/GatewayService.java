public class GatewayService{
    private final RateLimiter rateLimiter;

    public GatewayService(RateLimiter rateLimiter){
        this.rateLimiter = rateLimiter;
    }

    public boolean processRequest(String clientId){
        return rateLimiter.processRequest(clientId);
    }
}

