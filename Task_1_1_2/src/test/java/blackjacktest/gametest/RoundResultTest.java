package blackjacktest.gametest;

import blackjack.game.RoundResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.game.RoundResult enum functionality.
 * Tests enum values and string representations.
 */
class RoundResultTest {

    /**
     * Tests that all enum values are present and in correct order.
     */
    @Test
    void testEnumValues() {
        RoundResult[] results = RoundResult.values();
        assertEquals(3, results.length);
        assertEquals(RoundResult.BLACKJACK, results[0]);
        assertEquals(RoundResult.BUST, results[1]);
        assertEquals(RoundResult.CONTINUE, results[2]);
    }

    /**
     * Tests valueOf method for getting enum by string representation.
     */
    @Test
    void testValueOf() {
        assertEquals(RoundResult.BLACKJACK, RoundResult.valueOf("BLACKJACK"));
        assertEquals(RoundResult.BUST, RoundResult.valueOf("BUST"));
        assertEquals(RoundResult.CONTINUE, RoundResult.valueOf("CONTINUE"));
    }

    /**
     * Tests that toString method returns correct string representation.
     */
    @Test
    void testToString() {
        assertEquals("BLACKJACK", RoundResult.BLACKJACK.toString());
        assertEquals("BUST", RoundResult.BUST.toString());
        assertEquals("CONTINUE", RoundResult.CONTINUE.toString());
    }
}
