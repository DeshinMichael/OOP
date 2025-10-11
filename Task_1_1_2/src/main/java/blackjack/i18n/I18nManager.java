package blackjack.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages internationalization for the blackjack application.
 * Provides localized strings based on selected language.
 */
public class I18nManager {
    private static I18nManager instance;
    private ResourceBundle messages;
    private Language currentLanguage;

    private I18nManager(Language language) {
        setLanguage(language);
    }

    /**
     * Gets the singleton instance of I18nManager.
     * Default language is Russian.
     *
     * @return the I18nManager instance
     */
    public static I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager(Language.ENGLISH);
        }
        return instance;
    }

    /**
     * Sets the current language and loads corresponding resource bundle.
     *
     * @param language the language to set
     */
    public void setLanguage(Language language) {
        this.currentLanguage = language;
        messages = ResourceBundle.getBundle("i18n.messages_en");
    }

    /**
     * Gets the current language.
     *
     * @return current language
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Gets a localized string by key.
     *
     * @param key the key for the localized string
     * @return the localized string, or the key itself if not found
     */
    public String getString(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Gets a formatted localized string with parameters.
     *
     * @param key the key for the localized string
     * @param params parameters to format into the string
     * @return the formatted localized string
     */
    public String getString(String key, Object... params) {
        try {
            String template = messages.getString(key);
            return String.format(template, params);
        } catch (Exception e) {
            return key;
        }
    }
}
