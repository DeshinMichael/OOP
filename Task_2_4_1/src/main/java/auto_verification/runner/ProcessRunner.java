package auto_verification.runner;

import auto_verification.logger.AppLogger;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {
    private final AppLogger logger;

    public ProcessRunner(AppLogger logger) {
        this.logger = logger;
    }

    // Запуск любой команды студенческого проекта
    public boolean run(String logPrefix, File projectDir, String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            return false;
        }

        String prefixStr = logPrefix != null && !logPrefix.isEmpty() ? logPrefix + " " : "";
        logger.info(prefixStr + "Executing command: " + commandLine + " in " + projectDir.getName());
        try {
            // Разбор строки команды по пробелам
            String[] parts = commandLine.split("\\s+");
            
            // Поддержка gradlew на Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                if (parts[0].equals("./gradlew") || parts[0].equals("gradlew")) {
                    parts[0] = "gradlew.bat";
                }
            }

            ProcessBuilder builder = new ProcessBuilder(parts);
            builder.directory(projectDir);

            // Важен только exitCode
            builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            builder.redirectError(ProcessBuilder.Redirect.DISCARD);

            Process process = builder.start();

            // Жёсткий таймаут 5 минут для предотвращения зависания
            boolean finished = process.waitFor(5, TimeUnit.MINUTES);

            if (!finished) {
                process.destroyForcibly();
                logger.error(prefixStr + "Timeout reached (killed)");
                return false;
            }

            boolean success = process.exitValue() == 0;
            if (success) {
                logger.info(prefixStr + "Success");
            } else {
                logger.error(prefixStr + "Failed (exit code " + process.exitValue() + ")");
            }
            return success;
            
        } catch (Exception e) {
            logger.error(prefixStr + "Launch error: " + e.getMessage());
            return false;
        }
    }
}
