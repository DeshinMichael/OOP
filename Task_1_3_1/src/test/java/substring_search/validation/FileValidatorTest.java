package substring_search.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileValidatorTest {

    private Path tempFile;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".txt");
        tempDir = Files.createTempDirectory("testdir");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    void testValidFile() throws IOException {
        assertDoesNotThrow(() -> {
            FileValidator.validateFile(tempFile.toString());
        });
    }

    @Test
    void testValidDirectory() throws IOException {
        assertDoesNotThrow(() -> {
            FileValidator.validateFile(tempDir.toString());
        });
    }

    @Test
    void testFileNotFound() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            FileValidator.validateFile("nonexistent.txt");
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testNullFileName() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            FileValidator.validateFile(null);
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testEmptyFileName() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            FileValidator.validateFile("");
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testAbsolutePath() throws IOException {
        String absolutePath = tempFile.toAbsolutePath().toString();
        assertDoesNotThrow(() -> {
            FileValidator.validateFile(absolutePath);
        });
    }

    @Test
    void testRelativePath() throws IOException {
        String relativePath = tempFile.getFileName().toString();
        if (Files.exists(Path.of(relativePath))) {
            assertDoesNotThrow(() -> {
                FileValidator.validateFile(relativePath);
            });
        }
    }

    @Test
    void testValidateMultipleTimes() throws IOException {
        assertDoesNotThrow(() -> {
            FileValidator.validateFile(tempFile.toString());
            FileValidator.validateFile(tempFile.toString());
        });
    }
}
