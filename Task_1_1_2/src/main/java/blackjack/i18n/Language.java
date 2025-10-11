package blackjack.i18n;

/**
 * Enum representing supported languages in the blackjack game.
 */
public enum Language {
    ENGLISH("en", "English");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}