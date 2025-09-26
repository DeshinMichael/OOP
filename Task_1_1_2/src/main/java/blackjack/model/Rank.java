package blackjack.model;

/**
 * Enumeration of playing card ranks.
 * Contains all ranks from two to ace with their values in blackjack.
 */
public enum Rank {
    TWO(2, "Двойка"),
    THREE(3, "Тройка"),
    FOUR(4, "Четверка"),
    FIVE(5, "Пятерка"),
    SIX(6, "Шестерка"),
    SEVEN(7, "Семерка"),
    EIGHT(8, "Восьмерка"),
    NINE(9, "Девятка"),
    TEN(10, "Десятка"),
    JACK(10, "Валет"),
    QUEEN(10, "Дама"),
    KING(10, "Король"),
    ACE(11, "Туз");

    private final int value;
    private final String displayName;

    /**
     * Creates a rank with the specified value and display name.
     *
     * @param value the card value in blackjack
     * @param displayName the Russian name of the rank
     */
    Rank(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * Returns the card value in blackjack.
     *
     * @return the numeric value of the card
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the display name of the rank in Russian.
     *
     * @return the Russian name of the rank
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the string representation of the rank.
     *
     * @return the display name of the rank
     */
    @Override
    public String toString() {
        return displayName;
    }
}
