import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

// Test class for ErrorHandler functionality
public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    // Setup method that runs before each test
    @BeforeEach
    public void setUp() {
        Scanner in = new Scanner(System.in);
        ConsoleIO consoleIO = new ConsoleIO(in);
        errorHandler = new ErrorHandler(consoleIO);
    }

    // Tests that cards can be added safely to a hand under normal conditions
    @Test
    public void testAddCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        errorHandler.addCardSafety(hand, card);
        assertEquals(card, hand.getCard(0));
    }

    // Tests that cards can be dealt safely from a shoe under normal conditions
    @Test
    public void testDealCardSafetyNormal() {
        Shoe shoe = new Shoe(1);
        Card card = errorHandler.dealCardSafety(shoe);
        assertNotNull(card);
    }

    // Tests that cards can be retrieved safely from a hand under normal conditions
    @Test
    public void testGetCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        hand.addCard(card);
        Card result = errorHandler.getCardSafety(hand, 0);
        assertEquals(card, result);
    }
}