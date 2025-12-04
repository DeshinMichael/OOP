package substring_search.io;

import substring_search.algorithm.ZAlgorithm;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBlockReader {
    private static final int BLOCK_SIZE = 1024 * 1024;
    private static final char SEPARATOR = '\0';

    private FileBlockReader() {}

    public static List<Long> searchPattern(String fileName, String pattern) throws IOException {
        List<Long> result = new ArrayList<>();
        int patternCharLength = pattern.length();
        if (patternCharLength == 0) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(new FileInputStream(fileName)),
                        StandardCharsets.UTF_8), BLOCK_SIZE)) {

            String overlap = "";
            long globalSymbolOffset = 0;

            char[] readBuffer = new char[BLOCK_SIZE];
            int charsRead;

            while ((charsRead = reader.read(readBuffer)) != -1) {
                String currentBlock = overlap + new String(readBuffer, 0, charsRead);
                long blockStartSymbolOffset = globalSymbolOffset - countCodePoints(overlap);

                findInBlock(currentBlock, pattern, blockStartSymbolOffset, result);

                globalSymbolOffset += countCodePoints(readBuffer, 0, charsRead);

                if (currentBlock.length() >= patternCharLength - 1) {
                    overlap = currentBlock.substring(currentBlock.length() - (patternCharLength - 1));
                } else {
                    overlap = currentBlock;
                }
            }
        }

        return result;
    }

    private static void findInBlock(String block, String pattern, long blockStartSymbolOffset, List<Long> result) {
        String combined = pattern + SEPARATOR + block;
        int[] z = ZAlgorithm.zFunction(combined);

        int patternCharLength = pattern.length();
        int searchOffset = patternCharLength + 1;

        int[] charToSymbolMap = null;
        if (containsSurrogatePairs(block)) {
            charToSymbolMap = buildCharToSymbolMap(block);
        }

        for (int i = searchOffset; i < combined.length(); i++) {
            if (z[i] >= patternCharLength) {
                int matchCharIndexInBlock = i - searchOffset;
                long matchSymbolOffsetInBlock;

                if (charToSymbolMap != null) {
                    matchSymbolOffsetInBlock = charToSymbolMap[matchCharIndexInBlock];
                } else {
                    matchSymbolOffsetInBlock = matchCharIndexInBlock;
                }
                result.add(blockStartSymbolOffset + matchSymbolOffsetInBlock);
            }
        }
    }

    private static int[] buildCharToSymbolMap(String text) {
        int len = text.length();
        int[] map = new int[len + 1];
        int symbolCount = 0;
        for (int i = 0; i < len; i++) {
            map[i] = symbolCount;
            if (!Character.isLowSurrogate(text.charAt(i))) {
                symbolCount++;
            }
        }
        map[len] = symbolCount;
        return map;
    }

    private static boolean containsSurrogatePairs(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isHighSurrogate(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static long countCodePoints(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        return str.codePointCount(0, str.length());
    }

    private static long countCodePoints(char[] buffer, int offset, int length) {
        if (buffer == null || length == 0) {
            return 0;
        }
        return Character.codePointCount(buffer, offset, length);
    }
}
