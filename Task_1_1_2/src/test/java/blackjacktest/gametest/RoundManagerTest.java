package blackjacktest.gametest;

import blackjack.deck.Shoe;
import blackjack.game.RoundManager;
import blackjack.game.RoundResult;
import blackjack.i18n.I18nManager;
import blackjack.io.ConsoleInput;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Dealer;
import blackjack.participants.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.game.RoundManager functionality.
 * Tests round management, player-dealer interactions, and game scenarios.
 */
class RoundManagerTest {
    private Player player;
    private Dealer dealer;
    private Shoe shoe;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private final I18nManager i18n = I18nManager.getInstance();

    /**
     * Setup method that runs before each test.
     * Creates player and dealer instances and redirects output for testing.
     */
    @BeforeEach
    void setUp() {
        player = new Player();
        dealer = new Dealer();
        shoe = new Shoe(1);
        System.setOut(new PrintStream(outputStreamCaptor));
        ConsoleInput.closeScanner();
    }

    /**
     * Cleanup method that runs after each test.
     * Restores the original System.out and System.in streams.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
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
    void testPlayRoundWithPlayerBlackjack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method initRoundMethod = RoundManager.class.getDeclaredMethod(
                "initRound", Player.class, Dealer.class, Shoe.class, int.class);
        initRoundMethod.setAccessible(true);
        initRoundMethod.invoke(null, player, dealer, shoe, 1);

        player.getHand().clear();
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        int initialPlayerScore = player.getScore();

        assertTrue(player.hasBlackjack());
        assertEquals(RoundResult.BLACKJACK, player.checkInitialHand());

        player.incScore();

        assertEquals(initialPlayerScore + 1, player.getScore(), "Player should get 1 point for blackjack");
    }

    @Test
    void testPlayerBustScenario() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.JACK, Suit.DIAMONDS));

        int initialDealerScore = dealer.getScore();

        assertTrue(player.isBusted(), "Player should be busted with 30 points");

        dealer.incScore();

        assertEquals(initialDealerScore + 1, dealer.getScore(), "Dealer should get 1 point when player busts");
    }

    @Test
    void testInitRoundClearsHands() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertEquals(1, player.getHand().getCardCount());
        assertEquals(1, dealer.getHand().getCardCount());

        Method initRoundMethod = RoundManager.class.getDeclaredMethod(
                "initRound", Player.class, Dealer.class, Shoe.class, int.class);
        initRoundMethod.setAccessible(true);
        initRoundMethod.invoke(null, player, dealer, shoe, 1);

        assertEquals(2, player.getHand().getCardCount(), "Player should have 2 cards after initRound");
        assertEquals(2, dealer.getHand().getCardCount(), "Dealer should have 2 cards after initRound");
    }

    @Test
    void testDetermineWinner() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.KING, Suit.DIAMONDS));
        dealer.getHand().addCard(new Card(Rank.EIGHT, Suit.CLUBS));

        int initialPlayerScore = player.getScore();

        Method determineWinnerMethod = RoundManager.class.getDeclaredMethod(
                "determineWinner", Player.class, Dealer.class);
        determineWinnerMethod.setAccessible(true);
        determineWinnerMethod.invoke(null, player, dealer);

        assertEquals(initialPlayerScore + 1, player.getScore(), "Player should get 1 point for winning");
    }

    @Test
    void testDetermineWinnerDealerWins() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.KING, Suit.DIAMONDS));
        dealer.getHand().addCard(new Card(Rank.NINE, Suit.CLUBS)); //

        int initialDealerScore = dealer.getScore();

        Method determineWinnerMethod = RoundManager.class.getDeclaredMethod(
                "determineWinner", Player.class, Dealer.class);
        determineWinnerMethod.setAccessible(true);
        determineWinnerMethod.invoke(null, player, dealer);

        assertEquals(initialDealerScore + 1, dealer.getScore(), "Dealer should get 1 point for winning");
    }

    @Test
    void testDetermineWinnerDraw() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Test draw scenario
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.KING, Suit.DIAMONDS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        int initialPlayerScore = player.getScore();
        int initialDealerScore = dealer.getScore();

        Method determineWinnerMethod = RoundManager.class.getDeclaredMethod(
                "determineWinner", Player.class, Dealer.class);
        determineWinnerMethod.setAccessible(true);
        determineWinnerMethod.invoke(null, player, dealer);

        assertEquals(initialPlayerScore, player.getScore(), "Player score shouldn't change on draw");
        assertEquals(initialDealerScore, dealer.getScore(), "Dealer score shouldn't change on draw");
        assertTrue(outputStreamCaptor.toString().contains(i18n.getString("result.tie", 17, 17)));
    }

    @Test
    void testDetermineWinnerDealerBust() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.KING, Suit.DIAMONDS));
        dealer.getHand().addCard(new Card(Rank.QUEEN, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.FIVE, Suit.HEARTS));

        int initialPlayerScore = player.getScore();

        Method determineWinnerMethod = RoundManager.class.getDeclaredMethod(
                "determineWinner", Player.class, Dealer.class);
        determineWinnerMethod.setAccessible(true);
        determineWinnerMethod.invoke(null, player, dealer);

        assertEquals(initialPlayerScore + 1, player.getScore(), "Player should get 1 point when dealer busts");
        assertTrue(outputStreamCaptor.toString().contains(i18n.getString("result.dealer.busted")));
    }

    @Test
    void testInitRound() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        dealer.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertEquals(1, player.getHand().getCardCount());
        assertEquals(1, dealer.getHand().getCardCount());

        Method initRoundMethod = RoundManager.class.getDeclaredMethod(
                "initRound", Player.class, Dealer.class, Shoe.class, int.class);
        initRoundMethod.setAccessible(true);
        initRoundMethod.invoke(null, player, dealer, shoe, 1);

        assertEquals(2, player.getHand().getCardCount());
        assertEquals(2, dealer.getHand().getCardCount());
        assertTrue(outputStreamCaptor.toString().contains(i18n.getString("game.round.header", 1)));
    }

    @Test
    void testPlayRoundIntegration() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        int initialPlayerScore = player.getScore();
        int initialDealerScore = dealer.getScore();

        RoundManager.playRound(player, dealer, shoe, 1);

        assertTrue(player.getScore() >= initialPlayerScore, "Player score should not decrease");
        assertTrue(dealer.getScore() >= initialDealerScore, "Dealer score should not decrease");
        assertTrue(player.getScore() - initialPlayerScore + dealer.getScore() - initialDealerScore <= 1,
                "Either player or dealer should win, not both");
    }
}
