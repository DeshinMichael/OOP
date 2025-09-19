/**
 * Main game class that controls the flow of the Blackjack game.
 * Manages rounds, players, and game logic.
 */
public class Game {
    private final int numDecks;
    private final Participant player;
    private final Participant dealer;
    private static int roundCount;
    private final ConsoleIO io;
    private final ErrorHandler handler;

    /**
     * Creates a new Blackjack game.
     *
     * @param numDecks The number of card decks to use
     * @param io The ConsoleIO instance for user interaction
     * @param handler The ErrorHandler for safe operations
     */
    public Game(int numDecks, ConsoleIO io, ErrorHandler handler) {
        this.numDecks = numDecks;
        player = new Participant();
        dealer = new Participant();
        roundCount = 1;
        this.io = io;
        this.handler = handler;
    }

    /**
     * Starts the game by displaying a welcome message and playing the first round.
     */
    public void startGame() {
        io.printHelloMessage();
        playRound();
    }

    /**
     * Plays a single round of Blackjack.
     * Handles dealing cards, player and dealer turns, and determining the winner.
     */
    private void playRound() {
        Shoe shoe = new Shoe(numDecks);
        player.resetHand();
        dealer.resetHand();

        Hand playerHand = player.getHand();
        Hand dealerHand = dealer.getHand();

        handler.addCardSafety(playerHand, handler.dealCardSafety(shoe));
        handler.addCardSafety(dealerHand, handler.dealCardSafety(shoe));
        handler.addCardSafety(playerHand, handler.dealCardSafety(shoe));
        handler.addCardSafety(dealerHand, handler.dealCardSafety(shoe));

        io.printRoundHeader(roundCount);
        io.printDealerDealtCards();
        io.printPlayerAndDealerCards(player, dealer, false);

        if (isBlackjack(player)) {
            io.printPlayerHasBlackjack();
            player.incScore();
            newRound();
        }

        io.printPlayerTurn();
        io.printHitOrStand();

        while (true) {
            boolean playerChoice = io.readPlayerTurnChoice();
            if (playerChoice) {
                handler.addCardSafety(playerHand, handler.dealCardSafety(shoe));
                Card drawnCard = handler.getCardSafety(playerHand, playerHand.getCardCount() - 1);
                io.printPlayerDrewCard(drawnCard);
                io.printPlayerAndDealerCards(player, dealer, false);
                if (isBust(player)) {
                    io.printPlayerBust();
                    dealer.incScore();
                    newRound();
                } else if (player.getHandValue() == 21) {
                    io.printPlayer21();
                    break;
                } else {
                    io.printHitOrStand();
                }
            } else {
                break;
            }
        }

        io.printDealerTurn();
        io.printDealerDrewClosedCard();
        io.printPlayerAndDealerCards(player, dealer, true);

        if (isBlackjack(dealer)) {
            io.printDealerHasBlackjack();
            dealer.incScore();
            newRound();
        }

        while (dealer.getHandValue() < 17) {
            handler.addCardSafety(dealerHand, handler.dealCardSafety(shoe));
            Card drawnCard = handler.getCardSafety(dealerHand, dealerHand.getCardCount() - 1);
            io.printDealerDrewCard(drawnCard);
            io.printPlayerAndDealerCards(player, dealer, true);
        }

        if (isBust(dealer)) {
            io.printDealerBust();
            player.incScore();
        } else if (dealer.getHandValue() > player.getHandValue()) {
            io.printDealerWon();
            dealer.incScore();
        } else if (dealer.getHandValue() < player.getHandValue()) {
            io.printPlayerWon();
            player.incScore();
        } else {
            io.printPush();
        }

        newRound();
    }

    /**
     * Sets up and starts a new round of the game.
     * Increments round counter and asks if the player wants to continue.
     */
    private void newRound() {
        roundCount++;
        io.printRoundIsOver(player, dealer);
        while (true) {
            boolean playerChoice = io.readPlayerNexRoundChoice();
            if (playerChoice) {
                playRound();
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * Checks if a participant has Blackjack (21 points with exactly 2 cards).
     *
     * @param participant The participant to check
     * @return true if the participant has Blackjack, false otherwise
     */
    private boolean isBlackjack(Participant participant) {
        return participant.getHandValue() == 21 && participant.getHand().getCardCount() == 2;
    }

    /**
     * Checks if a participant has busted (over 21 points).
     *
     * @param participant The participant to check
     * @return true if the participant has busted, false otherwise
     */
    private boolean isBust(Participant participant) {
        return participant.getHandValue() > 21;
    }
}
