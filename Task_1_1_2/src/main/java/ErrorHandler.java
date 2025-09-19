public class ErrorHandler {
    private final ConsoleIO io;

    public ErrorHandler(ConsoleIO io) {
        this.io = io;
    }

    public void addCardSafety(Hand hand, Card card) {
        try {
            hand.addCard(card);
        } catch (IllegalStateException e) {
            io.printErrorHandFull();
            System.exit(1);
        }
    }

    public Card dealCardSafety(Shoe shoe) {
        try {
            return shoe.dealCard();
        } catch (IllegalStateException e) {
            io.printErrorShoeOutOfCards();
            System.exit(1);
            return null;
        }
    }

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