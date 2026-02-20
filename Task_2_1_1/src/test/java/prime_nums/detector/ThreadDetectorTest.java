package prime_nums.detector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import prime_nums.exception.ThreadException;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ThreadDetectorTest {

    static Stream<Arguments> provideScenarios() {
        int[] largePrimeArray = IntStream.generate(() -> 104729).limit(10000).toArray();
        int[] largeMixedArray = IntStream.concat(IntStream.generate(() -> 104729).limit(10000), IntStream.of(4)).toArray();

        return Stream.of(
            Arguments.of(1, new int[]{}, false),
            Arguments.of(4, new int[]{2, 3, 5, 7}, false),
            Arguments.of(2, new int[]{4, 6, 8}, true),
            Arguments.of(8, new int[]{2, 3, 4, 5}, true),
            Arguments.of(3, new int[]{2, 2, 2, 2, 2, 9}, true),
            Arguments.of(10, new int[]{4}, true),
            Arguments.of(4, new int[]{Integer.MAX_VALUE}, false),
            Arguments.of(2, new int[]{Integer.MAX_VALUE - 1}, true),
            Arguments.of(12, largePrimeArray, false),
            Arguments.of(12, largeMixedArray, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideScenarios")
    void containsNonPrimeShouldReturnCorrectResult(int threadCount, int[] numbers, boolean expected) throws ThreadException {
        ThreadDetector detector = new ThreadDetector(threadCount);
        assertEquals(expected, detector.containsNonPrime(numbers));
    }

    @Test
    void shouldThrowExceptionForZeroThreads() {
        assertThrows(ThreadException.class, () -> new ThreadDetector(0));
    }
}
