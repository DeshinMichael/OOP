package auto_verification.git;

import auto_verification.logger.AppLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GitClient {

    private final AppLogger logger;

    public GitClient(AppLogger logger) {
        this.logger = logger;
    }

    // Клонирование репозитория в нужную папку.
    // Если папка уже существует, делает git pull.
    public boolean cloneOrPull(String repoUrl, File targetDir) {
        if (targetDir.exists() && new File(targetDir, ".git").exists()) {
            logger.info("Updating repository (git pull): " + targetDir.getName());
            return runCommand(targetDir, "git", "pull");
        } else {
            logger.info("Cloning repository (git clone): " + repoUrl);
            
            // Если папка существует, но это не git-репозиторий (например, осталась от неудачного клона), 
            // git clone выдаст ошибку. Нужно её очистить.
            if (targetDir.exists()) {
                deleteDirectory(targetDir);
            }

            // Создание папки-родителя, если её нет, и вызов clone
            targetDir.getParentFile().mkdirs();
            return runCommand(targetDir.getParentFile(), "git", "clone", repoUrl, targetDir.getName());
        }
    }

    // Рекурсивное удаление папки
    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    public boolean checkout(File repoDir, String branch) {
        logger.info("Switching to branch: " + branch + " in " + repoDir.getName());
        return runCommand(repoDir, "git", "checkout", branch);
    }

    // Расчет активности: доля "активных" недель от условного семестра (15 недель)
    public double calculateActivity(File repoDir) {
        try {
            ProcessBuilder builder = new ProcessBuilder("git", "log", "--format=%ad", "--date=short");
            builder.directory(repoDir);
            Process process = builder.start();

            Set<String> activeWeeks = getStrings(process);
            process.waitFor();
            
            // Предполагаем, что курс длится около 15 недель.
            int semesterWeeks = 15;
            return Math.min(1.0, (double) activeWeeks.size() / semesterWeeks);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Set<String> getStrings(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            Set<String> activeWeeks = new HashSet<>();
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    LocalDate date = LocalDate.parse(line.trim());
                    // Генерируем уникальный ключ года-недели
                    int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    activeWeeks.add(date.getYear() + "-" + week);
                } catch (Exception ignored) {
                }
            }
            return activeWeeks;
        }
    }

    // Универсальный метод для вызова консольных команд
    private boolean runCommand(File workingDir, String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(workingDir);

            // Перенаправление вывода команд сразу в консоль преподавателя
            builder.redirectErrorStream(true);

            Process process = builder.start();

            // Ожидание максимум 2 минуты (исключение бесконечных зависаний)
            boolean finished = process.waitFor(2, TimeUnit.MINUTES);

            if (!finished) {
                process.destroyForcibly();
                logger.error("Command timed out: " + String.join(" ", command));
                return false;
            }

            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            logger.error("Execution error (" + e.getMessage() + "): " + String.join(" ", command));
            return false;
        }
    }
}
