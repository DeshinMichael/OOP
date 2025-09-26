package blackjack_test.model_test;

import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HandTest {
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
        assertThrows(IllegalStateException.class, () -> hand.addCard(new Card(Rank.ACE, Suit.SPADES)));
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
        hand.addCard(new Card(Rank.TEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        String expected = "Десятка Черви, Туз Пики";
        assertEquals(expected, hand.toString());
    }
}