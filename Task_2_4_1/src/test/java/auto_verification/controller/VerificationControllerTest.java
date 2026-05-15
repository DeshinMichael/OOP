package auto_verification.controller;

import auto_verification.logger.AppLogger;
import auto_verification.git.GitClient;
import auto_verification.runner.Runner;
import auto_verification.runner.ProcessRunner;
import auto_verification.report.IReportGenerator;
import auto_verification.report.HtmlReportGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerificationControllerTest {
    @Test
    void testControllerFail() {
        String[] args = new String[]{"non_existing_config.groovy"};
        StringBuilder errStr = new StringBuilder();
        try {
            AppLogger logger = new AppLogger() {
                @Override
                public void info(String message) {}
                @Override
                public void error(String message) { errStr.append(message); }
            };
            GitClient git = new GitClient(logger);
            Runner runner = new ProcessRunner(logger);
            List<IReportGenerator> generators = List.of(new HtmlReportGenerator(logger));

            VerificationController controller = new VerificationController(logger, git, runner, generators);
            controller.run(args[0], "report.html");
        } catch (Exception ignored) {}
        assertTrue(errStr.toString().contains("not found"));
    }
    @Test
    void testControllerSuccess(@TempDir File tempDir) throws Exception {
        File configFile = new File(tempDir, "main_config.groovy");
        String workDirStr = new File(tempDir, "workDir").getAbsolutePath().replace("\\", "/");
        Files.writeString(configFile.toPath(), "settings { workDir = '" + workDirStr + "' }\ngroup('G1') { student('s1','S One','repo') }\n task('T1') { }");
        
        AppLogger logger = new AppLogger() {
            @Override
            public void info(String message) {}
            @Override
            public void error(String message) {}
        };
        GitClient git = new GitClient(logger);
        Runner runner = new ProcessRunner(logger);
        List<IReportGenerator> generators = List.of(new HtmlReportGenerator(logger));

        VerificationController controller = new VerificationController(logger, git, runner, generators);
        controller.run(configFile.getAbsolutePath(), "report.html");

        VerificationController controller2 = new VerificationController(logger, git, runner, generators);
        controller2.run("invalid_file_path_12345.groovy", "report.html");
    }
}