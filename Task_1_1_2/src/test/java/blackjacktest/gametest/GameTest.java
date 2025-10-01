package blackjacktest.gametest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import blackjack.game.Game;

import blackjack.io.ConsoleInput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.game.Game functionality.
 * Tests game initialization with different deck counts and basic game operations.
 */
class GameTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    /**
     * Setup method that runs before each test.
     */
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        ConsoleInput.closeScanner();
    }

    /**
     * Cleanup method that runs after each test.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Tests that game creation doesn't throw exceptions.
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
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 6, 8})
    void testGameWithDifferentDeckCounts(int decks) {
        assertDoesNotThrow(() -> {
            Game testGame = new Game(decks);
            assertNotNull(testGame);
        });
    }

    @Test
    void testStartGameImmediateQuit() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(1);
        assertDoesNotThrow(game::start);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("FINAL RESULTS"));
    }

    @Test
    void testStartGameOneRound() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(1);
        assertDoesNotThrow(game::start);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Round 1"));
        assertTrue(output.contains("FINAL RESULTS"));
    }

    @Test
    void testStartGameWithHitting() {
        String input = "1\n0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(1);
        assertDoesNotThrow(game::start);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Round 1"));
    }

    @Test
    void testStartGameShowsScore() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(2);
        assertDoesNotThrow(game::start);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Current score"));
        assertTrue(output.contains("Player:"));
        assertTrue(output.contains("Dealer:"));
    }

    @Test
    void testStartGameShowsFinalResults() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(4);
        assertDoesNotThrow(game::start);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("FINAL RESULTS"));
        assertTrue(output.contains("Thanks for playing"));
    }

    @Test
    void testStartGameWithMaxDecks() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(8);
        assertDoesNotThrow(game::start);
    }

    @Test
    void testGameStartingWelcomeMessage() {
        String input = "0\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Game game = new Game(1);
        game.start();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Welcome to Blackjack!"), "Should show welcome message");
    }
}