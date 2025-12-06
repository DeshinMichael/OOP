package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;
import lombok.Getter;

@Getter
public class Grade {
    private final String subject;
    private final ControlType controlType;
    private final int score;
    private final int semester;

    public Grade(String subject, ControlType controlType, int score, int semester) throws InvalidGradeException, InvalidSemesterException {
        if (score < 0 || score > 5) {
            throw new InvalidGradeException("Grade must be from 0 to 5");
        }
        if (semester <= 0) {
            throw new InvalidSemesterException("Semester must be positive");
        }

        this.subject = subject;
        this.controlType = controlType;
        this.score = score;
        this.semester = semester;
    }

    public boolean isExcellent() {
        return score == 5;
    }
    public boolean isSatisfactory() {
        return score == 3;
    }
    public boolean isPassed() {
        if (controlType == ControlType.CREDIT) {
            return score == 0;
        }
        return score >= 3;
    }
}
