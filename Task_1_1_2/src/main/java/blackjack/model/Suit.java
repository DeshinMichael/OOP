package blackjack.model;

import blackjack.i18n.I18nManager;

/**
 * Enumeration of playing card suits.
 * Contains four suits: hearts, diamonds, clubs, spades.
 */
public enum Suit {
    HEARTS("suit.hearts"),
    DIAMONDS("suit.diamonds"),
    CLUBS("suit.clubs"),
    SPADES("suit.spades");

    private final String i18nKey;

    /**
     * Creates a suit with the specified i18n key.
     *
     * @param i18nKey the internationalization key for the suit name
     */
    Suit(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * Returns the localized display name of the suit.
     *
     * @return the localized name of the suit
     */
    public String getDisplayName() {
        return I18nManager.getInstance().getString(i18nKey);
    }

    /**
     * Returns the string representation of the suit.
     *
     * @return the localized display name of the suit
     */
    @Override
    public String toString() {
        return getDisplayName();
    }
}
