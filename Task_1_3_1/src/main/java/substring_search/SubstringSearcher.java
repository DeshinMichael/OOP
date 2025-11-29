package substring_search;

import substring_search.exception.PatternException;
import substring_search.io.FileBlockReader;
import substring_search.validation.FileValidator;
import substring_search.validation.PatternValidator;

import java.io.IOException;
import java.util.List;

public class SubstringSearcher {
    private SubstringSearcher() {}

    public static List<Long> find(String fileName, String pattern) throws IOException, PatternException {
        PatternValidator.validatePattern(pattern);
        FileValidator.validateFile(fileName);
        return FileBlockReader.searchPattern(fileName, pattern);
    }
}
