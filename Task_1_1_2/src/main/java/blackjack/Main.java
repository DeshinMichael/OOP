package blackjack;

import blackjack.game.Game;

/**
 * Main class for the Blackjack application.
 * Entry point for the game.
 */
public class Main {
    private static final int DEFAULT_DECKS = 1;

    /**
     * Main method - entry point of the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game(DEFAULT_DECKS);
        game.start();
    }
}
