package blackjack_test.util_test;

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
     * Tests that an error is handled correctly when trying to add a card to a full hand.
     */
    @Test
    void testAddCardToFullHandWouldCauseError() {
        for (int i = 0; i < 12; i++) {
            hand.addCard(new Card(Rank.TWO, Suit.HEARTS));
        }

        assertEquals(12, hand.getCardCount());

        assertThrows(IllegalStateException.class, () -> {
            hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        });
    }

    /**
     * Tests that an error is handled correctly when trying to deal a card from an empty shoe.
     */
    @Test
    void testDealFromEmptyShoeWouldCauseError() {
        for (int i = 0; i < 52; i++) {
            shoe.dealCard();
        }

        assertThrows(IllegalStateException.class, () -> {
            shoe.dealCard();
        });
    }

    /**
     * Tests that an error is handled correctly when trying to get a card with an invalid index.
     */
    @Test
    void testGetCardWithInvalidIndexWouldCauseError() {
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));

        assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            hand.getCard(1);
        });
    }

    /**
     * Tests that an error is handled correctly when trying to get the last card from an empty hand.
     */
    @Test
    void testGetLastCardFromEmptyHandWouldCauseError() {
        assertEquals(0, hand.getCardCount());

        assertThrows(IllegalArgumentException.class, () -> {
            hand.getLastCard();
        });
    }
}