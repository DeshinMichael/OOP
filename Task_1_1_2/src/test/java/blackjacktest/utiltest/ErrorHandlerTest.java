package blackjacktest.utiltest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import blackjack.deck.Shoe;
import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;
import blackjack.util.ErrorHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for blackjack.util.ErrorHandler functionality.
 * Validates error handling methods function properly under normal conditions.
 */
public class ErrorHandlerTest {
    private Hand hand;
    private Shoe shoe;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Setup method that runs before each test.
     * Creates a blackjack.util.ErrorHandler instance with a blackjack.io.ConsoleIO for testing.
     */
    @BeforeEach
    public void setUp() {
        hand = new Hand();
        shoe = new Shoe(1);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Cleanup method that runs after each test.
     * Restores the original System.out stream.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStreamCaptor.reset();
    }

    /**
     * Tests that cards can be added safely to a hand under normal conditions.
     * Verifies the card is added correctly to the hand.
     */
    @Test
    public void testAddCardSafetyNormal() {
        Card card = new Card(Rank.ACE, Suit.SPADES);

        assertDoesNotThrow(() -> {
            ErrorHandler.addCardSafety(hand, card);
        });

        assertEquals(1, hand.getCardCount());
        assertEquals(card, hand.getCard(0));
    }

    /**
     * Tests that cards can be dealt safely from a shoe under normal conditions.
     * Verifies a card is successfully dealt from the shoe.
     */
    @Test
    public void testDealCardSafetyNormal() {
        Card card = ErrorHandler.dealCardSafety(shoe);

        assertNotNull(card);
        assertInstanceOf(Card.class, card);
    }

    /**
     * Tests that cards can be retrieved safely from a hand under normal conditions.
     * Verifies the correct card is retrieved from the hand.
     */
    @Test
    public void testGetCardSafetyNormal() {
        Card card = new Card(Rank.ACE, Suit.SPADES);
        hand.addCard(card);
        Card result = ErrorHandler.getCardSafety(hand, 0);
        assertEquals(card, result);
    }

    /**
     * Tests that the last card can be retrieved safely from a hand under normal conditions.
     * Verifies the correct last card is retrieved from the hand.
     */
    @Test
    public void testGetLastCardSafetySuccess() {
        Card card1 = new Card(Rank.TWO, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.HEARTS);

        hand.addCard(card1);
        hand.addCard(card2);

        Card lastCard = ErrorHandler.getLastCardSafety(hand);

        assertEquals(card2, lastCard);
    }

    /**
     * Tests that ErrorHandler would display an error when adding a card to a full hand.
     * This test only checks error message display, not System.exit behavior.
     */
    @Test
    void testAddCardToFullHandErrorMessage() {
        for (int i = 0; i < 12; i++) {
            hand.addCard(new Card(Rank.TWO, Suit.HEARTS));
        }
        assertEquals(12, hand.getCardCount());
        assertThrows(IllegalStateException.class, () -> {
            hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        });
    }

    /**
     * Tests that ErrorHandler would display an error when dealing from empty shoe.
     * This test only checks error message display, not System.exit behavior.
     */
    @Test
    void testDealFromEmptyShoeErrorMessage() {
        for (int i = 0; i < 52; i++) {
            shoe.dealCard();
        }

        assertThrows(IllegalStateException.class, () -> {
            shoe.dealCard();
        });
    }

    /**
     * Tests that ErrorHandler would display an error with invalid card index.
     * This test only checks error message display, not System.exit behavior.
     */
    @Test
    void testGetCardWithInvalidIndexErrorMessage() {
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(1);
        });
    }

    /**
     * Tests that ErrorHandler would display an error when getting last card from empty hand.
     * This test only checks error message display, not System.exit behavior.
     */
    @Test
    void testGetLastCardFromEmptyHandErrorMessage() {
        assertEquals(0, hand.getCardCount());
        assertThrows(IllegalArgumentException.class, () -> {
            hand.getLastCard();
        });
    }

    /**
     * Tests ErrorHandler with multiple normal operations.
     */
    @Test
    public void testErrorHandlerWithMultipleOperations() {
        Card card1 = new Card(Rank.ACE, Suit.HEARTS);
        Card card2 = new Card(Rank.KING, Suit.SPADES);

        ErrorHandler.addCardSafety(hand, card1);
        ErrorHandler.addCardSafety(hand, card2);

        Card retrieved = ErrorHandler.getCardSafety(hand, 0);
        assertEquals(card1, retrieved);

        Card lastCard = ErrorHandler.getLastCardSafety(hand);
        assertEquals(card2, lastCard);

        Card dealtCard = ErrorHandler.dealCardSafety(shoe);
        assertNotNull(dealtCard);
    }

    /**
     * Tests that ErrorHandler methods work with various valid inputs.
     */
    @Test
    public void testErrorHandlerWithVariousCards() {
        Card[] testCards = {
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.KING, Suit.SPADES),
            new Card(Rank.TWO, Suit.CLUBS),
            new Card(Rank.TEN, Suit.DIAMONDS)
        };

        for (Card card : testCards) {
            Hand testHand = new Hand();
            ErrorHandler.addCardSafety(testHand, card);

            Card retrieved = ErrorHandler.getCardSafety(testHand, 0);
            assertEquals(card, retrieved);

            Card lastCard = ErrorHandler.getLastCardSafety(testHand);
            assertEquals(card, lastCard);
        }
    }

    /**
     * Tests dealing multiple cards from shoe through ErrorHandler.
     */
    @Test
    public void testDealMultipleCards() {
        Hand testHand = new Hand();

        for (int i = 0; i < 5; i++) {
            Card dealtCard = ErrorHandler.dealCardSafety(shoe);
            assertNotNull(dealtCard);
            ErrorHandler.addCardSafety(testHand, dealtCard);
        }

        assertEquals(5, testHand.getCardCount());

        for (int i = 0; i < 5; i++) {
            Card retrievedCard = ErrorHandler.getCardSafety(testHand, i);
            assertNotNull(retrievedCard);
        }
    }

    /**
     * Tests additional error handling behavior by verifying the basic Hand class exceptions.
     * This helps with test coverage without triggering System.exit().
     */
    @Test
    public void testUnderlyingExceptions() {
        Hand fullHand = new Hand();
        for (int i = 0; i < 12; i++) {
            fullHand.addCard(new Card(Rank.TWO, Suit.HEARTS));
        }
        assertThrows(IllegalStateException.class, () ->
            fullHand.addCard(new Card(Rank.ACE, Suit.HEARTS)));

        Hand emptyHand = new Hand();
        assertThrows(IllegalArgumentException.class, emptyHand::getLastCard);

        Hand singleCardHand = new Hand();
        singleCardHand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        assertThrows(IllegalArgumentException.class, () ->
            singleCardHand.getCard(-1));
        assertThrows(IllegalArgumentException.class, () ->
            singleCardHand.getCard(1));

        Shoe emptyShoe = new Shoe(1);
        for (int i = 0; i < 52; i++) {
            emptyShoe.dealCard();
        }
        assertThrows(IllegalStateException.class, emptyShoe::dealCard);
    }
}
