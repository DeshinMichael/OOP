package blackjack_test.io_test;

import blackjack.io.ConsoleInput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
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
        assertTrue(testOut.toString().contains("Ваш выбор:"));
    }

    @Test
    void testReadPlayerTurnChoice_Stand() {
        provideInput("0\n");
        assertFalse(ConsoleInput.readPlayerTurnChoice());
        assertTrue(testOut.toString().contains("Ваш выбор:"));
    }

    @Test
    void testReadPlayerTurnChoice_InvalidThenValid() {
        provideInput("invalid\n1\n");
        assertTrue(ConsoleInput.readPlayerTurnChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Пожалуйста, введите '1' или '0'"));
        assertTrue(output.contains("Ваш выбор:"));
    }

    @Test
    void testReadPlayerTurnChoice_EmptyStringThenValid() {
        provideInput("\n0\n");
        assertFalse(ConsoleInput.readPlayerTurnChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Пожалуйста, введите '1' или '0'"));
    }

    @Test
    void testReadPlayerNextRoundChoice_Continue() {
        provideInput("1\n");
        assertTrue(ConsoleInput.readPlayerNextRoundChoice());
        assertTrue(testOut.toString().contains("Хотите сыграть еще раунд?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_Quit() {
        provideInput("0\n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
        assertTrue(testOut.toString().contains("Хотите сыграть еще раунд?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_InvalidThenValid() {
        provideInput("invalid\n1\n");
        assertTrue(ConsoleInput.readPlayerNextRoundChoice());
        String output = testOut.toString();
        assertTrue(output.contains("Пожалуйста, введите '1' или '0'"));
        assertTrue(output.contains("Хотите сыграть еще раунд?"));
    }

    @Test
    void testReadPlayerNextRoundChoice_MultipleInvalidThenValid() {
        provideInput("yes\nno\n0\n");
        assertFalse(ConsoleInput.readPlayerNextRoundChoice());
        String output = testOut.toString();
        long errorCount = output.lines().filter(line -> line.contains("Пожалуйста, введите '1' или '0'")).count();
        assertEquals(2, errorCount);
    }
}
