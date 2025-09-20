import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for Card functionality.
 * Validates the creation, property access, and string representation of cards.
 */
public class CardTest {

    /**
     * Tests that a card can be created properly.
     * Verifies the card is not null and has the expected rank.
     */
    @Test
    public void testCardCreation() {
        Card card = new Card("Дама", "Буби");
        assertNotNull(card);
        assertEquals("Дама", card.getRank());
    }

    /**
     * Tests the getRank method works correctly.
     * Ensures the rank returned matches the one used during creation.
     */
    @Test
    public void testGetRank() {
        Card card = new Card("Туз", "Черви");
        assertEquals("Туз", card.getRank());
    }

    /**
     * Tests the toString method returns proper string representation.
     * Verifies the string format matches "rank suit".
     */
    @Test
    public void testToString() {
        Card card = new Card("Король", "Пики");
        assertEquals("Король Пики", card.toString());
    }
}
