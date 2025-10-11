package blackjacktest.participantstest;

import blackjack.deck.Shoe;
import blackjack.game.RoundResult;
import blackjack.io.ConsoleInput;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.participants.Dealer;
import blackjack.participants.Participant;
import blackjack.participants.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for blackjack.participants.Player functionality.
 * Tests player-specific behavior, hand evaluation, and inheritance from Participant.
 */
class PlayerTest {
    private Player player;
    private Shoe shoe;
    private Dealer dealer;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    /**
     * Setup method that runs before each test.
     * Creates a new Player instance for testing.
     */
    @BeforeEach
    void setUp() {
        player = new Player();
        shoe = new Shoe(1);
        dealer = new Dealer();
        System.setOut(new PrintStream(outputStreamCaptor));
        ConsoleInput.closeScanner();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testDecidesToHitTrue() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertTrue(player.decidesToHit());
    }

    @Test
    void testDecidesToHitFalse() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertFalse(player.decidesToHit());
    }

    @Test
    void testDecidesToPlayAgainTrue() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertTrue(player.decidesToPlayAgain());
    }

    @Test
    void testDecidesToPlayAgainFalse() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertFalse(player.decidesToPlayAgain());
    }

    @Test
    void testHas21True() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        assertTrue(player.has21());
    }

    @Test
    void testHas21False() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        assertFalse(player.has21());
    }

    @Test
    void testHas21EmptyHand() {
        assertFalse(player.has21());
    }

    @Test
    void testCheckInitialHandBlackjack() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.KING, Suit.SPADES));

        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.BLACKJACK, result);
    }

    @Test
    void testCheckInitialHandContinue() {
        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.CONTINUE, result);
    }

    @Test
    void testCheckInitialHandEmptyHand() {
        RoundResult result = player.checkInitialHand();
        assertEquals(RoundResult.CONTINUE, result);
    }

    @Test
    void testMakeMoveWithStand() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        assertEquals(RoundResult.CONTINUE, result);
        assertEquals(2, player.getHand().getCardCount());
    }

    @Test
    void testMakeMoveWithHit() {
        String input = "1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.FIVE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SIX, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.TEN, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.NINE, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        assertTrue(player.getHand().getCardCount() >= 3);
        assertTrue(result == RoundResult.CONTINUE || result == RoundResult.BUST);
    }

    @Test
    void testMakeMoveWithBust() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.KING, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.QUEEN, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        if (player.isBusted()) {
            assertEquals(RoundResult.BUST, result);
        }
    }

    @Test
    void testMakeMoveMultipleHits() {
        String input = "1\n1\n1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.TWO, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.THREE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.TEN, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.NINE, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        assertTrue(player.getHand().getCardCount() >= 3);
    }

    @Test
    void testMakeMoveHitThenStand() {
        String input = "1\n1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.FOUR, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.FIVE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.TEN, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.NINE, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        assertTrue(result == RoundResult.CONTINUE || result == RoundResult.BUST);
        assertTrue(player.getHand().getCardCount() >= 3);
    }

    @Test
    void testHas21WithAceAdjustment() {
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.ACE, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.NINE, Suit.CLUBS));

        assertTrue(player.has21());
    }

    @Test
    void testHas21WithThreeCards() {
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        assertTrue(player.has21());
        assertFalse(player.hasBlackjack());
    }

    @Test
    void testCheckInitialHandWithVariousHands() {
        player.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.TEN, Suit.SPADES));

        assertEquals(RoundResult.CONTINUE, player.checkInitialHand());

        player.getHand().clear();
        player.getHand().addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));

        assertEquals(RoundResult.CONTINUE, player.checkInitialHand());
    }

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

    @Test
    void testPlayerSpecificLogic() {
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.SPADES));
        player.getHand().addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        assertTrue(player.has21());
        assertFalse(player.hasBlackjack());
    }

    @Test
    void testMakeMoveWithInvalidThenValidInput() {
        String input = "invalid\n2\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        player.getHand().addCard(new Card(Rank.TEN, Suit.HEARTS));
        player.getHand().addCard(new Card(Rank.NINE, Suit.SPADES));

        dealer.getHand().addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        dealer.getHand().addCard(new Card(Rank.SEVEN, Suit.DIAMONDS));

        RoundResult result = player.makeMove(shoe, dealer);

        assertEquals(RoundResult.CONTINUE, result);
    }
}
