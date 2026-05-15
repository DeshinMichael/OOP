package auto_verification.runner;

import auto_verification.git.GitClient;
import auto_verification.logger.AppLogger;
import auto_verification.model.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipeline {
    public static final String DEFAULT_TEST_RESULTS_DIR = "build/test-results/test";

    private final GitClient git;
    private final Runner runner;
    private final AppLogger logger;

    public Pipeline(GitClient git, Runner runner, AppLogger logger) {
        this.git = git;
        this.runner = runner;
        this.logger = logger;
    }

    @SuppressWarnings("resource")
    public PipelineResult execute(ProjectConfig config) {
        Map<Student, Map<Task, CheckResult>> allResults = new ConcurrentHashMap<>();
        Map<Student, Double> allActivities = new ConcurrentHashMap<>();
        
        File workDirFile = new File(config.getWorkDir());

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (Group group : config.getGroups()) {
            for (Student student : group.getStudents()) {
                executor.submit(() -> processStudent(student, group, workDirFile,
                        config, allResults, allActivities));
            }
        }

        executor.shutdown();
        try {
            // Ждём завершения всех проверок (например, максимум 1 час)
            boolean finished = executor.awaitTermination(1, TimeUnit.HOURS);
            if (!finished) {
                logger.error("Timeout reached! Some students checks were not finished.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Pipeline was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return new PipelineResult(allResults, allActivities);
    }

    private void processStudent(Student student, Group group, File workDirFile, ProjectConfig config,
                                Map<Student, Map<Task, CheckResult>> allResults,
                                Map<Student, Double> allActivities) {
        logger.info("Student: " + student.getFullName() + " (" + group.getName() + ")");

        Map<Task, CheckResult> studentResults = new HashMap<>();
        allResults.put(student, studentResults);

        // Целевая папка для репозитория студента
        File studentRepoDir = new File(workDirFile, student.getFullName());

        // 1. Скачивание кода и его обновление
        boolean gitOk = git.cloneOrPull(student.getRepoUrl(), studentRepoDir);
        if (!gitOk) {
            logger.error("[" + student.getGithubNickname() + "] GIT error. Skipping student.");
            for (Task t : config.getTasks()) {
                studentResults.put(t, new CheckResult(false, false, false, false));
            }
            allActivities.put(student, 0.0);
            return;
        }

        // Расчет активности после успешного pull/clone
        double activity = git.calculateActivity(studentRepoDir);
        allActivities.put(student, activity);
        logger.info("[" + student.getGithubNickname() + "] Git Activity: " + (int)(activity * 100) + "%");

        // 2. Проверяем каждую задачу из конфигурации
        for (Task task : config.getTasks()) {
            CheckResult result = new CheckResult();
            studentResults.put(task, result);
            processTask(student, task, studentRepoDir, result);
        }
    }

    private void processTask(Student student, Task task, File studentRepoDir, CheckResult result) {
        String logPrefix = "[" + student.getGithubNickname() + "]";
        logger.info(logPrefix + " --- Task: " + task.getId() + " ---");

        // Пытаемся переключить ветку для данной логики
        if (!git.checkout(studentRepoDir, task.getBranch())) {
            logger.error(logPrefix + " Branch " + task.getBranch() + " not found. 0 points.");
            return;
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
            result.buildOk = runner.run(logPrefix, taskDir, task.getBuildCmd());
            if (!result.buildOk) {
                logger.error(logPrefix + " Build failed, skipping remaining checks.");
                return;
            }
        } else {
            result.buildOk = true;
        }

        if (task.getDocsCmd() != null) {
            result.docsOk = runner.run(logPrefix, taskDir, task.getDocsCmd());
        } else {
            result.docsOk = true;
        }

        if (task.getStyleCmd() != null) {
            result.styleOk = runner.run(logPrefix, taskDir, task.getStyleCmd());
        } else {
            result.styleOk = true;
        }

        // Тесты запускаются только если документация и стиль прошли (либо их нет)
        if (result.docsOk && result.styleOk) {
            if (task.getTestCmd() != null) {
                result.testOk = runner.run(logPrefix, taskDir, task.getTestCmd());
                parseTestResults(taskDir, result);
            } else {
                result.testOk = true;
            }
        } else {
            logger.info(logPrefix + " Docs or Style failed, skipping tests.");
            result.testOk = false;
        }
    }

    private void parseTestResults(File taskDir, CheckResult result) {
        File testResultsDir = new File(taskDir, DEFAULT_TEST_RESULTS_DIR);
        if (testResultsDir.exists() && testResultsDir.isDirectory()) {
            File[] xmlFiles = testResultsDir.listFiles((d, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File xml : xmlFiles) {
                    try {
                        String content = Files.readString(xml.toPath());
                        // Простой парсинг атрибутов
                        Matcher mTotal = Pattern.compile("tests=\"(\\d+)\"").matcher(content);
                        if (mTotal.find()) {
                            result.testsTotal += Integer.parseInt(mTotal.group(1));
                        }

                        Matcher mFailed = Pattern.compile("failures=\"(\\d+)\"").matcher(content);
                        if (mFailed.find()) {
                            result.testsFailed += Integer.parseInt(mFailed.group(1));
                        }

                        Matcher mSkipped = Pattern.compile("skipped=\"(\\d+)\"").matcher(content);
                        if (mSkipped.find()) {
                            result.testsSkipped += Integer.parseInt(mSkipped.group(1));
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }
}
