package substring_search.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileValidator {
    private FileValidator() {}

    public static void validateFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        if (!Files.isReadable(filePath)) {
            throw new IOException("File is not readable: " + fileName);
        }
    }
}
