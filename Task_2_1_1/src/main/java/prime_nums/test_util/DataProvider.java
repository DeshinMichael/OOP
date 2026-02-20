package prime_nums.test_util;

import java.util.Arrays;
import java.util.Random;

public class DataProvider {
    public static int[] generateAllPrimes(int size, int primeNumber) {
        int[] numbers = new int[size];
        Arrays.fill(numbers, primeNumber);
        return numbers;
    }

    public static int[] generateRandomNumbers(int size, int maxBound) {
        Random random = new Random();
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(maxBound);
        }
        return numbers;
    }
}
