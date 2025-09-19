import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Test class for Card functionality
public class CardTest {

    // Tests that a card can be created properly
    @Test
    public void testCardCreation() {
        Card card = new Card("Дама", "Буби");
        assertNotNull(card);
        assertEquals("Дама", card.getRank());
    }

    // Tests the getRank method works correctly
    @Test
    public void testGetRank() {
        Card card = new Card("Туз", "Черви");
        assertEquals("Туз", card.getRank());
    }

    // Tests the toString method returns proper string representation
    @Test
    public void testToString() {
        Card card = new Card("Король", "Пики");
        assertEquals("Король Пики", card.toString());
    }
}
