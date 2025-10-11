package blackjack.game;

import blackjack.deck.Shoe;
import blackjack.io.ConsoleOutput;
import blackjack.participants.Dealer;
import blackjack.participants.Player;

/**
 * Main class for the blackjack game.
 * Manages the overall game process, including creating participants, deck, and conducting rounds.
 */
public class Game {
    private final Player player;
    private final Dealer dealer;
    private final Shoe shoe;
    private int roundNumber;

    /**
     * Creates a new game with the specified number of decks.
     *
     * @param numDecks the number of decks to use in the game
     */
    public Game(int numDecks) {
        this.player = new Player();
        this.dealer = new Dealer();
        this.shoe = new Shoe(numDecks);
        this.roundNumber = 1;
    }

    /**
     * Starts the main game loop.
     * Conducts rounds until the player decides to stop.
     */
    public void start() {
        ConsoleOutput.printHelloMessage();

        do {
            RoundManager.playRound(player, dealer, shoe, roundNumber);
            printScore();
            roundNumber++;
        } while (player.decidesToPlayAgain());

        printFinalResults();
    }

    /**
     * Prints the current game score.
     */
    private void printScore() {
        ConsoleOutput.printScore(player.getScore(), dealer.getScore());
    }

    /**
     * Prints the final game results.
     */
    private void printFinalResults() {
        ConsoleOutput.printFinalResults(player.getScore(), dealer.getScore());
    }
}
