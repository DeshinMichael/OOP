package auto_verification.git;

import auto_verification.logger.AppLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GitClientTest {
    @Test
    void testGitClientFailures(@TempDir File tempDir) {
        AppLogger logger = new AppLogger() {
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        };
        GitClient client = new GitClient(logger);

        assertFalse(client.cloneOrPull("invalid_repo_url_123", new File(tempDir, "repo")));
        assertFalse(client.checkout(tempDir, "invalid_branch_123"));
        assertEquals(0.0, client.calculateActivity(tempDir));
    }
    
    @Test
    void testDeleteDirectory(@TempDir File tempDir) throws IOException {
        File dir = new File(tempDir, "deleteMe");
        dir.mkdirs();
        new File(dir, "a.txt").createNewFile();
        GitClient client = new GitClient(new AppLogger() {
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        });
        client.cloneOrPull("repo", dir);
    }
}
