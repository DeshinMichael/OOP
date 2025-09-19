/**
 * Handles error conditions in the Blackjack game.
 * Provides safe operations for card-related actions that might fail.
 */
public class ErrorHandler {
    private final ConsoleIO io;

    /**
     * Creates a new ErrorHandler with the specified ConsoleIO.
     *
     * @param io The ConsoleIO instance for displaying error messages
     */
    public ErrorHandler(ConsoleIO io) {
        this.io = io;
    }

    /**
     * Safely adds a card to a hand, handling any errors.
     * If the hand is full, displays an error and exits the program.
     *
     * @param hand The hand to add a card to
     * @param card The card to add
     */
    public void addCardSafety(Hand hand, Card card) {
        try {
            hand.addCard(card);
        } catch (IllegalStateException e) {
            io.printErrorHandFull();
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
    public Card dealCardSafety(Shoe shoe) {
        try {
            return shoe.dealCard();
        } catch (IllegalStateException e) {
            io.printErrorShoeOutOfCards();
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
    public Card getCardSafety(Hand hand, int index) {
        try {
            return hand.getCard(index);
        } catch (IllegalArgumentException e) {
            io.printErrorInvalidCardIndex();
            System.exit(1);
            return null;
        }
    }
}