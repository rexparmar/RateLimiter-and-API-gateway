public class RejectAllRateLimiter implements RateLimiter{
    
    @Override 
    public boolean processRequest(String clientId){
        return false;
    }
    
}
