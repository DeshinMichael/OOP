package blackjacktest.i18ntest;

import blackjack.i18n.I18nManager;
import blackjack.i18n.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class I18nManagerTest {
    private I18nManager i18n;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = I18nManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
        i18n = I18nManager.getInstance();
    }

    @Test
    void testGetInstanceReturnsSameInstance() {
        I18nManager firstCall = I18nManager.getInstance();
        I18nManager secondCall = I18nManager.getInstance();
        assertSame(firstCall, secondCall, "getInstance should always return the same instance");
    }

    @Test
    void testGetInstanceDefaultLanguage() {
        I18nManager instance = I18nManager.getInstance();
        assertEquals(Language.ENGLISH, instance.getCurrentLanguage(),
                "Default language should be ENGLISH");
    }

    @Test
    void testSetLanguage() {
        i18n.setLanguage(Language.ENGLISH);
        assertEquals(Language.ENGLISH, i18n.getCurrentLanguage(),
                "Current language should be updated to ENGLISH");
    }

    @ParameterizedTest
    @CsvSource({
        "rank.ace, Ace",
        "rank.king, King",
        "rank.queen, Queen",
        "suit.hearts, Hearts",
        "suit.clubs, Clubs"
    })
    void testGetStringForValidKeys(String key, String expectedValue) {
        String result = i18n.getString(key);
        assertEquals(expectedValue, result, "Should return the correct localized string for " + key);
    }

    @Test
    void testGetStringForInvalidKey() {
        String nonExistentKey = "nonexistent.key";
        String result = i18n.getString(nonExistentKey);
        assertEquals(nonExistentKey, result, "Should return the key itself for non-existent keys");
    }

    @Test
    void testGetStringWithParams() {
        String result = i18n.getString("score.final", 5, 3);
        assertTrue(result.contains("5"), "Formatted string should contain first parameter");
        assertTrue(result.contains("3"), "Formatted string should contain second parameter");
    }

    @Test
    void testGetStringWithParamsForInvalidKey() {
        String nonExistentKey = "nonexistent.key.with.params";
        String result = i18n.getString(nonExistentKey, "param1", "param2");
        assertEquals(nonExistentKey, result, "Should return the key itself for non-existent formatted keys");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "game.welcome",
        "game.round.header",
        "result.player.wins",
        "result.dealer.wins"
    })
    void testGetStringForVariousKeys(String key) {
        String result = i18n.getString(key);
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testFormatStringWithMultipleParams() {
        String result = i18n.getString("hand.player", "Ace Hearts, King Spades", 21);
        assertTrue(result.contains("Ace Hearts, King Spades"), "Should contain the hand string");
        assertTrue(result.contains("21"), "Should contain the hand value");
    }

    @Test
    void testExceptionHandlingInGetString() {
        String problematicKey = "potentially.problematic.key";
        assertDoesNotThrow(() -> i18n.getString(problematicKey),
                "getString should handle exceptions gracefully");
    }
}

