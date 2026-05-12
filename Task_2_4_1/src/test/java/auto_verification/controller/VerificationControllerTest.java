package auto_verification.controller;

import auto_verification.logger.AppLogger;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationControllerTest {
    @Test
    void testControllerFail() {
        String[] args = new String[]{"non_existing_config.groovy"};

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        try {
            VerificationController controller = new VerificationController(new AppLogger());
            controller.run(args[0]);
        } catch (Exception ignored) {}

        System.setErr(System.err);

        assertTrue(errContent.toString().contains("not found"));
    }
}
