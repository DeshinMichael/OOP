package auto_verification.runner;

import auto_verification.git.GitClient;
import auto_verification.model.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipeline {
    private final GitClient git;
    private final ProcessRunner runner;

    public Pipeline(GitClient git, ProcessRunner runner) {
        this.git = git;
        this.runner = runner;
    }

    public PipelineResult execute(ProjectConfig config) {
        Map<Student, Map<Task, CheckResult>> allResults = new HashMap<>();
        Map<Student, Double> allActivities = new HashMap<>();
        
        File workDirFile = new File(config.getWorkDir());

        for (Group group : config.getGroups()) {
            for (Student student : group.getStudents()) {
                System.out.println("\n=========================================");
                System.out.println("Student: " + student.getName() + " (" + group.getName() + ")");
                
                Map<Task, CheckResult> studentResults = new HashMap<>();
                allResults.put(student, studentResults);
                
                // Целевая папка для репозитория студента
                File studentRepoDir = new File(workDirFile, student.getName());

                // 1. Скачивание кода и его обновление
                boolean gitOk = git.cloneOrPull(student.getRepoUrl(), studentRepoDir);
                if (!gitOk) {
                    System.err.println("GIT error. Skipping student.");
                    for (Task t : config.getTasks()) {
                        studentResults.put(t, new CheckResult(false, false, false, false));
                    }
                    allActivities.put(student, 0.0);
                    continue; 
                }

                // Расчет активности после успешного pull/clone
                double activity = git.calculateActivity(studentRepoDir);
                allActivities.put(student, activity);
                System.out.println("Git Activity: " + (int)(activity * 100) + "%");

                // 2. Проверяем каждую задачу из конфигурации
                for (Task task : config.getTasks()) {
                    System.out.println("--- Task: " + task.getId() + " ---");
                    CheckResult result = new CheckResult();
                    studentResults.put(task, result);

                    // Пытаемся переключить ветку для данной логики
                    if (!git.checkout(studentRepoDir, task.getBranch())) {
                        System.err.println("Branch " + task.getBranch() + " not found. 0 points.");
                        continue;
                    }

                    // Папка, в которой должна запускаться задача
                    File taskDir = new File(studentRepoDir, task.getDir());
                    if (!taskDir.exists() || !taskDir.isDirectory()) {
                        // Если папки нет, попробуем запускать в корне как раньше,
                        // но сообщим об этом.
                        taskDir = studentRepoDir;
                    }

                    // Если Build (компиляция) падает — остальные этапы не запускаются.
                    if (task.getBuildCmd() != null) {
                        result.buildOk = runner.run(taskDir, task.getBuildCmd());
                        if (!result.buildOk) {
                            System.err.println("Build failed, skipping remaining checks.");
                            continue;
                        }
                    } else {
                        result.buildOk = true;
                    }

                    if (task.getDocsCmd() != null) {
                        result.docsOk = runner.run(taskDir, task.getDocsCmd());
                    } else {
                        result.docsOk = true;
                    }

                    if (task.getStyleCmd() != null) {
                        result.styleOk = runner.run(taskDir, task.getStyleCmd());
                    } else {
                        result.styleOk = true;
                    }

                    // Тесты запускаются только если документация и стиль прошли (либо их нет)
                    if (result.docsOk && result.styleOk) {
                        if (task.getTestCmd() != null) {
                            result.testOk = runner.run(taskDir, task.getTestCmd());
                            parseTestResults(taskDir, result);
                        } else {
                            result.testOk = true;
                        }
                    } else {
                        System.out.println("Docs or Style failed, skipping tests.");
                        result.testOk = false;
                    }
                }
            }
        }

        return new PipelineResult(allResults, allActivities);
    }

    private void parseTestResults(File taskDir, CheckResult result) {
        File testResultsDir = new File(taskDir, "build/test-results/test");
        if (testResultsDir.exists() && testResultsDir.isDirectory()) {
            File[] xmlFiles = testResultsDir.listFiles((d, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File xml : xmlFiles) {
                    try {
                        String content = Files.readString(xml.toPath());
                        // Простой парсинг атрибутов
                        Matcher mTotal = Pattern.compile("tests=\"(\\d+)\"").matcher(content);
                        if (mTotal.find()) result.testsTotal += Integer.parseInt(mTotal.group(1));

                        Matcher mFailed = Pattern.compile("failures=\"(\\d+)\"").matcher(content);
                        if (mFailed.find()) result.testsFailed += Integer.parseInt(mFailed.group(1));

                        Matcher mSkipped = Pattern.compile("skipped=\"(\\d+)\"").matcher(content);
                        if (mSkipped.find()) result.testsSkipped += Integer.parseInt(mSkipped.group(1));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }
}
