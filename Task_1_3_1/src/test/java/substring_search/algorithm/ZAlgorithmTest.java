package substring_search.algorithm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZAlgorithmTest {

    @Test
    void testZFunctionWithSimplePattern() {
        int[] result = ZAlgorithm.zFunction("aba");
        int[] expected = {3, 0, 1};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithRepeatingPattern() {
        int[] result = ZAlgorithm.zFunction("aabaaab");
        int[] expected = {7, 1, 0, 2, 3, 1, 0};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithSingleCharacter() {
        int[] result = ZAlgorithm.zFunction("a");
        int[] expected = {1};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithNoMatches() {
        int[] result = ZAlgorithm.zFunction("abcdef");
        int[] expected = {6, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithComplexPattern() {
        int[] result = ZAlgorithm.zFunction("abacaba");
        int[] expected = {7, 0, 1, 0, 3, 0, 1};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithAllSameCharacters() {
        int[] result = ZAlgorithm.zFunction("aaaa");
        int[] expected = {4, 3, 2, 1};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithEmptyString() {
        int[] result = ZAlgorithm.zFunction("");
        int[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    void testZFunctionWithLongPattern() {
        int[] result = ZAlgorithm.zFunction("abcabcabc");
        int[] expected = {9, 0, 0, 6, 0, 0, 3, 0, 0};
        assertArrayEquals(expected, result);
    }
}
