package blackjacktest.i18ntest;

import blackjack.i18n.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.i18n.Language.
 */
class LanguageTest {

    @Test
    void testEnumValues() {
        Language[] languages = Language.values();
        assertEquals(1, languages.length, "Should have exactly one language defined");
        assertEquals(Language.ENGLISH, languages[0], "First language should be ENGLISH");
    }

    @Test
    void testEnglishLanguage() {
        assertEquals("en", Language.ENGLISH.getCode(), "English code should be 'en'");
        assertEquals("English", Language.ENGLISH.getDisplayName(), "English display name should be 'English'");
    }

    @Test
    void testValueOf() {
        assertEquals(Language.ENGLISH, Language.valueOf("ENGLISH"), "valueOf should return ENGLISH for 'ENGLISH'");
    }

    @Test
    void testAllValuesAccessible() {
        for (Language language : Language.values()) {
            assertNotNull(language.getCode(), "Language code should not be null");
            assertNotNull(language.getDisplayName(), "Language display name should not be null");
            assertFalse(language.getCode().isEmpty(), "Language code should not be empty");
            assertFalse(language.getDisplayName().isEmpty(), "Language display name should not be empty");
        }
    }
}

