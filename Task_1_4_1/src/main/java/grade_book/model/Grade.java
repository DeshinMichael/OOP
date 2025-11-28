package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;

public class Grade {
    private final String subject;
    private final ControlType controlType;
    private final int score;
    private final int semester;
    private final boolean isRetake;

    public Grade(String subject, ControlType controlType, int score, int semester, boolean isRetake) throws InvalidGradeException, InvalidSemesterException {
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
        this.isRetake = isRetake;
    }

    public Grade(String subject, ControlType controlType, int score, int semester) throws InvalidSemesterException, InvalidGradeException {
        this(subject, controlType, score, semester, false);
    }

    public String getSubject() {
        return subject;
    }
    public ControlType getControlType() {
        return controlType;
    }
    public int getScore() {
        return score;
    }
    public int getSemester() {
        return semester;
    }
    public boolean isRetake() {
        return isRetake;
    }

    public boolean isExcellent() {
        return score == 5;
    }
    public boolean isSatisfactory() {
        return score == 3;
    }
    public boolean isPassed() {
        return score >= 3 || (score == 0 && controlType == ControlType.CREDIT);
    }
}
