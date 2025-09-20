import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ErrorHandler functionality.
 * Validates error handling methods function properly under normal conditions.
 */
public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    /**
     * Setup method that runs before each test.
     * Creates an ErrorHandler instance with a ConsoleIO for testing.
     */
    @BeforeEach
    public void setUp() {
        Scanner in = new Scanner(System.in);
        ConsoleIO consoleIo = new ConsoleIO(in);
        errorHandler = new ErrorHandler(consoleIo);
    }

    /**
     * Tests that cards can be added safely to a hand under normal conditions.
     * Verifies the card is added correctly to the hand.
     */
    @Test
    public void testAddCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        errorHandler.addCardSafety(hand, card);
        assertEquals(card, hand.getCard(0));
    }

    /**
     * Tests that cards can be dealt safely from a shoe under normal conditions.
     * Verifies a card is successfully dealt from the shoe.
     */
    @Test
    public void testDealCardSafetyNormal() {
        Shoe shoe = new Shoe(1);
        Card card = errorHandler.dealCardSafety(shoe);
        assertNotNull(card);
    }

    /**
     * Tests that cards can be retrieved safely from a hand under normal conditions.
     * Verifies the correct card is retrieved from the hand.
     */
    @Test
    public void testGetCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        hand.addCard(card);
        Card result = errorHandler.getCardSafety(hand, 0);
        assertEquals(card, result);
    }
}