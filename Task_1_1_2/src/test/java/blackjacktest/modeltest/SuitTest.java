package blackjacktest.modeltest;

import blackjack.i18n.I18nManager;
import blackjack.model.Suit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for blackjack.model.Suit functionality.
 * Validates suit display names and enum operations.
 */
class SuitTest {
    private static final I18nManager i18n = I18nManager.getInstance();

    /**
     * Tests that toString method returns the display name.
     * Verifies toString matches getDisplayName for all suits.
     */
    @Test
    void testToString() {
        for (Suit suit : Suit.values()) {
            assertEquals(suit.getDisplayName(), suit.toString());
        }
    }

    /**
     * Tests that all enum values are present and in correct order.
     * Verifies there are 4 suits total and they are ordered correctly.
     */
    @Test
    void testEnumValues() {
        Suit[] suits = Suit.values();
        assertEquals(4, suits.length);
        assertEquals(Suit.HEARTS, suits[0]);
        assertEquals(Suit.DIAMONDS, suits[1]);
        assertEquals(Suit.CLUBS, suits[2]);
        assertEquals(Suit.SPADES, suits[3]);
    }

    /**
     * Tests that getDisplayName returns localized strings for each suit.
     */
    @ParameterizedTest
    @EnumSource(Suit.class)
    void testGetDisplayName(Suit suit) {
        String i18nKey = "";

        switch (suit) {
            case HEARTS -> i18nKey = "suit.hearts";
            case DIAMONDS -> i18nKey = "suit.diamonds";
            case CLUBS -> i18nKey = "suit.clubs";
            case SPADES -> i18nKey = "suit.spades";
        }

        String expected = i18n.getString(i18nKey);
        assertEquals(expected, suit.getDisplayName());
        assertNotNull(expected);
        assertNotNull(suit.getDisplayName());
    }

    /**
     * Tests enum valueOf functionality.
     */
    @Test
    void testValueOf() {
        assertEquals(Suit.HEARTS, Suit.valueOf("HEARTS"));
        assertEquals(Suit.DIAMONDS, Suit.valueOf("DIAMONDS"));
        assertEquals(Suit.CLUBS, Suit.valueOf("CLUBS"));
        assertEquals(Suit.SPADES, Suit.valueOf("SPADES"));
    }
}
