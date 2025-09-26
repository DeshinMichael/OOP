package blackjack.participants;

import blackjack.io.ConsoleInput;
import blackjack.io.ConsoleOutput;
import blackjack.util.ErrorHandler;
import blackjack.game.RoundResult;
import blackjack.deck.Shoe;
import blackjack.model.Card;

/**
 * Represents a player in the Blackjack game.
 * Inherits base functionality from Participant and adds player-specific behavior.
 */
public class Player extends Participant {
    /**
     * Decides whether to take an additional card.
     * Reads the player's choice through the console interface.
     *
     * @return true if the player decides to hit, false if stand
     */
    public boolean decidesToHit() {
        return ConsoleInput.readPlayerTurnChoice();
    }

    /**
     * Decides whether to play the next round.
     * Reads the player's choice about continuing the game through the console interface.
     *
     * @return true if the player wants to continue playing, false if quit
     */
    public boolean decidesToPlayAgain() {
        return ConsoleInput.readPlayerNextRoundChoice();
    }

    /**
     * Checks if the player has exactly 21 points.
     *
     * @return true if the player's hand value equals 21, false otherwise
     */
    public boolean has21() {
        return getHand().getValue() == 21;
    }

    /**
     * Executes the player's turn and returns the result.
     * Manages the player's decision-making process about taking cards,
     * handles the results of each action.
     *
     * @param shoe the shoe to deal cards from
     * @param dealer the dealer for displaying their cards
     * @return the turn result (BUST, GOT_21, STAND)
     */
    public RoundResult makeMove(Shoe shoe, Dealer dealer) {
        ConsoleOutput.printPlayerTurn();
        ConsoleOutput.printHitOrStand();

        while (true) {
            boolean choice = decidesToHit();
            if (choice) {
                ErrorHandler.addCardSafety(getHand(), ErrorHandler.dealCardSafety(shoe));
                Card drawnCard = ErrorHandler.getLastCardSafety(getHand());
                ConsoleOutput.printPlayerDrewCard(drawnCard);
                ConsoleOutput.printPlayerCards(getHand());
                ConsoleOutput.printDealerClosedCards(dealer.getHand());

                if (isBusted()) {
                    ConsoleOutput.printPlayerBust();
                    return RoundResult.BUST;
                } else if (has21()) {
                    ConsoleOutput.printPlayer21();
                    return RoundResult.GOT_21;
                } else {
                    ConsoleOutput.printHitOrStand();
                }
            } else {
                return RoundResult.STAND;
            }
        }
    }

    /**
     * Checks the initial hand for blackjack.
     * Determines if the player has natural blackjack (21 points with two cards).
     *
     * @return BLACKJACK if the player has blackjack, CONTINUE if the game continues
     */
    public RoundResult checkInitialHand() {
        if (hasBlackjack()) {
            ConsoleOutput.printPlayerHasBlackjack();
            return RoundResult.BLACKJACK;
        }
        return RoundResult.CONTINUE;
    }
}