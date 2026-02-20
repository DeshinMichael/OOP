package prime_nums.detector;

import prime_nums.checker.PrimeUtils;

import java.util.Arrays;

public class StreamDetector implements NonPrimeDetector {
    @Override
    public boolean containsNonPrime(int[] numbers) {
        return Arrays.stream(numbers)
                .parallel()
                .anyMatch(n -> !PrimeUtils.isPrime(n));
    }
}
