import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

// Test class for Game functionality
public class GameTest {

    // Tests that a Game object can be created properly
    @Test
    public void testCreationGame() {
        Scanner in = new Scanner(System.in);
        ConsoleIO io = new ConsoleIO(in);
        ErrorHandler handler = new ErrorHandler(io);
        Game game = new Game(4, io, handler);
        assertNotNull(game);
    }
}