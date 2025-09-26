package blackjack_test.participants_test;

import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Dealer;
import blackjack.participants.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.participants.Dealer functionality.
 * Tests dealer-specific behavior, hit/stand logic, and inheritance from Participant.
 */
class DealerTest {
    private Dealer dealer;

    /**
     * Setup method that runs before each test.
     * Creates a new Dealer instance for testing.
     */
    @BeforeEach
    void setUp() {
        dealer = new Dealer();
    }

    /**
     * Tests that shouldHit returns true when dealer has less than 17.
     * Verifies dealer hits with Ten and Five (total 15).
     */
    @Test
    void testShouldHitTrue() {
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.FIVE, Suit.SPADES));

        assertTrue(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit returns false when dealer has exactly 17.
     * Verifies dealer stands with Ten and Seven (total 17).
     */
    @Test
    void testShouldHitFalseWith17() {
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        assertFalse(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit returns false when dealer has 18.
     * Verifies dealer stands with Ten and Eight (total 18).
     */
    @Test
    void testShouldHitFalseWith18() {
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.EIGHT, Suit.SPADES));

        assertFalse(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit works correctly with Aces.
     * Verifies dealer stands with Ace and Six (soft 17).
     */
    @Test
    void testShouldHitWithAces() {
        dealer.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SIX, Suit.SPADES));

        assertFalse(dealer.shouldHit());
        assertEquals(17, dealer.getHand().getValue());
    }

    /**
     * Tests that shouldHit returns true for empty hand.
     * Verifies dealer should hit when no cards are dealt.
     */
    @Test
    void testShouldHitEmptyHand() {
        assertTrue(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit returns true when dealer has 16.
     * Verifies dealer hits with Ten and Six (total 16).
     */
    @Test
    void testShouldHitWith16() {
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SIX, Suit.SPADES));

        assertTrue(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit returns false when dealer has 21.
     * Verifies dealer stands with Blackjack (Ace and King).
     */
    @Test
    void testShouldHitWith21() {
        dealer.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertFalse(dealer.shouldHit());
    }

    /**
     * Tests that shouldHit returns false when dealer is busted.
     * Verifies dealer doesn't hit when already over 21.
     */
    @Test
    void testShouldHitBusted() {
        dealer.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));
        dealer.getHand().addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertFalse(dealer.shouldHit());
    }

    /**
     * Tests that Dealer properly inherits from Participant.
     * Verifies all inherited methods work correctly.
     */
    @Test
    void testInheritanceFromParticipant() {
        assertInstanceOf(Participant.class, dealer);

        dealer.incScore();
        assertEquals(1, dealer.getScore());

        dealer.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.KING, Suit.SPADES));
        assertTrue(dealer.hasBlackjack());

        dealer.getHand().clear();
        dealer.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));
        dealer.getHand().addCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertTrue(dealer.isBusted());
    }

    @Test
    void testDealerSpecificLogic() {
        dealer.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        assertEquals(17, dealer.getHand().getValue());
        assertFalse(dealer.shouldHit());

        dealer.getHand().clear();
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SIX, Suit.SPADES));

        assertEquals(16, dealer.getHand().getValue());
        assertTrue(dealer.shouldHit());
    }

    @Test
    void testDealerWithMultipleAces() {
        dealer.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.ACE, Suit.SPADES));

        assertEquals(2, dealer.getHand().getValue());
        assertTrue(dealer.shouldHit());

        dealer.getHand().addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertEquals(7, dealer.getHand().getValue());
        assertTrue(dealer.shouldHit());
    }
}
