package blackjack.model;

/**
 * Enumeration of playing card suits.
 * Contains four suits: hearts, diamonds, clubs, spades.
 */
public enum Suit {
    HEARTS("Черви"),
    DIAMONDS("Буби"),
    CLUBS("Крести"),
    SPADES("Пики");

    private final String displayName;

    /**
     * Creates a suit with the specified display name.
     *
     * @param displayName the Russian name of the suit
     */
    Suit(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the suit in Russian.
     *
     * @return the Russian name of the suit
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the string representation of the suit.
     *
     * @return the display name of the suit
     */
    @Override
    public String toString() {
        return displayName;
    }
}
