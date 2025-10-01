package blackjack.model;

import blackjack.i18n.I18nManager;

/**
 * Enumeration of playing card ranks.
 * Contains all ranks from two to ace with their values in blackjack.
 */
public enum Rank {
    TWO(2, "rank.two"),
    THREE(3, "rank.three"),
    FOUR(4, "rank.four"),
    FIVE(5, "rank.five"),
    SIX(6, "rank.six"),
    SEVEN(7, "rank.seven"),
    EIGHT(8, "rank.eight"),
    NINE(9, "rank.nine"),
    TEN(10, "rank.ten"),
    JACK(10, "rank.jack"),
    QUEEN(10, "rank.queen"),
    KING(10, "rank.king"),
    ACE(11, "rank.ace");

    private final int value;
    private final String i18nKey;

    /**
     * Creates a rank with the specified value and i18n key.
     *
     * @param value the card value in blackjack
     * @param i18nKey the internationalization key for the rank name
     */
    Rank(int value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
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
     * Returns the localized display name of the rank.
     *
     * @return the localized name of the rank
     */
    public String getDisplayName() {
        return I18nManager.getInstance().getString(i18nKey);
    }

    /**
     * Returns the string representation of the rank.
     *
     * @return the localized display name of the rank
     */
    @Override
    public String toString() {
        return getDisplayName();
    }
}
