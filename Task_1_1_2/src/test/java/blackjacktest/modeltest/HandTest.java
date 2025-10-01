package blackjacktest.modeltest;

import blackjack.i18n.I18nManager;
import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    private static final I18nManager i18n = I18nManager.getInstance();

    /**
     * Tests that cards can be added to a hand.
     * Verifies cards are added in the correct order and can be retrieved.
     */
    @Test
    public void testAddCard() {
        Hand hand = new Hand();
        Card card1 = new Card(Rank.TEN, Suit.HEARTS);
        Card card2 = new Card(Rank.ACE, Suit.SPADES);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(2, hand.getCardCount());
        assertEquals(card1, hand.getCard(0));
        assertEquals(card2, hand.getCard(1));
    }

    /**
     * Tests that adding cards beyond capacity throws an exception.
     * Verifies IllegalStateException is thrown when adding a 13th card.
     */
    @Test
    public void testAddCardOverflow() {
        Hand hand = new Hand();
        for (int i = 0; i < 12; i++) {
            hand.addCard(new Card(Rank.TEN, Suit.HEARTS));
        }
        assertThrows(IllegalStateException.class,
            () -> hand.addCard(new Card(Rank.ACE, Suit.SPADES)));
    }

    /**
     * Tests that getting cards with invalid indices throws exceptions.
     * Verifies IllegalArgumentException is thrown for out-of-bounds indices.
     */
    @Test
    public void testGetCardInvalidIndex() {
        Hand hand = new Hand();
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(0));
        Card card1 = new Card(Rank.TEN, Suit.HEARTS);
        hand.addCard(card1);
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(-1));
        assertThrows(IllegalArgumentException.class, () -> hand.getCard(1));
    }

    /**
     * Tests that the last card can be retrieved correctly.
     * Verifies getLastCard returns the most recently added card.
     */
    @Test
    public void testGetLastCard() {
        Hand hand = new Hand();
        Card card1 = new Card(Rank.TEN, Suit.HEARTS);
        Card card2 = new Card(Rank.ACE, Suit.SPADES);
        hand.addCard(card1);
        assertEquals(card1, hand.getLastCard());
        hand.addCard(card2);
        assertEquals(card2, hand.getLastCard());
    }

    /**
     * Tests that getting the last card from an empty hand throws an exception.
     * Verifies IllegalArgumentException is thrown when calling getLastCard on an empty hand.
     */
    @Test
    public void testGetLastCardFromEmptyHand() {
        Hand hand = new Hand();
        assertThrows(IllegalArgumentException.class, hand::getLastCard);
    }

    /**
     * Tests that hand value is calculated correctly.
     * Verifies correct Blackjack hand values with various card combinations.
     */
    @Test
    public void testGetValue() {
        Hand hand = new Hand();
        assertEquals(0, hand.getValue());
        hand.addCard(new Card(Rank.TEN, Suit.HEARTS));
        assertEquals(10, hand.getValue());
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        assertEquals(21, hand.getValue());
        hand.addCard(new Card(Rank.THREE, Suit.DIAMONDS));
        assertEquals(14, hand.getValue());
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(15, hand.getValue());
    }

    /**
     * Tests different scenarios for hand values calculation.
     */
    @ParameterizedTest
    @MethodSource("provideHandScenarios")
    public void testHandValues(Card[] cards, int expectedValue) {
        Hand hand = new Hand();
        for (Card card : cards) {
            hand.addCard(card);
        }
        assertEquals(expectedValue, hand.getValue());
    }

    private static Stream<Arguments> provideHandScenarios() {
        return Stream.of(
            Arguments.of(new Card[] {
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.CLUBS)
            }, 21),

            Arguments.of(new Card[] {
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS)
            }, 13),

            Arguments.of(new Card[] {
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.HEARTS)
            }, 24),

            Arguments.of(new Card[] {
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.THREE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.HEARTS)
            }, 20)
        );
    }

    /**
     * Tests that hand can be cleared.
     * Verifies the hand is empty after clearing.
     */
    @Test
    public void testClear() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        assertEquals(2, hand.getCardCount());
        hand.clear();
        assertEquals(0, hand.getCardCount());
        assertEquals(0, hand.getValue());
    }

    /**
     * Tests that string representation is correct.
     * Verifies the hand is properly formatted as a comma-separated list of cards.
     */
    @Test
    public void testToString() {
        Hand hand = new Hand();
        Card card1 = new Card(Rank.TEN, Suit.HEARTS);
        Card card2 = new Card(Rank.ACE, Suit.SPADES);
        hand.addCard(card1);
        hand.addCard(card2);

        String expectedTen = i18n.getString("rank.ten");
        String expectedHearts = i18n.getString("suit.hearts");
        String expectedAce = i18n.getString("rank.ace");
        String expectedSpades = i18n.getString("suit.spades");

        String expected = expectedTen + " " + expectedHearts + " (10), " +
                          expectedAce + " " + expectedSpades + " (11)";
        assertEquals(expected, hand.toString());
    }

    /**
     * Tests that getActualCardValue calculates correctly for different card positions.
     */
    @Test
    public void testGetActualCardValue() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        hand.addCard(new Card(Rank.ACE, Suit.DIAMONDS));

        assertEquals(11, hand.getActualCardValue(0));
        assertEquals(5, hand.getActualCardValue(1));
        assertEquals(1, hand.getActualCardValue(2));

        assertEquals(17, hand.getValue());
    }
}