package auto_verification;


import auto_verification.controller.VerificationController;
import auto_verification.logger.AppLogger;

public class Main {
    public static void main(String[] args) {
        AppLogger logger = new AppLogger();
        VerificationController controller = new VerificationController(logger);
        controller.run("main_config.groovy");
    }
}
