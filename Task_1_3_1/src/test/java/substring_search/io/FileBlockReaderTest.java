package substring_search.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class FileBlockReaderTest {

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSearchPatternInSimpleFile() throws IOException {
        String content = "hello world hello";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "hello");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(12L, positions.get(1));
    }

    @Test
    void testSearchPatternNotFound() throws IOException {
        String content = "hello world";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "xyz");
        assertTrue(positions.isEmpty());
    }

    @Test
    void testSearchPatternAtBeginning() throws IOException {
        String content = "pattern test string";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "pattern");
        assertEquals(1, positions.size());
        assertEquals(0L, positions.get(0));
    }

    @Test
    void testSearchPatternAtEnd() throws IOException {
        String content = "test string pattern";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "pattern");
        assertEquals(1, positions.size());
        assertEquals(12L, positions.get(0));
    }

    @Test
    void testSearchSingleCharacterPattern() throws IOException {
        String content = "abcabc";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "a");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(3L, positions.get(1));
    }

    @Test
    void testSearchOverlappingPatterns() throws IOException {
        String content = "aaaa";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "aa");
        assertEquals(3, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(1L, positions.get(1));
        assertEquals(2L, positions.get(2));
    }

    @Test
    void testSearchInEmptyFile() throws IOException {
        String content = "";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "test");
        assertTrue(positions.isEmpty());
    }

    @Test
    void testFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> {
            FileBlockReader.searchPattern("nonexistent.txt", "pattern");
        });
    }

    @Test
    void testLargeFileProcessing() throws IOException {
        String content = "test".repeat(100000);
        Files.write(tempFile, content.getBytes());

        List<Long> positions = FileBlockReader.searchPattern(tempFile.toString(), "test");
        assertEquals(100000, positions.size());
    }
}
