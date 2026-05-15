package auto_verification;


import auto_verification.controller.VerificationController;
import auto_verification.logger.AppLogger;
import auto_verification.report.HtmlReportGenerator;
import auto_verification.report.IReportGenerator;
import auto_verification.runner.ProcessRunner;
import auto_verification.runner.Runner;
import auto_verification.git.GitClient;

import java.util.List;

public class Main {
    private static final String DEFAULT_CONFIG_PATH = "main_config.groovy";
    private static final String DEFAULT_HTML_REPORT_PATH = "report.html";

    public static void main(String[] args) {
        AppLogger logger = new AppLogger();
        GitClient gitClient = new GitClient(logger);
        Runner runner = new ProcessRunner(logger);
        List<IReportGenerator> reportGenerators = List.of(
            new HtmlReportGenerator(logger)
        );

        VerificationController controller = new VerificationController(logger, gitClient, runner, reportGenerators);
        controller.run(DEFAULT_CONFIG_PATH, DEFAULT_HTML_REPORT_PATH);
    }
}
