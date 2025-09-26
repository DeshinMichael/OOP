package blackjack_test.participants_test;

import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Participant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for blackjack.participants.Participant functionality.
 * Tests basic participant behavior, scoring, and hand evaluation methods.
 */
class ParticipantTest {
    private Participant participant;

    /**
     * Setup method that runs before each test.
     * Creates a new Participant instance for testing.
     */
    @BeforeEach
    void setUp() {
        participant = new Participant();
    }

    /**
     * Tests that incScore correctly increments the participant's score.
     * Verifies score increases by one with each call.
     */
    @Test
    void testIncScore() {
        assertEquals(0, participant.getScore());

        participant.incScore();
        assertEquals(1, participant.getScore());

        participant.incScore();
        assertEquals(2, participant.getScore());

        for (int i = 0; i < 10; i++) {
            participant.incScore();
        }
        assertEquals(12, participant.getScore());
    }

    /**
     * Tests that hasBlackjack returns true for natural 21.
     * Verifies Ace and King combination is recognized as Blackjack.
     */
    @Test
    void testHasBlackjackTrue() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.KING, Suit.SPADES));

        assertTrue(participant.hasBlackjack());
    }

    /**
     * Tests that hasBlackjack returns false for two cards not totaling 21.
     * Verifies King and Nine combination is not Blackjack.
     */
    @Test
    void testHasBlackjackFalseWithTwoCardsButNot21() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.KING, Suit.HEARTS));
        hand.addCard(new Card(Rank.NINE, Suit.SPADES));

        assertFalse(participant.hasBlackjack());
    }

    /**
     * Tests that hasBlackjack returns false for 21 with more than two cards.
     * Verifies three sevens totaling 21 is not considered Blackjack.
     */
    @Test
    void testHasBlackjackFalseWith21ButMoreThanTwoCards() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.SEVEN, Suit.SPADES));
        hand.addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        assertEquals(21, hand.getValue());
        assertFalse(participant.hasBlackjack());
    }

    /**
     * Tests that hasBlackjack returns false for single card.
     * Verifies one Ace alone is not Blackjack.
     */
    @Test
    void testHasBlackjackFalseWithOneCard() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));

        assertFalse(participant.hasBlackjack());
    }

    /**
     * Tests that hasBlackjack returns false for empty hand.
     * Verifies empty hand is not considered Blackjack.
     */
    @Test
    void testHasBlackjackFalseWithNoCards() {
        assertFalse(participant.hasBlackjack());
    }

    /**
     * Tests that isBusted returns true when hand value exceeds 21.
     * Verifies participant is busted with King, Queen, and Five.
     */
    @Test
    void testIsBustedTrue() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.KING, Suit.HEARTS));
        hand.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        hand.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertTrue(participant.isBusted());
    }

    /**
     * Tests that isBusted returns false when hand value is 21 or less.
     * Verifies participant is not busted with valid hand values.
     */
    @Test
    void testIsBustedFalse() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.KING, Suit.SPADES));

        assertFalse(participant.isBusted());
    }

    /**
     * Tests that isBusted returns false for empty hand.
     * Verifies empty hand is not considered busted.
     */
    @Test
    void testIsBustedFalseWithNoCards() {
        assertFalse(participant.isBusted());
    }

    @Test
    void testIsBustedWithAcesAdjustment() {
        Hand hand = participant.getHand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.KING, Suit.CLUBS));

        assertFalse(participant.isBusted());
        assertEquals(12, hand.getValue());
    }
}
