import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

public class TokenBucketTest {
    @Test
    public void allowRequest() {
        // get an execution permit from the RateLimiter
        TokenBucket rateLimiter = new TokenBucket(10, 1);

        IntStream.range(0, 10).forEach(i -> {
            // When the acquire() method gets called, it blocks the executing thread until a permit is available.
            if (rateLimiter.allowRequest(1)) {
                doSomeLimitedOperation();
            } else {
                doNothing();
            }
        });
    }

    @Test
    public void blockRequest() {
        // get an execution permit from the RateLimiter
        TokenBucket rateLimiter = new TokenBucket(10, 1);

        IntStream.range(0, 10).forEach(i -> {
            // When the acquire() method gets called, it blocks the executing thread until a permit is available.
            if (rateLimiter.allowRequest(11)) {
                doSomeLimitedOperation();
            } else {
                doNothing();
            }
        });
    }

    private void doSomeLimitedOperation() {
        //some computing
        System.out.println("Do something.");
    }

    private void doNothing() {
        //do nothing
        System.out.println("Do nothing.");
    }
}
