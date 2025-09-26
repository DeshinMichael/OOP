package blackjack_test.model_test;

import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.model.Card functionality.
 * Validates the creation, property access, and string representation of cards.
 */
public class CardTest {
    /**
     * Tests that string representation returns proper format.
     * Verifies the string format matches "rank suit".
     */
    @Test
    void testToString() {
        Card card = new Card(Rank.KING, Suit.SPADES);
        assertEquals("Король Пики", card.toString());
    }

    /**
     * Tests card creation with all possible rank and suit combinations.
     * Verifies that every combination of rank and suit creates a valid card
     * with correct properties and string representation.
     */
    @Test
    void testAllRanksAndSuits() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                Card card = new Card(rank, suit);
                assertEquals(rank, card.rank());
                assertEquals(suit, card.suit());
                assertEquals(rank.getValue(), card.getValue());
                assertEquals(rank.getDisplayName() + " " + suit.getDisplayName(), card.toString());
            }
        }
    }
}
