import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Test class for Hand functionality
public class HandTest {

    // Tests that a Hand object can be created properly
    @Test
    public void testCreationHand() {
        Hand hand = new Hand();
        assertNotNull(hand);
        assertEquals(0, hand.getCardCount());
        assertEquals(0, hand.getValue());
    }

    // Tests that cards can be added to a hand
    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        Card card1 = new Card("Десятка", "Черви");
        Card card2 = new Card("Туз", "Пики");
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(2, hand.getCardCount());
        assertEquals(card1, hand.getCard(0));
        assertEquals(card2, hand.getCard(1));
    }

    // Tests that adding cards beyond capacity throws an exception
    @Test
    public void testAddCardOverflow() {
        Hand hand = new Hand();
        for (int i = 0; i < 12; i++) {
            hand.addCard(new Card("Десятка", "Черви"));
        }
        assertThrows(IllegalStateException.class, () -> hand.addCard(new Card("Туз", "Пики")));
    }

    // Tests that cards can be retrieved from a hand
    @Test
    public void testGetCard(){
        Hand hand = new Hand();
        Card card1 = new Card("Десятка", "Черви");
        hand.addCard(card1);
        assertEquals(card1, hand.getCard(0));
    }

    // Tests that getting cards with invalid indices throws exceptions
    @Test
    public void testGetCardInvalidIndex(){
        Hand hand = new Hand();
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(0));
        Card card1 = new Card("Десятка", "Черви");
        hand.addCard(card1);
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(-1));
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(1));
    }

    // Tests that card count is tracked correctly
    @Test
    public void testGetCardCount() {
        Hand hand = new Hand();
        assertEquals(0, hand.getCardCount());
        hand.addCard(new Card("Десятка", "Черви"));
        assertEquals(1, hand.getCardCount());
        hand.addCard(new Card("Туз", "Пики"));
        assertEquals(2, hand.getCardCount());
    }

    // Tests that hand value is calculated correctly
    @Test
    public void testGetValue() {
        Hand hand = new Hand();
        assertEquals(0, hand.getValue());
        hand.addCard(new Card("Десятка", "Черви"));
        assertEquals(10, hand.getValue());
        hand.addCard(new Card("Туз", "Пики"));
        assertEquals(21, hand.getValue());
        hand.addCard(new Card("Тройка", "Буби"));
        assertEquals(14, hand.getValue());
        hand.addCard(new Card("Туз", "Крести"));
        assertEquals(15, hand.getValue());
    }

    // Tests that hand can be cleared
    @Test
    public void testClear() {
        Hand hand = new Hand();
        hand.addCard(new Card("Десятка", "Черви"));
        hand.addCard(new Card("Туз", "Пики"));
        assertEquals(2, hand.getCardCount());
        hand.clear();
        assertEquals(0, hand.getCardCount());
        assertEquals(0, hand.getValue());
    }

    // Tests that string representation is correct
    @Test
    public void testToString() {
        Hand hand = new Hand();
        hand.addCard(new Card("Десятка", "Черви"));
        hand.addCard(new Card("Туз", "Пики"));
        String expected = "Десятка Черви, Туз Пики";
        assertEquals(expected, hand.toString());
    }
}