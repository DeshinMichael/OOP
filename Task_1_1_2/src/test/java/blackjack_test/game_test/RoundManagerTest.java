package blackjack_test.game_test;

import blackjack.deck.Shoe;
import blackjack.game.RoundResult;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Dealer;
import blackjack.participants.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.game.RoundManager functionality.
 * Tests round management, player-dealer interactions, and game scenarios.
 */
class RoundManagerTest {
    private Player player;
    private Dealer dealer;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Setup method that runs before each test.
     * Creates player and dealer instances and redirects output for testing.
     */
    @BeforeEach
    void setUp() {
        player = new Player();
        dealer = new Dealer();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Cleanup method that runs after each test.
     * Restores the original System.out stream.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests interaction between player and dealer with different hands.
     * Verifies hand values are calculated correctly for comparisons.
     */
    @Test
    void testPlayerAndDealerInteraction() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));

        assertEquals(19, player.getHand().getValue());
        assertEquals(17, dealer.getHand().getValue());

        assertTrue(player.getHand().getValue() > dealer.getHand().getValue());
    }

    /**
     * Tests Blackjack scenario detection.
     * Verifies natural 21 is properly recognized as Blackjack.
     */
    @Test
    void testBlackjackScenario() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertTrue(player.hasBlackjack());
        assertEquals(RoundResult.BLACKJACK, player.checkInitialHand());
    }

    /**
     * Tests bust scenario detection.
     * Verifies hands over 21 are properly recognized as busted.
     */
    @Test
    void testBustScenario() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertTrue(player.isBusted());
        assertTrue(player.getHand().getValue() > 21);
    }

    /**
     * Tests dealer's hit/stand logic.
     * Verifies dealer follows proper Blackjack rules for hitting.
     */
    @Test
    void testDealerLogic() {
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SIX, Suit.SPADES));

        assertTrue(dealer.shouldHit());

        dealer.getHand().clear();
        dealer.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        assertFalse(dealer.shouldHit());
    }

    @Test
    void testScoreTracking() {
        assertEquals(0, player.getScore());
        assertEquals(0, dealer.getScore());

        player.incScore();
        assertEquals(1, player.getScore());
        assertEquals(0, dealer.getScore());

        dealer.incScore();
        assertEquals(1, player.getScore());
        assertEquals(1, dealer.getScore());
    }

    @Test
    void testHandClearing() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertEquals(1, player.getHand().getCardCount());
        assertEquals(1, dealer.getHand().getCardCount());

        player.getHand().clear();
        dealer.getHand().clear();

        assertEquals(0, player.getHand().getCardCount());
        assertEquals(0, dealer.getHand().getCardCount());
    }

    @Test
    void testMultipleDeckSupport() {
        assertDoesNotThrow(() -> {
            new Shoe(1);
            new Shoe(2);
            new Shoe(4);
            new Shoe(6);
            new Shoe(8);
        });
    }
}
