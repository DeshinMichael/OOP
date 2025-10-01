package blackjacktest.modeltest;

import blackjack.i18n.I18nManager;
import blackjack.model.Rank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for blackjack.model.Rank functionality.
 * Validates rank display names, values, and enum operations.
 */
class RankTest {
    private static final I18nManager i18n = I18nManager.getInstance();

    /**
     * Tests that toString method returns the display name.
     * Verifies toString matches getDisplayName for all ranks.
     */
    @Test
    void testToString() {
        for (Rank rank : Rank.values()) {
            assertEquals(rank.getDisplayName(), rank.toString());
        }
    }

    /**
     * Tests that all enum values are present and in correct order.
     * Verifies there are 13 ranks total and they are ordered correctly.
     */
    @Test
    void testEnumValues() {
        Rank[] ranks = Rank.values();
        assertEquals(13, ranks.length);
        assertEquals(Rank.TWO, ranks[0]);
        assertEquals(Rank.THREE, ranks[1]);
        assertEquals(Rank.ACE, ranks[12]);
    }

    /**
     * Tests that getValue returns the correct values for all ranks.
     */
    @ParameterizedTest
    @CsvSource({
        "TWO, 2",
        "THREE, 3",
        "FOUR, 4",
        "FIVE, 5",
        "SIX, 6",
        "SEVEN, 7",
        "EIGHT, 8",
        "NINE, 9",
        "TEN, 10",
        "JACK, 10",
        "QUEEN, 10",
        "KING, 10",
        "ACE, 11"
    })
    void testGetValue(Rank rank, int expectedValue) {
        assertEquals(expectedValue, rank.getValue());
    }

    /**
     * Tests that getDisplayName returns localized strings.
     */
    @ParameterizedTest
    @EnumSource(Rank.class)
    void testGetDisplayName(Rank rank) {
        String i18nKey = "";

        switch (rank) {
            case TWO -> i18nKey = "rank.two";
            case THREE -> i18nKey = "rank.three";
            case FOUR -> i18nKey = "rank.four";
            case FIVE -> i18nKey = "rank.five";
            case SIX -> i18nKey = "rank.six";
            case SEVEN -> i18nKey = "rank.seven";
            case EIGHT -> i18nKey = "rank.eight";
            case NINE -> i18nKey = "rank.nine";
            case TEN -> i18nKey = "rank.ten";
            case JACK -> i18nKey = "rank.jack";
            case QUEEN -> i18nKey = "rank.queen";
            case KING -> i18nKey = "rank.king";
            case ACE -> i18nKey = "rank.ace";
        }

        String expected = i18n.getString(i18nKey);
        assertEquals(expected, rank.getDisplayName());
        assertNotNull(expected);
        assertNotNull(rank.getDisplayName());
    }
}
