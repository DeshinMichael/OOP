package blackjack_test.game_test;

import blackjack.game.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.game.Game functionality.
 * Tests game initialization with different deck counts and basic game operations.
 */
class GameTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Setup method that runs before each test.
     * Redirects System.out to capture console output for testing.
     */
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * Cleanup method that runs after each test.
     * Restores the original System.out stream.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests that game creation doesn't throw exceptions.
     * Verifies basic game instantiation is safe.
     */
    @Test
    void testStartGameDoesNotThrow() {
        assertDoesNotThrow(() -> {
            Game testGame = new Game(1);
            assertNotNull(testGame);
        });
    }

    /**
     * Tests game creation with different valid deck counts.
     * Verifies games can be created with 1-8 deck configurations.
     */
    @Test
    void testGameWithDifferentDeckCounts() {
        for (int decks = 1; decks <= 8; decks++) {
            final int deckCount = decks;
            assertDoesNotThrow(() -> {
                Game testGame = new Game(deckCount);
                assertNotNull(testGame);
            });
        }
    }
}
