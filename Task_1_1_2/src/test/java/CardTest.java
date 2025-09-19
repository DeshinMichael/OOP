import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card("Дама", "Буби");
        assertNotNull(card);
        assertEquals("Дама", card.getRank());
    }

    @Test
    public void testGetRank() {
        Card card = new Card("Туз", "Черви");
        assertEquals("Туз", card.getRank());
    }

    @Test
    public void testToString() {
        Card card = new Card("Король", "Пики");
        assertEquals("Король Пики", card.toString());
    }
}
