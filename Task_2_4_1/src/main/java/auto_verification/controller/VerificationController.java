package auto_verification.controller;

import auto_verification.dsl.ConfigParser;
import auto_verification.git.GitClient;
import auto_verification.logger.AppLogger;
import auto_verification.model.PipelineResult;
import auto_verification.model.ProjectConfig;
import auto_verification.report.IReportGenerator;
import auto_verification.report.ReportBuilder;
import auto_verification.model.report.ReportModel;
import auto_verification.runner.Pipeline;
import auto_verification.runner.Runner;
import auto_verification.scoring.ScoreCalculator;

import java.io.File;
import java.util.List;

public class VerificationController {
    private final AppLogger logger;
    private final GitClient gitClient;
    private final Runner processRunner;
    private final List<IReportGenerator> reportGenerators;

    public VerificationController(AppLogger logger, GitClient gitClient, Runner processRunner, List<IReportGenerator> reportGenerators) {
        this.logger = logger;
        this.gitClient = gitClient;
        this.processRunner = processRunner;
        this.reportGenerators = reportGenerators;
    }

    public void run(String configPath, String defaultReportPath) {
        logger.info("Starting system...");

        File mainConfigFile = new File(configPath);

        if (!mainConfigFile.exists()) {
            logger.error("Main configuration file '" + configPath + "' not found!");
            return;
        }

        try {
            ProjectConfig config = ConfigParser.parse(mainConfigFile);

            Pipeline pipeline = new Pipeline(gitClient, processRunner, logger);

            logger.info("Running checks...");
            PipelineResult reportData = pipeline.execute(config);

            ScoreCalculator calculator = new ScoreCalculator();
            ReportBuilder reportBuilder = new ReportBuilder(calculator);
            ReportModel reportModel = reportBuilder.build(config, reportData);

            for (IReportGenerator reportGenerator : reportGenerators) {
                reportGenerator.generateReport(reportModel, new File(defaultReportPath));
            }

            logger.info("\nChecks completed!");
        } catch (Exception e) {
            logger.error("Fatal error during execution: " + e.getMessage());
        }
    }
}
