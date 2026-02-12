package prime_nums.checker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeUtilsTest {

    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0, 1})
    void isPrimeShouldReturnFalseForValuesLessThanTwo(int n) {
        assertFalse(PrimeUtils.isPrime(n));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 104729})
    void isPrimeShouldReturnTrueForPrimes(int n) {
        assertTrue(PrimeUtils.isPrime(n));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 9, 10, 12, 14, 15, 25, 27, 33, 35, 49, 100, 104728})
    void isPrimeShouldReturnFalseForComposites(int n) {
        assertFalse(PrimeUtils.isPrime(n));
    }
}
