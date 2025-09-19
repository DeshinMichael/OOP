// Class representing a player or dealer in the Blackjack game
public class Participant {
    private final Hand hand;
    private int score = 0;

    // Constructor creating participant with empty hand and zero score
    public Participant() {
        this.hand = new Hand();
    }

    // Returns the participant's current hand of cards
    public Hand getHand() {
        return hand;
    }

    // Returns the participant's current score (rounds won)
    public int getScore() {
        return score;
    }

    // Increases participant's score by one when they win a round
    public void incScore() {
        score++;
    }

    // Calculates and returns the Blackjack value of participant's current hand
    public int getHandValue() {
        return hand.getValue();
    }

    // Clears participant's hand for a new round
    public void resetHand() {
        hand.clear();
    }
}
