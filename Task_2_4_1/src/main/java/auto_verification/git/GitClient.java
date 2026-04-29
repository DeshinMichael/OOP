package auto_verification.git;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GitClient {

    // Клонирование репозитория в нужную папку.
    // Если папка уже существует, делает git pull.
    public boolean cloneOrPull(String repoUrl, File targetDir) {
        if (targetDir.exists() && new File(targetDir, ".git").exists()) {
            System.out.println("Updating repository (git pull): " + targetDir.getName());
            return runCommand(targetDir, "git", "pull");
        } else {
            System.out.println("Cloning repository (git clone): " + repoUrl);
            
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
        System.out.println("Switching to branch: " + branch + " in " + repoDir.getName());
        return runCommand(repoDir, "git", "checkout", branch);
    }

    // Расчет активности: доля "активных" недель от условного семестра (15 недель)
    public double calculateActivity(File repoDir) {
        try {
            ProcessBuilder builder = new ProcessBuilder("git", "log", "--format=%ad", "--date=short");
            builder.directory(repoDir);
            Process process = builder.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            java.util.Set<String> activeWeeks = new java.util.HashSet<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(line.trim());
                    // Генерируем уникальный ключ года-недели
                    int week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    activeWeeks.add(date.getYear() + "-" + week);
                } catch (Exception ignored) {
                }
            }
            process.waitFor();
            
            // Предполагаем, что курс длится около 15 недель.
            int semesterWeeks = 15;
            return Math.min(1.0, (double) activeWeeks.size() / semesterWeeks);
        } catch (Exception e) {
            return 0.0;
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
                System.err.println("Command timed out: " + String.join(" ", command));
                return false;
            }

            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Execution error (" + e.getMessage() + "): " + String.join(" ", command));
            return false;
        }
    }
}
