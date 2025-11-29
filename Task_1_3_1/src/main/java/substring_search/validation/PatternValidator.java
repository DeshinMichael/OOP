package substring_search.validation;

import substring_search.exception.PatternException;

public class PatternValidator {
    private PatternValidator() {}

    public static void validatePattern(String pattern) throws PatternException {
        if (pattern == null || pattern.isEmpty()) {
            throw new PatternException("Pattern must be no empty");
        }
    }
}
