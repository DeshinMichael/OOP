package blackjack.model;

/**
 * Represents a playing card in blackjack.
 * A card has a rank and suit, and can calculate its value.
 */
public record Card(Rank rank, Suit suit) {
    /**
     * Creates a new card with the specified rank and suit.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    public Card {}

    /**
     * Returns the rank of the card.
     *
     * @return the card's rank
     */
    @Override
    public Rank rank() {
        return rank;
    }

    /**
     * Returns the suit of the card.
     *
     * @return the card's suit
     */
    @Override
    public Suit suit() {
        return suit;
    }

    /**
     * Returns the value of the card in blackjack.
     *
     * @return the numeric value of the card
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Returns the string representation of the card.
     * Format: "Rank Suit" (e.g., "Ace Spades").
     *
     * @return string representation of the card
     */
    @Override
    public String toString() {
        return rank.toString() + " " + suit.toString();
    }
}
