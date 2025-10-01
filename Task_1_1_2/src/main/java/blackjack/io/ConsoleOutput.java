package blackjack.io;

import blackjack.i18n.I18nManager;
import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.util.ErrorHandler;

/**
 * Class for output through console.
 * Provides output functionality for the blackjack game.
 */
public class ConsoleOutput {
    private static final I18nManager i18n = I18nManager.getInstance();

    /**
     * Prints the game welcome message.
     */
    public static void printHelloMessage() {
        System.out.println(i18n.getString("game.welcome"));
    }

    /**
     * Prints the round header with its number.
     *
     * @param roundNumber the current round number
     */
    public static void printRoundHeader(int roundNumber) {
        System.out.println(i18n.getString("game.round.header", roundNumber));
    }

    /**
     * Prints a message that the dealer dealt cards.
     */
    public static void printDealerDealtCards() {
        System.out.println(i18n.getString("game.dealer.dealt"));
    }

    /**
     * Prints the player's cards and their total sum.
     *
     * @param hand the player's hand
     */
    public static void printPlayerCards(Hand hand) {
        System.out.println(i18n.getString("hand.player", hand.toString(), hand.getValue()));
    }

    /**
     * Prints the dealer's cards and their total sum.
     *
     * @param hand the dealer's hand
     */
    public static void printDealerCards(Hand hand) {
        System.out.println(i18n.getString("hand.dealer", hand.toString(), hand.getValue()));
    }

    /**
     * Prints the dealer's cards with one hidden card.
     *
     * @param hand the dealer's hand
     */
    public static void printDealerClosedCards(Hand hand) {
        System.out.println(i18n.getString("hand.dealer.closed",
            ErrorHandler.getCardSafety(hand, 0).toString(),
                ErrorHandler.getCardSafety(hand, 0).getValue()));
    }

    /**
     * Prints a message about player's blackjack.
     */
    public static void printPlayerHasBlackjack() {
        System.out.println(i18n.getString("blackjack.player"));
    }

    /**
     * Prints a message about dealer's blackjack.
     */
    public static void printDealerHasBlackjack() {
        System.out.println(i18n.getString("blackjack.dealer"));
    }

    /**
     * Prints the player's turn header.
     */
    public static void printPlayerTurn() {
        System.out.println(i18n.getString("turn.player"));
    }

    /**
     * Prints the dealer's turn header.
     */
    public static void printDealerTurn() {
        System.out.println(i18n.getString("turn.dealer"));
    }

    /**
     * Prints a prompt for the player's decision to hit or stand.
     */
    public static void printHitOrStand() {
        System.out.println(i18n.getString("game.hit.or.stand"));
    }

    /**
     * Prints a message that the player drew a card.
     *
     * @param card the drawn card
     */
    public static void printPlayerDrewCard(Card card) {
        System.out.println(i18n.getString("player.drew.card", card.toString()));
    }

    /**
     * Prints a message that the dealer drew a card.
     *
     * @param card the drawn card
     */
    public static void printDealerDrewCard(Card card) {
        System.out.println(i18n.getString("dealer.drew.card", card.toString()));
    }

    /**
     * Prints a message that the dealer reveals the hidden card.
     */
    public static void printDealerDrewClosedCard() {
        System.out.println(i18n.getString("dealer.drew.closed"));
    }

    /**
     * Prints a message about player's bust.
     */
    public static void printPlayerBust() {
        System.out.println(i18n.getString("result.player.busted"));
    }

    /**
     * Prints a message that the player got 21.
     */
    public static void printPlayer21() {
        System.out.println(i18n.getString("player.21"));
    }

    /**
     * Prints an error message about hand overflow.
     */
    public static void printErrorHandFull() {
        System.out.println(i18n.getString("error.hand.full"));
    }

    /**
     * Prints an error message about running out of cards in the shoe.
     */
    public static void printErrorShoeOutOfCards() {
        System.out.println(i18n.getString("error.shoe.empty"));
    }

    /**
     * Prints an error message about invalid card index.
     */
    public static void printErrorInvalidCardIndex() {
        System.out.println(i18n.getString("error.invalid.card.index"));
    }

    /**
     * Prints the current game score.
     *
     * @param playerScore the player's score
     * @param dealerScore the dealer's score
     */
    public static void printScore(int playerScore, int dealerScore) {
        System.out.println(i18n.getString("score.current", playerScore, dealerScore));
    }

    /**
     * Prints the final game results.
     *
     * @param playerScore the player's final score
     * @param dealerScore the dealer's final score
     */
    public static void printFinalResults(int playerScore, int dealerScore) {
        System.out.println(i18n.getString("score.final", playerScore, dealerScore));

        if (playerScore > dealerScore) {
            System.out.println(i18n.getString("final.player.wins"));
        } else if (dealerScore > playerScore) {
            System.out.println(i18n.getString("final.dealer.wins"));
        } else {
            System.out.println(i18n.getString("final.tie"));
        }

        System.out.println(i18n.getString("final.thanks"));
    }

    /**
     * Prints a message that dealer is busted.
     */
    public static void printDealerBust() {
        System.out.println(i18n.getString("result.dealer.busted"));
    }

    /**
     * Prints a message that player won with comparison of values.
     *
     * @param playerValue player's hand value
     * @param dealerValue dealer's hand value
     */
    public static void printPlayerWins(int playerValue, int dealerValue) {
        System.out.println(i18n.getString("result.player.wins", playerValue, dealerValue));
    }

    /**
     * Prints a message that dealer won with comparison of values.
     *
     * @param dealerValue dealer's hand value
     * @param playerValue player's hand value
     */
    public static void printDealerWins(int dealerValue, int playerValue) {
        System.out.println(i18n.getString("result.dealer.wins", dealerValue, playerValue));
    }

    /**
     * Prints a message about draw game.
     *
     * @param value the value of both hands
     */
    public static void printDraw(int value) {
        System.out.println(i18n.getString("result.tie", value, value));
    }
}
