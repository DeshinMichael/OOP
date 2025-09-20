/**
 * Represents a playing card with a rank and suit.
 * Used in the Blackjack game to store card information.
 */
public class Card {
    private final String rank;
    private final String suit;

    /**
     * Creates a new card with the specified rank and suit.
     *
     * @param rank The rank of the card (e.g., "Ace", "King", "Two")
     * @param suit The suit of the card (e.g., "Hearts", "Diamonds")
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Gets the rank of the card.
     *
     * @return The rank of the card
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns a string representation of the card.
     *
     * @return A string in the format "rank suit"
     */
    @Override
    public String toString() {
        return rank + " " + suit;
    }
}
