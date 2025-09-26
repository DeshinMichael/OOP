package blackjack_test.game_test;

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
     * Verifies there are 5 round result types and they are ordered correctly.
     */
    @Test
    void testEnumValues() {
        RoundResult[] results = RoundResult.values();
        assertEquals(5, results.length);
        assertEquals(RoundResult.BLACKJACK, results[0]);
        assertEquals(RoundResult.BUST, results[1]);
        assertEquals(RoundResult.STAND, results[2]);
        assertEquals(RoundResult.GOT_21, results[3]);
        assertEquals(RoundResult.CONTINUE, results[4]);
    }

    /**
     * Tests that toString method returns correct string representation.
     * Verifies each enum value has proper string representation.
     */
    @Test
    void testToString() {
        assertEquals("BLACKJACK", RoundResult.BLACKJACK.toString());
        assertEquals("BUST", RoundResult.BUST.toString());
        assertEquals("STAND", RoundResult.STAND.toString());
        assertEquals("GOT_21", RoundResult.GOT_21.toString());
        assertEquals("CONTINUE", RoundResult.CONTINUE.toString());
    }
}
