import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        Scanner in = new Scanner(System.in);
        ConsoleIO consoleIO = new ConsoleIO(in);
        errorHandler = new ErrorHandler(consoleIO);
    }

    @Test
    public void testAddCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        errorHandler.addCardSafety(hand, card);
        assertEquals(card, hand.getCard(0));
    }

    @Test
    public void testDealCardSafetyNormal() {
        Shoe shoe = new Shoe(1);
        Card card = errorHandler.dealCardSafety(shoe);
        assertNotNull(card);
    }

    @Test
    public void testGetCardSafetyNormal() {
        Hand hand = new Hand();
        Card card = new Card("Туз", "Пики");
        hand.addCard(card);
        Card result = errorHandler.getCardSafety(hand, 0);
        assertEquals(card, result);
    }
}