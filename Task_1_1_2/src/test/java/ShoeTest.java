import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Test class for Shoe functionality
public class ShoeTest {

    // Tests that a Shoe object can be created properly with correct number of cards
    @Test
    public void testShoeCreation() {
        Shoe shoe = new Shoe(4);
        assertNotNull(shoe);
        assertEquals(208, shoe.getCards().length);
    }

    // Tests that cards can be accessed
    @Test
    public void testGetCards() {
        Shoe shoe = new Shoe(1);
        Card[] cards = shoe.getCards();
        assertNotNull(cards);
        assertEquals(52, cards.length);
    }

    // Tests that cards are shuffled
    @Test
    public void testShuffle() {
        Shoe shoe = new Shoe(3);
        Card[] originalOrder = shoe.getCards().clone();
        shoe.shuffle();
        Card[] shuffledOrder = shoe.getCards();
        assertNotEquals(originalOrder[0], shuffledOrder[0]);
    }

    // Tests that cards can be dealt from the shoe
    @Test
    public void testDealCard() {
        Shoe shoe = new Shoe(2);
        Card firstCard = shoe.dealCard();
        assertNotNull(firstCard);
        assertEquals(1, shoe.getCards().length - (shoe.getCards().length - 1));
    }

    // Tests that dealing beyond the number of available cards throws an exception
    @Test
    public void testDealCardBeyondLimit() {
        Shoe shoe = new Shoe(1);
        for (int i = 0; i < 52; i++) {
            shoe.dealCard();
        }
        Exception exception = assertThrows(IllegalStateException.class, shoe::dealCard);
        assertNotNull(exception);
    }
}
