package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.exception.TransferNotAllowedException;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;

import java.util.*;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

public class ElectronicGradeBook {
    private final String studentName;
    private StudyType studyType;
    private int currentSemester;
    private final List<Grade> grades;
    private Grade thesisGrade;

    public ElectronicGradeBook(String studentName, StudyType studyType, int currentSemester) {
        this.studentName = studentName;
        this.studyType = studyType;
        this.currentSemester = currentSemester;
        this.grades = new ArrayList<>();
    }

    public ElectronicGradeBook(String studentName, StudyType studyType) {
        this(studentName, studyType, 1);
    }

    public String getStudentName() {
        return studentName;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Grade getThesisGrade() {
        return thesisGrade;
    }

    public void addGrade(Grade grade) {
        if (grade.getControlType() == ControlType.THESIS_DEFENSE) {
            this.thesisGrade = grade;
        } else {
            grades.add(grade);
        }
    }

    public double getCurrentAverageScore() {
        return grades.stream()
                .filter(grade -> grade.getSemester() > 0)
                .mapToInt(Grade::getScore)
                .average()
                .orElse(0.0);
    }

    public boolean canTransferToBudget() {
        if (studyType == StudyType.BUDGET) return false;

        List<Grade> lastTwoSessions = getLastTwoExamSessions();
        return lastTwoSessions.stream()
                .filter(grade -> grade.getControlType() == ControlType.EXAM)
                .noneMatch(Grade::isSatisfactory);
    }

    public boolean canGetHonorsDiploma() {
        List<Grade> diplomaGrades = getFinalGradesForDiploma();
        if (diplomaGrades.isEmpty()) return true;

        boolean hasSatisfactoryGrades = diplomaGrades.stream()
                .anyMatch(Grade::isSatisfactory);
        if (hasSatisfactoryGrades) return false;

        long excellentCount = diplomaGrades.stream()
                .mapToLong(grade -> grade.isExcellent() ? 1 : 0)
                .sum();
        double excellentPercentage = (double) excellentCount / diplomaGrades.size();
        boolean hasEnoughExcellent = excellentPercentage >= 0.75;

        boolean thesisExcellent = thesisGrade == null || thesisGrade.isExcellent();

        return hasEnoughExcellent && thesisExcellent;
    }

    public boolean canGetIncreasedScholarship() {
        if (studyType == StudyType.PAID) return false;

        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.getSemester() == currentSemester)
                .filter(grade -> grade.getControlType() == ControlType.EXAM ||
                        grade.getControlType() == ControlType.DIFFERENTIATED_CREDIT)
                .toList();

        return !currentSemesterGrades.isEmpty() &&
                currentSemesterGrades.stream().allMatch(Grade::isExcellent);
    }

    public void transferToBudget() throws TransferNotAllowedException {
        if (canTransferToBudget()) {
            studyType = StudyType.BUDGET;
        } else {
            throw new TransferNotAllowedException("Impossible to transfer to budget");
        }
    }

    public void setCurrentSemester(int semester) throws InvalidSemesterException {
        if (semester <= 0) {
            throw new InvalidSemesterException("Semester must be positive");
        }
        this.currentSemester = semester;
    }

    private List<Grade> getLastTwoExamSessions() {
        return grades.stream()
                .filter(grade -> grade.getControlType() == ControlType.EXAM)
                .collect(groupingBy(Grade::getSemester))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, List<Grade>>comparingByKey().reversed())
                .limit(2)
                .flatMap(entry -> entry.getValue().stream())
                .collect(toList());
    }

    private List<Grade> getFinalGradesForDiploma() {
        return grades.stream()
                .filter(grade -> grade.getControlType() == ControlType.EXAM ||
                        grade.getControlType() == ControlType.DIFFERENTIATED_CREDIT)
                .filter(grade -> grade.getScore() > 0)
                .collect(groupingBy(grade -> grade.getSubject() + "-" + grade.getControlType()))
                .values().stream()
                .map(gradeList -> gradeList.stream()
                        .max(comparingInt(Grade::getSemester))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(toList());
    }
}
