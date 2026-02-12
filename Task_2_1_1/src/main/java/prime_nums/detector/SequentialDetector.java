package prime_nums.detector;

import prime_nums.checker.PrimeUtils;

public class SequentialDetector implements NonPrimeDetector {
    public boolean containsNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!PrimeUtils.isPrime(number)) {
                return true;
            }
        }
        return false;
    }
}
