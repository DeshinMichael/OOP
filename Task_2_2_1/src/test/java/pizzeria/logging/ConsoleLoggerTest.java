package pizzeria.logging;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleLoggerTest {

    @Test
    void logPrintsToStdout() {
        ConsoleLogger logger = new ConsoleLogger();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try {
            logger.log("hello pizzeria");
        } finally {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("hello pizzeria"));
    }
}
