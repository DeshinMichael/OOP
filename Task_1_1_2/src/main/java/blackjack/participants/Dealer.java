package blackjack.participants;

import blackjack.deck.Shoe;
import blackjack.game.RoundResult;
import blackjack.io.ConsoleOutput;
import blackjack.util.ErrorHandler;

/**
 * Represents a dealer in the Blackjack game.
 * Inherits base functionality from Participant and adds dealer-specific logic.
 */
public class Dealer extends Participant {

    /**
     * Determines whether the dealer should take another card.
     * The dealer hits until the sum reaches 17 or more.
     *
     * @return true if the dealer should hit, false if the dealer should stand
     */
    public boolean shouldHit() {
        return getHand().getValue() < 17 && !isBusted();
    }

    /**
     * Plays the dealer's turn according to blackjack rules.
     * The dealer hits until the sum reaches 17 or more.
     *
     * @param dealer the dealer
     * @param shoe the card shoe
     */
    public RoundResult makeMove(Shoe shoe, Dealer dealer) {
        ConsoleOutput.printDealerTurn();
        ConsoleOutput.printDealerDrewClosedCard();
        ConsoleOutput.printDealerCards(dealer.getHand());

        while (dealer.shouldHit()) {
            ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
            ConsoleOutput.printDealerDrewCard(ErrorHandler.getLastCardSafety(dealer.getHand()));
            ConsoleOutput.printDealerCards(dealer.getHand());
        }

        if (dealer.isBusted()) {
            ConsoleOutput.printDealerBust();
            return RoundResult.BUST;
        }
        return RoundResult.CONTINUE;
    }

    /**
     * Deals initial cards to player and dealer.
     * Each participant receives two cards.
     *
     * @param player the player
     * @param dealer the dealer
     * @param shoe the card shoe
     */
    public void dealInitialCards(Player player, Dealer dealer, Shoe shoe) {
        ErrorHandler.addCardSafety(player.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(player.getHand(), ErrorHandler.dealCardSafety(shoe));
        ErrorHandler.addCardSafety(dealer.getHand(), ErrorHandler.dealCardSafety(shoe));
    }

    public RoundResult checkInitialHand() {
        if (hasBlackjack()) {
            ConsoleOutput.printDealerHasBlackjack();
            return RoundResult.BLACKJACK;
        }
        return RoundResult.CONTINUE;
    }
}
