public class Game {
    private final int numDecks;
    private final Participant player;
    private final Participant dealer;
    private static int roundCount;
    private final ConsoleIO io;
    private final ErrorHandler handler;

    public Game(int numDecks, ConsoleIO io, ErrorHandler handler) {
        this.numDecks = numDecks;
        player = new Participant();
        dealer = new Participant();
        roundCount = 1;
        this.io = io;
        this.handler = handler;
    }

    public void startGame() {
        io.printHelloMessage();
        playRound();
    }

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
                io.printPlayerDrewCard(handler.getCardSafety(playerHand, playerHand.getCardCount() - 1));
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
            io.printDealerDrewCard(handler.getCardSafety(dealerHand, dealerHand.getCardCount() - 1));
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

    private boolean isBlackjack(Participant participant) {
        return participant.getHandValue() == 21 && participant.getHand().getCardCount() == 2;
    }

    private boolean isBust(Participant participant) {
        return participant.getHandValue() > 21;
    }
}
