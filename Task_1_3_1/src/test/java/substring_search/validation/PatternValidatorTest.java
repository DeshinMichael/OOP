package substring_search.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import substring_search.exception.PatternException;

public class PatternValidatorTest {

    @Test
    void testValidPattern() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("valid pattern");
        });
    }

    @Test
    void testValidSingleCharPattern() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("a");
        });
    }

    @Test
    void testValidPatternWithSpecialChars() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("test@#$%");
        });
    }

    @Test
    void testValidPatternWithNumbers() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("123456");
        });
    }

    @Test
    void testValidLongPattern() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("very long pattern with many characters");
        });
    }

    @Test
    void testNullPattern() {
        PatternException exception = assertThrows(PatternException.class, () -> {
            PatternValidator.validatePattern(null);
        });
        assertEquals("Pattern must be no empty", exception.getMessage());
    }

    @Test
    void testEmptyPattern() {
        PatternException exception = assertThrows(PatternException.class, () -> {
            PatternValidator.validatePattern("");
        });
        assertEquals("Pattern must be no empty", exception.getMessage());
    }

    @Test
    void testPatternWithWhitespaceOnly() {
        assertDoesNotThrow(() -> {
            PatternValidator.validatePattern("   ");
        });
    }
}

