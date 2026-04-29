package auto_verification;

import auto_verification.dsl.ConfigParser;
import auto_verification.git.GitClient;
import auto_verification.model.ProjectConfig;
import auto_verification.report.HtmlReportGenerator;
import auto_verification.runner.Pipeline;
import auto_verification.model.PipelineResult;
import auto_verification.runner.ProcessRunner;
import auto_verification.scoring.ScoreCalculator;

import java.io.File;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting system...");

        File mainConfigFile = new File("main_config.groovy");

        if (!mainConfigFile.exists()) {
            System.err.println("Main configuration file 'main_config.groovy' not found!");
            return;
        }

        ProjectConfig config = ConfigParser.parse(mainConfigFile);

        GitClient gitClient = new GitClient();
        ProcessRunner processRunner = new ProcessRunner();
        Pipeline pipeline = new Pipeline(gitClient, processRunner);

        System.out.println("Running checks...");
        PipelineResult reportData = pipeline.execute(config);

        ScoreCalculator calculator = new ScoreCalculator();
        HtmlReportGenerator reportGenerator = new HtmlReportGenerator(calculator);
        reportGenerator.generateReport(config, reportData, new File("report.html"));

        System.out.println("\nChecks completed!");
    }
}
