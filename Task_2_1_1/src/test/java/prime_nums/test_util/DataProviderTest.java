package prime_nums.test_util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataProviderTest {

    @Test
    void generateAllPrimesShouldCreateCorrectArray() {
        int size = 100;
        int prime = 17;

        int[] result = DataProvider.generateAllPrimes(size, prime);

        assertEquals(size, result.length);
        for (int num : result) {
            assertEquals(prime, num);
        }
    }

    @Test
    void generateRandomNumbersShouldCreateArrayWithinBounds() {
        int size = 50;
        int maxBound = 100;

        int[] result = DataProvider.generateRandomNumbers(size, maxBound);

        assertEquals(size, result.length);
        for (int num : result) {
            assertTrue(num >= 0 && num < maxBound, "Number should be within bounds");
        }
    }
}
