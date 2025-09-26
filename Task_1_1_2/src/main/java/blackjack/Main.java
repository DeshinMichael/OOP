package blackjack;

import blackjack.game.Game;

public class Main {
    private static final int DEFAULT_DECKS = 1;

    public static void main(String[] args) {
        Game game = new Game(DEFAULT_DECKS);
        game.start();
    }
}
