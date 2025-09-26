package blackjack_test.model_test;

import blackjack.model.Rank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for blackjack.model.Rank functionality.
 * Validates rank display names, values, and enum operations.
 */
class RankTest {
    /**
     * Tests that toString method returns the display name.
     * Verifies toString matches getDisplayName for all ranks.
     */
    @Test
    void testToString() {
        assertEquals("Двойка", Rank.TWO.toString());
        assertEquals("Валет", Rank.JACK.toString());
        assertEquals("Дама", Rank.QUEEN.toString());
        assertEquals("Король", Rank.KING.toString());
        assertEquals("Туз", Rank.ACE.toString());
    }

    /**
     * Tests that all enum values are present and in correct order.
     * Verifies there are 13 ranks total and they are ordered correctly.
     */
    @Test
    void testEnumValues() {
        Rank[] ranks = Rank.values();
        assertEquals(13, ranks.length);
        assertEquals(Rank.TWO, ranks[0]);
        assertEquals(Rank.THREE, ranks[1]);
        assertEquals(Rank.ACE, ranks[12]);
    }
}
