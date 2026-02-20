package prime_nums.test_util;

import prime_nums.detector.NonPrimeDetector;

public class BenchmarkUtils {
    public static double measure(NonPrimeDetector detector, int[] data, String label) {
        long startTime = System.nanoTime();
        boolean result = detector.containsNonPrime(data);
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        System.out.printf("%-20s: %.2f ms (Result: %s)%n", label, durationMs, result);
        return durationMs;
    }
}
