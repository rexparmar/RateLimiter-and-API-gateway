import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LocalRateLimiterTest {
    public static void main(String[] args) throws Exception {
        testSequentialRequests();
        testConcurrentRequests("same client", new String[]{"A"});
        testConcurrentRequests("independent clients", new String[]{"A", "B", "C"});
        System.out.println("ALL TESTS PASSED");
    }

    private static void testSequentialRequests() {
        LocalRateLimiter limiter = new LocalRateLimiter();

        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest("A"), "A request " + request + " should be allowed");
            checkCount(limiter, "A", request);
        }

        for (int request = 6; request <= 7; request++) {
            check(!limiter.processRequest("A"), "A request " + request + " should be rejected");
            checkCount(limiter, "A", 5);
        }

        // Use the same limiter to verify that exhausting A does not affect B.
        for (int request = 1; request <= 5; request++) {
            check(limiter.processRequest("B"), "B request " + request + " should be allowed");
            checkCount(limiter, "B", request);
            checkCount(limiter, "A", 5);
        }
        check(!limiter.processRequest("B"), "B request 6 should be rejected");
        checkCount(limiter, "B", 5);
        System.out.println("PASS sequential: A allowed=5 rejected=2 count=5; B allowed=5 rejected=1 count=5");
    }

    private static void testConcurrentRequests(String scenario, String[] clients) throws Exception {
        LocalRateLimiter limiter = new LocalRateLimiter();
        int requestsPerClient = 100;
        int workerCount = 16;
        ExecutorService workers = Executors.newFixedThreadPool(workerCount);

        // Wait for the first 16 tasks, not all queued tasks: only 16 can run at once.
        CountDownLatch ready = new CountDownLatch(workerCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> results = new ArrayList<>();

        try {
            for (int request = 0; request < requestsPerClient * clients.length; request++) {
                // Interleave clients so different clients compete during the same run.
                String clientId = clients[request % clients.length];
                results.add(workers.submit(() -> {
                    ready.countDown();
                    if (!start.await(10, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Timed out waiting for the starting gate");
                    }
                    return limiter.processRequest(clientId);
                }));
            }

            check(ready.await(10, TimeUnit.SECONDS), "Workers did not reach the starting gate");
            start.countDown();

            int[] allowed = new int[clients.length];
            int[] rejected = new int[clients.length];
            for (int request = 0; request < results.size(); request++) {
                int clientIndex = request % clients.length;
                // get() waits for completion and propagates worker failures to the test.
                if (results.get(request).get(10, TimeUnit.SECONDS)) {
                    allowed[clientIndex]++;
                } else {
                    rejected[clientIndex]++;
                }
            }

            for (int clientIndex = 0; clientIndex < clients.length; clientIndex++) {
                String clientId = clients[clientIndex];
                check(allowed[clientIndex] == 5,
                        scenario + ": " + clientId + " allowed=" + allowed[clientIndex] + ", expected 5");
                check(rejected[clientIndex] == 95,
                        scenario + ": " + clientId + " rejected=" + rejected[clientIndex] + ", expected 95");
                checkCount(limiter, clientId, 5);
                System.out.println("PASS concurrent " + scenario + ": " + clientId
                        + " allowed=" + allowed[clientIndex] + " rejected=" + rejected[clientIndex] + " count=5");
            }
        } finally {
            // Also release and stop workers if any assertion or task fails.
            start.countDown();
            workers.shutdownNow();
            check(workers.awaitTermination(10, TimeUnit.SECONDS), "Workers did not terminate");
        }
    }

    private static void checkCount(LocalRateLimiter limiter, String clientId, int expected) {
        Integer actual = limiter.requestMap.get(clientId);
        check(Integer.valueOf(expected).equals(actual),
                clientId + " stored count=" + actual + ", expected " + expected);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
