package blackjack.game;

import blackjack.deck.Shoe;
import blackjack.io.ConsoleOutput;
import blackjack.participants.Player;
import blackjack.participants.Dealer;
import blackjack.util.ErrorHandler;

/**
 * Manages the logic of a single blackjack game round.
 * Coordinates player and dealer actions, determines the round winner.
 */
public class RoundManager {

    /**
     * Creates a new round manager.
     */
    public RoundManager() {
    }

    /**
     * Plays a complete round of the game between player and dealer.
     * Manages the entire process from dealing cards to determining the winner.
     *
     * @param player the player
     * @param dealer the dealer
     * @param shoe the card shoe
     * @param roundNumber the current round number
     */
    public void playRound(Player player, Dealer dealer, Shoe shoe, int roundNumber) {
        ConsoleOutput.printRoundHeader(roundNumber);

        player.getHand().clear();
        dealer.getHand().clear();

        dealInitialCards(player, dealer, shoe);

        ConsoleOutput.printDealerDealtCards();
        ConsoleOutput.printPlayerCards(player.getHand());
        ConsoleOutput.printDealerClosedCards(dealer.getHand());

        RoundResult initResult = player.checkInitialHand();
        if (initResult == RoundResult.BLACKJACK) {
            return;
        }

        RoundResult playerResult = player.makeMove(shoe, dealer);

        if (playerResult == RoundResult.BUST) {
            dealer.incScore();
            return;
        }

        if (dealer.hasBlackjack()) {
            ConsoleOutput.printDealerHasBlackjack();
            return;
        }

        playDealerTurn(dealer, shoe);

        determineWinner(player, dealer);
    }

    /**
     * Deals initial cards to player and dealer.
     * Each participant receives two cards.
     *
     * @param player the player
     * @param dealer the dealer
     * @param shoe the card shoe
     */
    private void dealInitialCards(Player player, Dealer dealer, Shoe shoe) {
        ErrorHandler.addCardSafety(player.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(player.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
    }

    /**
     * Checks initial hands for blackjack.
     *
     * @param player the player
     * @return true if the round is over due to blackjack, false if the game continues
     */
    private boolean checkInitialCards(Player player) {
        boolean playerBlackjack = player.hasBlackjack();

        if (playerBlackjack) {
            ConsoleOutput.printPlayerHasBlackjack();
            player.incScore();
            return true;
        }

        return false;
    }

    /**
     * Plays the dealer's turn according to blackjack rules.
     * The dealer hits until the sum reaches 17 or more.
     *
     * @param dealer the dealer
     * @param shoe the card shoe
     */
    private void playDealerTurn(Dealer dealer, Shoe shoe) {
        ConsoleOutput.printDealerTurn();
        ConsoleOutput.printDealerDrewClosedCard();
        ConsoleOutput.printDealerCards(dealer.getHand());

        while (dealer.shouldHit()) {
            ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
            ConsoleOutput.printDealerDrewCard(ErrorHandler.getLastCardSafety(dealer.getHand()));
            ConsoleOutput.printDealerCards(dealer.getHand());
        }
    }

    /**
     * Determines the round winner by comparing card sums.
     *
     * @param player the player
     * @param dealer the dealer
     */
    private void determineWinner(Player player, Dealer dealer) {
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
