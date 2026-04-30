package auto_verification.logger;

public class AppLogger {

    public void info(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.err.println(message);
    }

    public void divider() {
        System.out.println("\n=========================================");
    }
}
