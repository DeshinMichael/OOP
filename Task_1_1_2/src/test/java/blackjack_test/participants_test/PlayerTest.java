package blackjack_test.participants_test;

import blackjack.game.RoundResult;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Participant;
import blackjack.participants.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.participants.Player functionality.
 * Tests player-specific behavior, hand evaluation, and inheritance from Participant.
 */
class PlayerTest {
    private Player player;

    /**
     * Setup method that runs before each test.
     * Creates a new Player instance for testing.
     */
    @BeforeEach
    void setUp() {
        player = new Player();
    }

    /**
     * Tests that has21 returns true when player has exactly 21 points.
     * Verifies Ace and King combination equals 21.
     */
    @Test
    void testHas21True() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertTrue(player.has21());
    }

    /**
     * Tests that has21 returns false when player doesn't have 21 points.
     * Verifies King and Nine combination doesn't equal 21.
     */
    @Test
    void testHas21False() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        assertFalse(player.has21());
    }

    /**
     * Tests that has21 returns false for empty hand.
     * Verifies empty hand doesn't count as having 21.
     */
    @Test
    void testHas21EmptyHand() {
        assertFalse(player.has21());
    }

    /**
     * Tests that checkInitialHand returns BLACKJACK for natural 21.
     * Verifies Ace and King initial hand is recognized as Blackjack.
     */
    @Test
    void testCheckInitialHandBlackjack() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.BLACKJACK, result);
    }

    /**
     * Tests that checkInitialHand returns CONTINUE for non-Blackjack hands.
     * Verifies hands without natural 21 allow game to continue.
     */
    @Test
    void testCheckInitialHandContinue() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.CONTINUE, result);
    }

    /**
     * Tests that checkInitialHand returns CONTINUE for empty hand.
     * Verifies empty hand allows game to continue.
     */
    @Test
    void testCheckInitialHandEmptyHand() {
        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.CONTINUE, result);
    }

    /**
     * Tests that Player properly inherits from Participant.
     * Verifies all inherited methods work correctly.
     */
    @Test
    void testInheritanceFromParticipant() {
        assertInstanceOf(Participant.class, player);

        player.incScore();
        assertEquals(1, player.getScore());

        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));
        assertTrue(player.hasBlackjack());

        player.getHand().clear();
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertTrue(player.isBusted());
    }

    /**
     * Tests player-specific logic for 21 without Blackjack.
     * Verifies three sevens equals 21 but is not Blackjack.
     */
    @Test
    void testPlayerSpecificLogic() {
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        assertTrue(player.has21());
        assertFalse(player.hasBlackjack());
    }
}
