package blackjack.participants;

import blackjack.model.Hand;

/**
 * Base class for participants in the Blackjack game.
 * Represents common functionality for player and dealer.
 */
public class Participant {
    private final Hand hand;
    private int score;

    /**
     * Creates a new participant with an empty hand and zero score.
     */
    public Participant() {
        this.hand = new Hand();
        this.score = 0;
    }

    /**
     * Returns the participant's hand.
     *
     * @return the participant's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the current score of the participant.
     *
     * @return the participant's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Increments the participant's score by one.
     */
    public void incScore() {
        score++;
    }

    /**
     * Checks if the participant has blackjack.
     * Blackjack is 21 points with exactly two cards.
     *
     * @return true if the participant has blackjack, false otherwise
     */
    public boolean hasBlackjack() {
        return hand.getCardCount() == 2 && hand.getValue() == 21;
    }

    /**
     * Checks if the participant has busted (exceeded 21 points).
     *
     * @return true if the hand value is greater than 21, false otherwise
     */
    public boolean isBusted() {
        return hand.getValue() > 21;
    }
}
