package substring_search;

import substring_search.exception.PatternException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SubstringSearcher {
    private static final int BLOCK_SIZE = 1 << 18; // 256KB
    private static final char SEPARATOR = '\u0000';

    public static List<Long> find(String fileName, String pattern) throws IOException, PatternException {
        if (pattern == null || pattern.isEmpty()) {
            throw new PatternException("Pattern must be no empty");
        }

        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        if (!Files.isReadable(filePath)) {
            throw new IOException("File is not readable: " + fileName);
        }

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

                int[] z = zFunction(s);

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

    private static int[] zFunction(String str) {
        int n = str.length();
        int[] z = new int[n];

        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(z[i-l], r-i+1);
            }

            while (i + z[i] < n && str.charAt(z[i]) == str.charAt(i + z[i])) {
                z[i]++;
            }

            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }

        z[0] = n;
        return z;
    }
}
