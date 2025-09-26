package blackjack_test.model_test;

import blackjack.model.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.model.Suit functionality.
 * Validates suit display names and enum operations.
 */
class SuitTest {
    /**
     * Tests that toString method returns the display name.
     * Verifies toString matches getDisplayName for all suits.
     */
    @Test
    void testToString() {
        assertEquals("Черви", Suit.HEARTS.toString());
        assertEquals("Буби", Suit.DIAMONDS.toString());
        assertEquals("Крести", Suit.CLUBS.toString());
        assertEquals("Пики", Suit.SPADES.toString());
    }

    /**
     * Tests that all enum values are present and in correct order.
     * Verifies there are 4 suits total and they are ordered correctly.
     */
    @Test
    void testEnumValues() {
        Suit[] suits = Suit.values();
        assertEquals(4, suits.length);
        assertEquals(Suit.HEARTS, suits[0]);
        assertEquals(Suit.DIAMONDS, suits[1]);
        assertEquals(Suit.CLUBS, suits[2]);
        assertEquals(Suit.SPADES, suits[3]);
    }
}
