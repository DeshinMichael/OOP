package auto_verification.runner;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {

    // Запуск любой команды студенческого проекта
    public boolean run(File projectDir, String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            return false;
        }

        System.out.println("Executing command: " + commandLine + " in " + projectDir.getName());
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
                System.err.println("Timeout reached (killed)");
                return false;
            }

            boolean success = process.exitValue() == 0;
            if (success) {
                System.out.println("Success");
            } else {
                System.err.println("Failed (exit code " + process.exitValue() + ")");
            }
            return success;
            
        } catch (Exception e) {
            System.err.println("Launch error: " + e.getMessage());
            return false;
        }
    }
}
