public class LocalRateLimiter implements RateLimiter{

    @Override 
    public boolean processRequest(String clientId){
        return true;
    }
    
}
