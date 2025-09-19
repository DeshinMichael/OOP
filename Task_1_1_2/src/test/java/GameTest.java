import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class GameTest {

    @Test
    public void testCreationGame() {
        Scanner in = new Scanner(System.in);
        ConsoleIO io = new ConsoleIO(in);
        ErrorHandler handler = new ErrorHandler(io);
        Game game = new Game(4, io, handler);
        assertNotNull(game);
    }
}