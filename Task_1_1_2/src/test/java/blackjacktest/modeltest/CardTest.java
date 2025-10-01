package blackjacktest.modeltest;

import blackjack.i18n.I18nManager;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for blackjack.model.Card functionality.
 * Validates the creation, property access, and string representation of cards.
 */
public class CardTest {
    private static final I18nManager i18n = I18nManager.getInstance();

    /**
     * Tests that string representation returns proper format.
     * Verifies the string format matches "rank suit".
     */
    @Test
    void testToString() {
        Card card = new Card(Rank.KING, Suit.SPADES);
        String expectedKing = i18n.getString("rank.king");
        String expectedSpades = i18n.getString("suit.spades");
        assertEquals(expectedKing + " " + expectedSpades, card.toString());
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
                assertEquals(rank.getDisplayName() + " " + suit.getDisplayName(),
                    card.toString());
            }
        }
    }

    /**
     * Tests that getValue returns the correct value based on card rank.
     */
    @ParameterizedTest
    @CsvSource({
        "TWO, 2",
        "THREE, 3",
        "FOUR, 4",
        "FIVE, 5",
        "SIX, 6",
        "SEVEN, 7",
        "EIGHT, 8",
        "NINE, 9",
        "TEN, 10",
        "JACK, 10",
        "QUEEN, 10",
        "KING, 10",
        "ACE, 11"
    })
    void testGetValue(Rank rank, int expectedValue) {
        Card card = new Card(rank, Suit.HEARTS);
        assertEquals(expectedValue, card.getValue());
    }

    /**
     * Tests that card value is correctly passed from rank.
     */
    @ParameterizedTest
    @EnumSource(Rank.class)
    void testValueFromRank(Rank rank) {
        Card card = new Card(rank, Suit.CLUBS);
        assertEquals(rank.getValue(), card.getValue());
    }
}
