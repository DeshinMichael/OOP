package blackjack.participants;

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
}
