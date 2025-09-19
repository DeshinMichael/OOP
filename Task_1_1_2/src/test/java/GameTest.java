import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * Test class for Game functionality.
 * Validates that the Game class can be properly instantiated.
 */
public class GameTest {

    /**
     * Tests that a Game object can be created properly.
     * Verifies the Game instance is not null after creation.
     */
    @Test
    public void testCreationGame() {
        Scanner in = new Scanner(System.in);
        ConsoleIO io = new ConsoleIO(in);
        ErrorHandler handler = new ErrorHandler(io);
        Game game = new Game(4, io, handler);
        assertNotNull(game);
    }
}