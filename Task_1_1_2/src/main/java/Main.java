import java.util.Scanner;

/**
 * Main entry point for the Blackjack game application.
 * Sets up required components and starts the game.
 */
public class Main {
    /**
     * Main method that initializes the game and starts it.
     * Creates required objects and passes them to the Game instance.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ConsoleIO io = new ConsoleIO(in);
        ErrorHandler handler = new ErrorHandler(io);
        Game game = new Game(4, io, handler);
        game.startGame();
    }
}