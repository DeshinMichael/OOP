package blackjack.util;

import blackjack.deck.Shoe;
import blackjack.io.ConsoleOutput;
import blackjack.model.Card;
import blackjack.model.Hand;

/**
 * Handles error conditions in the Blackjack game.
 * Provides safe operations for card-related actions that might fail.
 */
public class ErrorHandler {

    /**
     * Safely adds a card to a hand, handling any errors.
     * If the hand is full, displays an error and exits the program.
     *
     * @param hand The hand to add a card to
     * @param card The card to add
     */
    public static void addCardSafety(Hand hand, Card card) {
        try {
            hand.addCard(card);
        } catch (IllegalStateException e) {
            ConsoleOutput.printErrorHandFull();
            System.exit(1);
        }
    }

    /**
     * Safely deals a card from the shoe, handling any errors.
     * If the shoe is empty, displays an error and exits the program.
     *
     * @param shoe The shoe to deal from
     * @return The dealt card
     */
    public static Card dealCardSafety(Shoe shoe) {
        try {
            return shoe.dealCard();
        } catch (IllegalStateException e) {
            ConsoleOutput.printErrorShoeOutOfCards();
            System.exit(1);
            return null;
        }
    }

    /**
     * Safely gets a card from a hand by index, handling any errors.
     * If the index is invalid, displays an error and exits the program.
     *
     * @param hand The hand to get a card from
     * @param index The index of the card
     * @return The card at the specified index
     */
    public static Card getCardSafety(Hand hand, int index) {
        try {
            return hand.getCard(index);
        } catch (IllegalArgumentException e) {
            ConsoleOutput.printErrorInvalidCardIndex();
            System.exit(1);
            return null;
        }
    }

    /**
     * Safely gets the last card from a hand, handling any errors.
     * If the hand is empty, displays an error and exits the program.
     *
     * @param hand The hand to get the last card from
     * @return The last card in the hand
     */
    public static Card getLastCardSafety(Hand hand) {
        try {
            return hand.getLastCard();
        } catch (IllegalStateException e) {
            ConsoleOutput.printErrorShoeOutOfCards();
            System.exit(1);
            return null;
        }
    }
}