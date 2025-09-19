// Class for handling error situations in the game
public class ErrorHandler {
    private final ConsoleIO io;

    // Constructor that initializes with ConsoleIO for error reporting
    public ErrorHandler(ConsoleIO io) {
        this.io = io;
    }

    // Safely adds card to hand, handling any errors that occur
    public void addCardSafety(Hand hand, Card card) {
        try {
            hand.addCard(card);
        } catch (IllegalStateException e) {
            io.printErrorHandFull();
            System.exit(1);
        }
    }

    // Safely deals card from shoe, handling empty shoe error
    public Card dealCardSafety(Shoe shoe) {
        try {
            return shoe.dealCard();
        } catch (IllegalStateException e) {
            io.printErrorShoeOutOfCards();
            System.exit(1);
            return null;
        }
    }

    // Safely retrieves card from hand by index, handling invalid index errors
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