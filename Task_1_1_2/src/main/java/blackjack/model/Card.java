package blackjack.model;

/**
 * Represents a playing card in blackjack.
 * A card has a rank and suit, and can calculate its value.
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    /**
     * Creates a new card with the specified rank and suit.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the rank of the card.
     *
     * @return the card's rank
     */
    public Rank rank() {
        return rank;
    }

    /**
     * Returns the suit of the card.
     *
     * @return the card's suit
     */
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
        return rank.getDisplayName() + " " + suit.getDisplayName();
    }
}
