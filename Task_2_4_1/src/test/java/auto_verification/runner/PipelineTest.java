package auto_verification.runner;

import auto_verification.git.GitClient;
import auto_verification.logger.AppLogger;
import auto_verification.model.CheckResult;
import auto_verification.model.Group;
import auto_verification.model.PipelineResult;
import auto_verification.model.ProjectConfig;
import auto_verification.model.Student;
import auto_verification.model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PipelineTest {

    @Test
    void testExecutePipeline() {
        AppLogger logger = new AppLogger() {
            @Override
            public void info(String message) {}
            @Override
            public void error(String message) {}
        };

        GitClient fakeGit = new GitClient(logger) {
            @Override
            public boolean cloneOrPull(String repoUrl, File targetDir) {
                return true;
            }
            @Override
            public boolean checkout(File repoDir, String branch) {
                return true;
            }
            @Override
            public double calculateActivity(File repoDir) {
                return 0.5;
            }
        };

        ProcessRunner fakeRunner = new ProcessRunner(logger) {
            @Override
            public boolean run(String logPrefix, File projectDir, String commandLine) {
                if (commandLine.contains("build")) return true;
                if (commandLine.contains("docs")) return true;
                if (commandLine.contains("style")) return true;
                if (commandLine.contains("check")) return true;
                if (commandLine.contains("test")) return true;
                return false;
            }
        };

        Pipeline pipeline = new Pipeline(fakeGit, fakeRunner, logger);

        ProjectConfig config = new ProjectConfig();
        config.setWorkDir(System.getProperty("java.io.tmpdir") + "/auto_verify_test");

        Group group = new Group("TestGroup");
        Student student = new Student("ivan-github", "Ivan Ivanov", "url_to_repo");
        group.getStudents().add(student);

        config.getGroups().add(group);

        Task task = new Task("Task1");
        task.setBranch("main");
        task.setBuildCmd("build");
        task.setDocsCmd("docs");
        task.setStyleCmd("style");
        task.setTestCmd("test");
        config.getTasks().add(task);

        PipelineResult result = pipeline.execute(config);

        assertNotNull(result);
        assertEquals(1, result.activities.size());
        assertEquals(0.5, result.activities.get(student));

        Map<Task, CheckResult> studRes = result.reportData.get(student);
        assertNotNull(studRes);
        CheckResult cr = studRes.get(task);
        assertNotNull(cr);
        assertTrue(cr.buildOk);
        assertTrue(cr.docsOk);
        assertTrue(cr.styleOk);
        assertTrue(cr.testOk);
    }
}
