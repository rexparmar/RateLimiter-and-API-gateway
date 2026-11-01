public class Main {
    public static void main(String[] args) {
        LocalRateLimiter local = new LocalRateLimiter();
        GatewayService gateway = new GatewayService(local);

        boolean allowed = gateway.processRequest("client-1");
        System.out.println("Local limiter allowed : " + allowed);


        RejectAllRateLimiter rejectAll = new RejectAllRateLimiter();
        GatewayService gatewayReject = new GatewayService(rejectAll);
        boolean allowedByRejectAll = gatewayReject.processRequest("client-1");
        System.out.println("Reject all limiter allowed : " + allowedByRejectAll);

    }
}
