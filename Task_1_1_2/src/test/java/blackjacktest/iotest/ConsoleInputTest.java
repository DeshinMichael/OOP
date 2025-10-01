package blackjacktest.iotest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import blackjack.io.ConsoleInput;
import blackjack.i18n.I18nManager;
import blackjack.i18n.Language;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleInputTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        I18nManager.getInstance().setLanguage(Language.ENGLISH);
        ConsoleInput.closeScanner();
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testReadPlayerTurnChoice_Hit() {
        provideInput("1\n");
        assertTrue(ConsoleInput.readPlayerTurnChoice());
        assertTrue(testOut.toString().contains("Your choice:"));
    }

    @Test
    void testReadPlayerTurnChoice_Stand() {
        provideInput("0\n");
        assertFalse(ConsoleInput.readPlayerTurnChoice());
        assertTrue(testOut.toString().contains("Your choice:"));
    }

    @Test
    void testReadPlayerTurnChoice_InvalidThenValid() {
        provideInput("invalid\n1\n");
        assertTrue(ConsoleInput.readPlayerTurnChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Please enter '1' or '0'"));
        assertTrue(output.contains("Your choice:"));
    }

    @Test
    void testReadPlayerTurnChoice_EmptyStringThenValid() {
        provideInput("\n0\n");
        assertFalse(ConsoleInput.readPlayerTurnChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Please enter '1' or '0'"));
    }

    @Test
    void testReadPlayerNextRoundChoice_Continue() {
        provideInput("1\n");
        assertTrue(ConsoleInput.readPlayerNextRoundChoice());
        assertTrue(testOut.toString().contains("Do you want to play another round?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_Quit() {
        provideInput("0\n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
        assertTrue(testOut.toString().contains("Do you want to play another round?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_InvalidThenValid() {
        provideInput("invalid\n1\n");
        assertTrue(ConsoleInput.readPlayerNextRoundChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Please enter '1' or '0'"));
        assertTrue(output.contains("Do you want to play another round?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_MultipleInvalidThenValid() {
        provideInput("yes\nno\n0\n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
        String output = testOut.toString();
        long errorCount = output.lines()
            .filter(line -> line.contains("Please enter '1' or '0'")).count();
        assertEquals(2, errorCount);
    }

    @Test
    void testCloseScanner() {
        assertDoesNotThrow(ConsoleInput::closeScanner);
    }

    @Test
    void testReadPlayerTurnChoice_MultipleInvalid() {
        provideInput("abc\nxyz\n123\n1\n");
        assertTrue(ConsoleInput.readPlayerTurnChoice());
        String output = testOut.toString();
        long errorCount = output.lines()
            .filter(line -> line.contains("Please enter '1' or '0'")).count();
        assertEquals(3, errorCount);
    }

    @Test
    void testReadPlayerNextRoundChoice_InvalidChars() {
        provideInput("&\n*\n!\n0\n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
        String output = testOut.toString();
        long errorCount = output.lines()
            .filter(line -> line.contains("Please enter '1' or '0'")).count();
        assertEquals(3, errorCount);
    }

    @Test
    void testReadPlayerTurnChoice_NumbersWithSpaces() {
        provideInput(" 1 \n");
        assertTrue(ConsoleInput.readPlayerTurnChoice());

        ConsoleInput.closeScanner();

        provideInput(" 0 \n");
        assertFalse(ConsoleInput.readPlayerTurnChoice());
    }

    @Test
    void testReadPlayerNextRoundChoice_NumbersWithSpaces() {
        provideInput(" 1 \n");
        assertTrue(ConsoleInput.readPlayerNextRoundChoice());

        ConsoleInput.closeScanner();

        provideInput(" 0 \n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
    }
}
