package substring_search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import substring_search.exception.PatternException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubstringSearcherTest {

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
    void testSuccessfulSearch() throws IOException, PatternException {
        String content = "hello world hello";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "hello");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(12L, positions.get(1));
    }

    @Test
    void testSearchWithInvalidPattern() {
        assertThrows(PatternException.class, () -> {
            SubstringSearcher.find(tempFile.toString(), null);
        });
    }

    @Test
    void testSearchWithEmptyPattern() {
        assertThrows(PatternException.class, () -> {
            SubstringSearcher.find(tempFile.toString(), "");
        });
    }

    @Test
    void testSearchWithNonexistentFile() {
        assertThrows(FileNotFoundException.class, () -> {
            SubstringSearcher.find("nonexistent.txt", "pattern");
        });
    }

    @Test
    void testSearchPatternNotFound() throws IOException, PatternException {
        String content = "hello world";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "xyz");
        assertTrue(positions.isEmpty());
    }

    @Test
    void testSearchInEmptyFile() throws IOException, PatternException {
        String content = "";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "test");
        assertTrue(positions.isEmpty());
    }

    @Test
    void testSearchSingleCharacter() throws IOException, PatternException {
        String content = "abcabc";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "a");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(3L, positions.get(1));
    }

    @Test
    void testSearchComplexPattern() throws IOException, PatternException {
        String content = "The quick brown fox jumps over the lazy dog. The fox is quick.";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "fox");
        assertEquals(2, positions.size());
        assertEquals(16L, positions.get(0));
        assertEquals(49L, positions.get(1));
    }

    @Test
    void testSearchWithSpecialCharacters() throws IOException, PatternException {
        String content = "test@#$%test";
        Files.write(tempFile, content.getBytes());

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "@#$%");
        assertEquals(1, positions.size());
        assertEquals(4L, positions.get(0));
    }

    @Test
    void testSearchValidationFlow() throws IOException {
        String content = "test content";
        Files.write(tempFile, content.getBytes());

        assertThrows(PatternException.class, () -> {
            SubstringSearcher.find(tempFile.toString(), "");
        });

        assertThrows(FileNotFoundException.class, () -> {
            SubstringSearcher.find("invalid.txt", "test");
        });
    }

    @Test
    void testSearchWithJapaneseCharacters() throws IOException, PatternException {
        String content = "こんにちは世界こんにちは";
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "こんにちは");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(7L, positions.get(1));
    }

    @Test
    void testSearchWithEmojis() throws IOException, PatternException {
        String content = "Hello 😀 World 😀 Test";
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "😀");
        assertEquals(2, positions.size());
        assertEquals(6L, positions.get(0));
        assertEquals(14L, positions.get(1));
    }

    @Test
    void testSearchMixedUnicodeCharacters() throws IOException, PatternException {
        String content = "Test 日本語 and 🎌 emoji 日本語";
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "日本語");
        assertEquals(2, positions.size());
        assertEquals(5L, positions.get(0));
        assertEquals(21L, positions.get(1));
    }

    @Test
    void testSearchComplexEmojis() throws IOException, PatternException {
        String content = "🌟✨💫🌟✨";
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

        List<Long> positions = SubstringSearcher.find(tempFile.toString(), "🌟✨");
        assertEquals(2, positions.size());
        assertEquals(0L, positions.get(0));
        assertEquals(3L, positions.get(1));
    }

    @Test
    void testSearchIn512MBFile() throws IOException, PatternException {
        Path largeFile = Files.createTempFile("large_512mb", ".txt");
        try {
            long targetSize = 512L * 1024 * 1024;
            long patternPosition = 256L * 1024 * 1024;
            String pattern = "TARGET512MB";
            byte[] patternBytes = pattern.getBytes(StandardCharsets.UTF_8);
            byte[] filler = new byte[1024 * 1024];
            java.util.Arrays.fill(filler, (byte) 'a');

            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(largeFile))) {
                for (int i = 0; i < 256; i++) {
                    out.write(filler);
                }
                out.write(patternBytes);
                long remaining = targetSize - patternPosition - patternBytes.length;
                while (remaining > 0) {
                    int toWrite = (int) Math.min(filler.length, remaining);
                    out.write(filler, 0, toWrite);
                    remaining -= toWrite;
                }
            }

            List<Long> positions = SubstringSearcher.find(largeFile.toString(), pattern);
            assertEquals(1, positions.size());
            assertEquals(patternPosition, positions.get(0));
        } finally {
            Files.deleteIfExists(largeFile);
        }
    }
}
