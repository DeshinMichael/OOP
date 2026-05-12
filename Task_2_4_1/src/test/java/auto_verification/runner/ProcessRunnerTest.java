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
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        String cmd = System.getProperty("os.name").toLowerCase().contains("win") ? "cmd /c echo test" : "echo test";
        assertTrue(pr.run(new File("."), cmd));
    }
    @Test
    void testRunFailEmpty() {
        AppLogger logger = new AppLogger() {
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        assertFalse(pr.run(new File("."), ""));
        assertFalse(pr.run(new File("."), null));
    }
    @Test
    void testRunFailInvalid() {
        AppLogger logger = new AppLogger() {
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        };
        ProcessRunner pr = new ProcessRunner(logger);
        assertFalse(pr.run(new File("."), "invalid_cmd_that_does_not_exist_12345"));
    }
}
