package blackjack.game;

import blackjack.deck.Shoe;
import blackjack.io.ConsoleOutput;
import blackjack.participants.Dealer;
import blackjack.participants.Player;

/**
 * Manages the logic of a single blackjack game round.
 * Coordinates player and dealer actions, determines the round winner.
 */
public class RoundManager {

    /**
     * Plays a complete round of the game between player and dealer.
     * Manages the entire process from dealing cards to determining the winner.
     *
     * @param player the player
     * @param dealer the dealer
     * @param shoe the card shoe
     * @param roundNumber the current round number
     */
    public static void playRound(Player player, Dealer dealer, Shoe shoe, int roundNumber) {
        initRound(player, dealer, shoe, roundNumber);

        RoundResult initPlayerResult = player.checkInitialHand();
        if (initPlayerResult == RoundResult.BLACKJACK) {
            player.incScore();
            return;
        }

        RoundResult playerResult = player.makeMove(shoe, dealer);
        if (playerResult == RoundResult.BUST) {
            dealer.incScore();
            return;
        }

        RoundResult initDealerResult = dealer.checkInitialHand();
        if (initDealerResult == RoundResult.BLACKJACK) {
            dealer.incScore();
            return;
        }

        RoundResult dealerResult = dealer.makeMove(shoe, dealer);
        if (dealerResult == RoundResult.BUST) {
            player.incScore();
            return;
        }

        determineWinner(player, dealer);
    }

    private static void initRound(Player player, Dealer dealer, Shoe shoe, int roundNumber) {
        ConsoleOutput.printRoundHeader(roundNumber);
        player.getHand().clear();
        dealer.getHand().clear();
        dealer.dealInitialCards(player, dealer, shoe);
        ConsoleOutput.printDealerDealtCards();
        ConsoleOutput.printPlayerCards(player.getHand());
        ConsoleOutput.printDealerClosedCards(dealer.getHand());
    }

    /**
     * Determines the round winner by comparing card sums.
     *
     * @param player the player
     * @param dealer the dealer
     */
    private static void determineWinner(Player player, Dealer dealer) {
        int playerValue = player.getHand().getValue();
        int dealerValue = dealer.getHand().getValue();

        if (dealer.isBusted()) {
            ConsoleOutput.printDealerBust();
            player.incScore();
        } else if (playerValue > dealerValue) {
            ConsoleOutput.printPlayerWins(playerValue, dealerValue);
            player.incScore();
        } else if (dealerValue > playerValue) {
            ConsoleOutput.printDealerWins(dealerValue, playerValue);
            dealer.incScore();
        } else {
            ConsoleOutput.printDraw(playerValue);
        }
    }
}
