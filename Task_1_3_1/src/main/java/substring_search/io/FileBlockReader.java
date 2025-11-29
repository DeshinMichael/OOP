package substring_search.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import substring_search.algorithm.ZAlgorithm;

public class FileBlockReader {
    private static final int BLOCK_SIZE = 1 << 18; // 256KB
    private static final char SEPARATOR = '\u0000';

    private FileBlockReader() {}

    public static List<Long> searchPattern(String fileName, String pattern) throws IOException {
        List<Long> result = new ArrayList<>();
        int m = pattern.length();
        StringBuilder sb = new StringBuilder(BLOCK_SIZE + m + 1);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(new FileInputStream(fileName), 64 * 1024),
                        StandardCharsets.UTF_8), BLOCK_SIZE)) {

            char[] buf = new char[BLOCK_SIZE];
            String tail = "";
            long processed = 0;

            int read;
            while ((read = reader.read(buf)) != -1) {
                sb.setLength(0);
                sb.ensureCapacity(pattern.length() + 1 + tail.length() + read);
                sb.append(pattern).append(SEPARATOR).append(tail);
                sb.append(buf, 0, read);

                String s = sb.toString();
                int[] z = ZAlgorithm.zFunction(s);

                int offset = m + 1;
                for (int i = offset; i < s.length(); i++) {
                    if (z[i] >= m) {
                        long globalPos = processed - tail.length() + (i - offset);
                        result.add(globalPos);
                    }
                }

                if (processed > Long.MAX_VALUE - read) {
                    throw new IOException("File too large to process");
                }
                processed += read;

                if (m > 1 && read >= m - 1) {
                    int tailStart = Math.max(0, read - (m - 1));
                    tail = new String(buf, tailStart, read - tailStart);
                } else {
                    tail = "";
                }
            }
        }
        return result;
    }
}
