package auto_verification.runner;

import auto_verification.logger.AppLogger;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessRunnerTest {
    @Test
    void testRunSuccess() {
        AppLogger logger = new AppLogger() {
            @Override public void info(String message) {}
            @Override public void error(String message) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        
        String[] cmd = System.getProperty("os.name").toLowerCase().contains("win") ? 
            new String[]{"cmd.exe", "/c", "echo test"} : new String[]{"echo", "test"};
        
        assertTrue(pr.run("[Prefix]", new File("."), String.join(" ", cmd)));
    }

    @Test
    void testRunFailEmptyCommand() {
        AppLogger logger = new AppLogger() {
            @Override public void info(String message) {}
            @Override public void error(String message) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        
        assertFalse(pr.run("[Prefix]", new File("."), ""));
        assertFalse(pr.run("[Prefix]", new File("."), null));
    }

    @Test
    void testRunFailInvalidCommand() {
        AppLogger logger = new AppLogger() {
            @Override public void info(String message) {}
            @Override public void error(String message) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        
        assertFalse(pr.run("[Prefix]", new File("."), "invalid_cmd_that_does_not_exist_12345"));
    }
}
