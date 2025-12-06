package grade_book.model;

import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ElectronicGradeBook {
    private final String studentName;
    @Setter
    private StudyType studyType;
    private int currentSemester;
    private final List<Grade> grades = new ArrayList<>();
    private Grade thesisGrade;

    public ElectronicGradeBook(String studentName, StudyType studyType, int currentSemester) {
        this.studentName = studentName;
        this.studyType = studyType;
        this.currentSemester = currentSemester;
    }

    public void setCurrentSemester(int semester) throws InvalidSemesterException {
        if (semester <= 0) {
            throw new InvalidSemesterException("Semester must be positive");
        }
        this.currentSemester = semester;
    }

    public void addGrade(Grade grade) {
        if (grade.getControlType() == ControlType.THESIS_DEFENSE) {
            this.thesisGrade = grade;
        } else {
            grades.add(grade);
        }
    }
}
