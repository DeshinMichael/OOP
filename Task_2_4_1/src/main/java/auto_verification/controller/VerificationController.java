package auto_verification.controller;

import auto_verification.dsl.ConfigParser;
import auto_verification.git.GitClient;
import auto_verification.logger.AppLogger;
import auto_verification.model.PipelineResult;
import auto_verification.model.ProjectConfig;
import auto_verification.report.HtmlReportGenerator;
import auto_verification.runner.Pipeline;
import auto_verification.runner.ProcessRunner;
import auto_verification.scoring.ScoreCalculator;

import java.io.File;

public class VerificationController {
    private final AppLogger logger;

    public VerificationController(AppLogger logger) {
        this.logger = logger;
    }

    public void run(String configPath) {
        logger.info("Starting system...");

        File mainConfigFile = new File(configPath);

        if (!mainConfigFile.exists()) {
            logger.error("Main configuration file 'main_config.groovy' not found!");
            return;
        }

        try {
            ProjectConfig config = ConfigParser.parse(mainConfigFile);

            GitClient gitClient = new GitClient(logger);
            ProcessRunner processRunner = new ProcessRunner(logger);
            Pipeline pipeline = new Pipeline(gitClient, processRunner, logger);

            logger.info("Running checks...");
            PipelineResult reportData = pipeline.execute(config);

            ScoreCalculator calculator = new ScoreCalculator();
            HtmlReportGenerator reportGenerator = new HtmlReportGenerator(calculator, logger);
            reportGenerator.generateReport(config, reportData, new File("report.html"));

            logger.info("\nChecks completed!");
        } catch (Exception e) {
            logger.error("Fatal error during execution: " + e.getMessage());
        }
    }
}
