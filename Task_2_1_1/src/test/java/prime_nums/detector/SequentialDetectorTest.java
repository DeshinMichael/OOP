package prime_nums.detector;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequentialDetectorTest {

    static Stream<Arguments> provideArraysForDetection() {
        int[] largePrimeArray = IntStream.generate(() -> 104729).limit(10000).toArray();
        int[] largeMixedArray = IntStream.concat(IntStream.generate(() -> 104729).limit(10000), IntStream.of(4)).toArray();

        return Stream.of(
            Arguments.of(new int[]{}, false),
            Arguments.of(new int[]{2, 3, 5, 7}, false),
            Arguments.of(new int[]{4, 6, 8, 9}, true),
            Arguments.of(new int[]{2, 3, 4, 5}, true),
            Arguments.of(new int[]{2, 2, 2, 9}, true),
            Arguments.of(new int[]{Integer.MAX_VALUE}, false),
            Arguments.of(new int[]{Integer.MAX_VALUE - 1}, true),
            Arguments.of(largePrimeArray, false),
            Arguments.of(largeMixedArray, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArraysForDetection")
    void containsNonPrimeShouldReturnCorrectResult(int[] numbers, boolean expected) {
        SequentialDetector detector = new SequentialDetector();
        assertEquals(expected, detector.containsNonPrime(numbers));
    }
}
