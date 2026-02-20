package prime_nums.test_util;

import org.junit.jupiter.api.Test;
import prime_nums.detector.NonPrimeDetector;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BenchmarkUtilsTest {

    @Test
    void measureShouldReturnNonNegativeDuration() {
        NonPrimeDetector dummyDetector = numbers -> true;
        int[] data = new int[]{1, 2, 3};

        double duration = BenchmarkUtils.measure(dummyDetector, data, "Test Label");

        assertTrue(duration >= 0, "Duration should be non-negative");
    }
}
