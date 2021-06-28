import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class GauvaTest {
    @Test
    public void givenLimitedResource_whenRequestOnce_thenShouldPermitWithoutBlocking() {
        // get an execution permit from the RateLimiter
        RateLimiter rateLimiter = RateLimiter.create(100);

        long startTime = ZonedDateTime.now().getSecond();
        // get 100 execution permit from the RateLimiter
        rateLimiter.acquire(100);
        // make 1 subsequent call to the throttled method
        doSomeLimitedOperation();
        long elapsedTimedSeconds = ZonedDateTime.now().getSecond() - startTime;

        // then
        assertThat(elapsedTimedSeconds <= 1);
    }

    @Test
    public void givenLimitedResource_whenUseRateLimiter_thenShouldLimitPermits() {
        // create a RateLimiter with 100 permits
        RateLimiter rateLimiter = RateLimiter.create(100);

        // when
        long startTime = ZonedDateTime.now().getSecond();

        // execute an action that needs to acquire 1000 permits
        IntStream.range(0, 1000).forEach(i -> {
            // When the acquire() method gets called, it blocks the executing thread until a permit is available.
            rateLimiter.acquire();
            doSomeLimitedOperation();
        });
        long elapsedTimeSeconds = ZonedDateTime.now().getSecond() - startTime;

        //According to the specification of the RateLimiter, such action will need at least 10 seconds to complete because we're able to execute only 100 units of action per second
        assertThat(elapsedTimeSeconds >= 10);
    }

    @Test
    public void givenLimitedResource_whenTryAcquire_shouldNotBlockIndefinitely() {
        // given
        RateLimiter rateLimiter = RateLimiter.create(1);

        // when
        rateLimiter.acquire();
        boolean result = rateLimiter.tryAcquire(2, 10, TimeUnit.MILLISECONDS);

        // then
        assertThat(result).isFalse();
    }

    private void doSomeLimitedOperation() {
        //some computing
    }
}
