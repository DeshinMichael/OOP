package auto_verification.controller;

import auto_verification.logger.AppLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationControllerTest {
    @Test
    void testControllerFail() {
        String[] args = new String[]{"non_existing_config.groovy"};
        StringBuilder errStr = new StringBuilder();
        try {
            VerificationController controller = new VerificationController(new AppLogger() {
                @Override
                public void info(String message) {}
                @Override
                public void error(String message) { errStr.append(message); }
            });
            controller.run(args[0]);
        } catch (Exception ignored) {}
        assertTrue(errStr.toString().contains("not found"));
    }
    @Test
    void testControllerSuccess(@TempDir File tempDir) throws Exception {
        File configFile = new File(tempDir, "main_config.groovy");
        Files.writeString(configFile.toPath(), "group('G1') { student('s1','S One','repo') }\n task('T1') { }");
        VerificationController controller = new VerificationController(new AppLogger() {
            @Override
            public void info(String message) {}
            @Override
            public void error(String message) {}
        });
        controller.run(configFile.getAbsolutePath());

        VerificationController controller2 = new VerificationController(new AppLogger() {
            @Override
            public void info(String message) {}
            @Override
            public void error(String message) {}
        });
        controller2.run("invalid_file_path_12345.groovy");
    }
}