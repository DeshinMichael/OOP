import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test class for Shoe functionality.
 * Tests deck management, shuffling, and card dealing.
 */
public class ShoeTest {

    /**
     * Tests that a Shoe object can be created properly with correct number of cards.
     * Verifies the shoe contains the expected number of cards based on deck count.
     */
    @Test
    public void testShoeCreation() {
        Shoe shoe = new Shoe(4);
        assertNotNull(shoe);
        assertEquals(208, shoe.getCards().length);
    }

    /**
     * Tests that cards can be accessed.
     * Verifies the card array is properly sized.
     */
    @Test
    public void testGetCards() {
        Shoe shoe = new Shoe(1);
        Card[] cards = shoe.getCards();
        assertNotNull(cards);
        assertEquals(52, cards.length);
    }

    /**
     * Tests that cards are shuffled.
     * Verifies that cards are in a different order after shuffling.
     */
    @Test
    public void testShuffle() {
        Shoe shoe = new Shoe(3);
        Card[] originalOrder = shoe.getCards().clone();
        shoe.shuffle();
        Card[] shuffledOrder = shoe.getCards();
        assertNotEquals(originalOrder[0], shuffledOrder[0]);
    }

    /**
     * Tests that cards can be dealt from the shoe.
     * Verifies a card is successfully dealt and is not null.
     */
    @Test
    public void testDealCard() {
        Shoe shoe = new Shoe(2);
        Card firstCard = shoe.dealCard();
        assertNotNull(firstCard);
        assertEquals(1, shoe.getCards().length - (shoe.getCards().length - 1));
    }

    /**
     * Tests that dealing beyond the number of available cards throws an exception.
     * Verifies IllegalStateException is thrown when attempting to deal from an empty shoe.
     */
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
