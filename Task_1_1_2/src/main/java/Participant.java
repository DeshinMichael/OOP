public class Participant {
    private final Hand hand;
    private int score = 0;

    public Participant() {
        this.hand = new Hand();
    }

    public Hand getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        score++;
    }

    public int getHandValue() {
        return hand.getValue();
    }

    public void resetHand() {
        hand.clear();
    }
}
