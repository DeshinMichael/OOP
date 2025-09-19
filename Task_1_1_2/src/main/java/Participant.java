/**
 * Represents a participant in the Blackjack game (player or dealer).
 * Manages the participant's hand and score.
 */
public class Participant {
    private final Hand hand;
    private int score = 0;

    /**
     * Creates a new participant with an empty hand and zero score.
     */
    public Participant() {
        this.hand = new Hand();
    }

    /**
     * Gets the participant's hand.
     *
     * @return The participant's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Gets the participant's current score.
     *
     * @return The number of rounds won
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
     * Gets the current value of the participant's hand.
     *
     * @return The total value of the cards in the hand
     */
    public int getHandValue() {
        return hand.getValue();
    }

    /**
     * Clears the participant's hand for a new round.
     */
    public void resetHand() {
        hand.clear();
    }
}
