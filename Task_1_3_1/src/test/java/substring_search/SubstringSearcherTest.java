package substring_search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import substring_search.exception.PatternException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubstringSearcherTest {

    private static final String TEST_DIR = "test_files";
    private Path testDirectory;

    @BeforeEach
    void setUp() throws IOException {
        testDirectory = Paths.get(TEST_DIR);
        Files.createDirectories(testDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(testDirectory)) {
            Files.walk(testDirectory)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                        }
                    });
        }
    }

    private void createTestFile(String fileName, String content) throws IOException {
        Path filePath = testDirectory.resolve(fileName);
        Files.writeString(filePath, content, StandardCharsets.UTF_8);
    }

    private void createLargeTestFile(String fileName, long size, String pattern, long patternPosition) throws IOException {
        Path filePath = testDirectory.resolve(fileName);
        try (BufferedOutputStream bos = new BufferedOutputStream(
                Files.newOutputStream(filePath), 8 * 1024 * 1024)) {

            byte[] buffer = new byte[1024 * 1024];
            java.util.Arrays.fill(buffer, (byte) 'A');

            long written = 0;
            while (written < patternPosition) {
                int toWrite = (int) Math.min(buffer.length, patternPosition - written);
                bos.write(buffer, 0, toWrite);
                written += toWrite;
            }

            bos.write(pattern.getBytes(StandardCharsets.UTF_8));
            written += pattern.getBytes(StandardCharsets.UTF_8).length;

            while (written < size) {
                int toWrite = (int) Math.min(buffer.length, size - written);
                bos.write(buffer, 0, toWrite);
                written += toWrite;
            }
        }
    }

    @Test
    void testFindInSimpleFile() throws IOException, PatternException {
        String fileName = "simple.txt";
        String content = "Hello world! This is a test file.";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "world");

        assertEquals(1, result.size());
        assertEquals(6L, result.get(0));
    }

    @Test
    void testFindMultipleOccurrences() throws IOException, PatternException {
        String fileName = "multiple.txt";
        String content = "test test test";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "test");

        assertEquals(3, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(5L, result.get(1));
        assertEquals(10L, result.get(2));
    }

    @Test
    void testFindInEmptyFile() throws IOException, PatternException {
        String fileName = "empty.txt";
        createTestFile(fileName, "");

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "test");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindNonExistentPattern() throws IOException, PatternException {
        String fileName = "test.txt";
        String content = "Hello world";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyPatternThrowsException() throws IOException {
        String fileName = "test.txt";
        createTestFile(fileName, "Hello world");

        assertThrows(PatternException.class, () -> {
            SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "");
        });
    }

    @Test
    void testNullPatternThrowsException() throws IOException {
        String fileName = "test.txt";
        createTestFile(fileName, "Hello world");

        assertThrows(PatternException.class, () -> {
            SubstringSearcher.find(testDirectory.resolve(fileName).toString(), null);
        });
    }

    @Test
    void testNonExistentFileThrowsException() {
        assertThrows(IOException.class, () -> {
            SubstringSearcher.find("nonexistent.txt", "test");
        });
    }

    @Test
    void testFindInLargeFile() throws IOException, PatternException {
        String fileName = "large.txt";
        int fileSize = 64 * 1024 * 2;
        int patternPosition = 64 * 1024 + 1000;

        createLargeTestFile(fileName, fileSize, "target", patternPosition);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "target");

        assertEquals(1, result.size());
        assertEquals((long)patternPosition, result.get(0));
    }

    @Test
    void testFindPatternAcrossBlocks() throws IOException, PatternException {
        String fileName = "boundary.txt";
        int fileSize = 64 * 1024 + 100;
        int patternPosition = 64 * 1024 - 2;

        createLargeTestFile(fileName, fileSize, "test", patternPosition);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "test");

        assertEquals(1, result.size());
        assertEquals((long)patternPosition, result.get(0));
    }

    @Test
    void testFindSingleCharacterPattern() throws IOException, PatternException {
        String fileName = "single.txt";
        String content = "abaca";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "a");

        assertEquals(3, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(4L, result.get(2));
    }

    @Test
    void testFindOverlappingPatterns() throws IOException, PatternException {
        String fileName = "overlap.txt";
        String content = "abababa";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "aba");

        assertEquals(3, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(4L, result.get(2));
    }

    @Test
    void testFindWithUTF8Characters() throws IOException, PatternException {
        String fileName = "utf8.txt";
        String content = "Привет мир! Это тест с русскими буквами.";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "мир");

        assertEquals(1, result.size());
        assertEquals(7L, result.get(0));
    }

    @ParameterizedTest
    @CsvSource({
            "abcdef, abc, 0",
            "abcdef, def, 3",
            "abcdef, cde, 2",
            "aaaa, aa, 0",
            "mississippi, issi, 1"
    })
    void testFindParametrized(String content, String pattern, long expectedFirst) throws IOException, PatternException {
        String fileName = "param.txt";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), pattern);

        assertFalse(result.isEmpty());
        assertEquals(expectedFirst, result.get(0));
    }

    @Test
    void testFindWithNewlines() throws IOException, PatternException {
        String fileName = "newlines.txt";
        String content = "Первая строка\nВторая строка\nТретья строка";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "Вторая");

        assertEquals(1, result.size());
        assertEquals(14L, result.get(0));
    }

    @Test
    void testFindLongPattern() throws IOException, PatternException {
        String fileName = "longpattern.txt";
        String pattern = "abcdefghijklmnopqrstuvwxyz";
        String content = "start" + pattern + "end";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), pattern);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0));
    }

    @Test
    void testFindInHugeFile() throws IOException, PatternException {
        String fileName = "huge.txt";
        long fileSize = 500L * 1024 * 1024;
        String pattern = "NEEDLE";
        long patternPosition = fileSize - 1000;

        createLargeTestFile(fileName, fileSize, pattern, patternPosition);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), pattern);

        assertEquals(1, result.size());
        assertEquals(patternPosition, result.get(0));
    }

    @Test
    void testFindTinyPatternInLargeFile() throws IOException, PatternException {
        String fileName = "largefiletinypattern.txt";
        Path filePath = testDirectory.resolve(fileName);

        long fileSize = 5L * 1024 * 1024;
        String pattern = "X";

        try (BufferedOutputStream bos = new BufferedOutputStream(
                Files.newOutputStream(filePath), 1024 * 1024)) {

            byte[] buffer = new byte[64 * 1024];
            java.util.Arrays.fill(buffer, (byte) 'A');

            buffer[1000] = (byte) 'X';
            buffer[30000] = (byte) 'X';

            long written = 0;
            boolean firstBlock = true;

            while (written < fileSize) {
                int toWrite = (int) Math.min(buffer.length, fileSize - written);
                bos.write(buffer, 0, toWrite);
                written += toWrite;

                if (firstBlock) {
                    java.util.Arrays.fill(buffer, (byte) 'A');
                    firstBlock = false;
                }
            }
        }

        List<Long> result = SubstringSearcher.find(filePath.toString(), pattern);

        assertTrue(result.size() >= 2);
        assertTrue(result.contains(1000L));
        assertTrue(result.contains(30000L));
    }

    @Test
    void testFindPatternEqualsText() throws IOException, PatternException {
        String fileName = "equal.txt";
        String content = "Hello";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "Hello");

        assertEquals(1, result.size());
        assertEquals(0L, result.get(0));
    }

    @Test
    void testFindPatternLongerThanText() throws IOException, PatternException {
        String fileName = "short.txt";
        String content = "Hi";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "Hello");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindWithRepeatingCharacters() throws IOException, PatternException {
        String fileName = "repeat.txt";
        String content = "aaabaaab";
        createTestFile(fileName, content);

        List<Long> result = SubstringSearcher.find(testDirectory.resolve(fileName).toString(), "aaab");

        assertEquals(2, result.size());
        assertEquals(0L, result.get(0));
        assertEquals(4L, result.get(1));
    }
}

