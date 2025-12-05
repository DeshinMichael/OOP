package grade_book.service;

import grade_book.exception.TransferNotAllowedException;
import grade_book.model.ElectronicGradeBook;
import grade_book.model.Grade;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class GradeBookService {

    public double calculateCurrentAverageScore(ElectronicGradeBook gradeBook) {
        return gradeBook.getGrades().stream()
                .filter(grade -> grade.getScore() > 0)
                .mapToInt(Grade::getScore)
                .average()
                .orElse(0.0);
    }

    public boolean canTransferToBudget(ElectronicGradeBook gradeBook) {
        if (gradeBook.getStudyType() == StudyType.BUDGET) {
            return false;
        }

        List<Grade> lastTwoSessions = getLastTwoExamSessions(gradeBook);

        boolean hasFailed = lastTwoSessions.stream()
                .anyMatch(grade -> !grade.isPassed());
        if (hasFailed) {
            return false;
        }

        boolean hasSatisfactoryExam = lastTwoSessions.stream()
                .filter(grade -> grade.getControlType() == ControlType.EXAM)
                .anyMatch(Grade::isSatisfactory);

        return !hasSatisfactoryExam;
    }

    public boolean canGetHonorsDiploma(ElectronicGradeBook gradeBook) {
        List<Grade> diplomaGrades = getFinalGradesForDiploma(gradeBook);
        if (diplomaGrades.isEmpty()) {
            return true;
        }

        boolean hasSatisfactoryGrades = diplomaGrades.stream()
                .anyMatch(Grade::isSatisfactory);
        if (hasSatisfactoryGrades) {
            return false;
        }

        long excellentCount = diplomaGrades.stream()
                .filter(Grade::isExcellent)
                .count();
        double excellentPercentage = (double) excellentCount / diplomaGrades.size();
        boolean hasEnoughExcellent = excellentPercentage >= 0.75;

        boolean thesisExcellent = gradeBook.getThesisGrade() == null || gradeBook.getThesisGrade().isExcellent();

        return hasEnoughExcellent && thesisExcellent;
    }

    public boolean canGetIncreasedScholarship(ElectronicGradeBook gradeBook) {
        if (gradeBook.getStudyType() == StudyType.PAID) {
            return false;
        }

        List<Grade> currentSemesterGrades = gradeBook.getGrades().stream()
                .filter(grade -> grade.getSemester() == gradeBook.getCurrentSemester())
                .filter(grade -> grade.getControlType() == ControlType.EXAM ||
                        grade.getControlType() == ControlType.DIFFERENTIATED_CREDIT)
                .toList();

        return !currentSemesterGrades.isEmpty() &&
                currentSemesterGrades.stream().allMatch(Grade::isExcellent);
    }

    public void transferToBudget(ElectronicGradeBook gradeBook) throws TransferNotAllowedException {
        if (canTransferToBudget(gradeBook)) {
            gradeBook.setStudyType(StudyType.BUDGET);
        } else {
            throw new TransferNotAllowedException("Impossible to transfer to budget");
        }
    }

    private List<Grade> getLastTwoExamSessions(ElectronicGradeBook gradeBook) {
        return gradeBook.getGrades().stream()
                .filter(grade -> grade.getControlType() == ControlType.EXAM
                        || grade.getControlType() == ControlType.DIFFERENTIATED_CREDIT)
                .collect(groupingBy(Grade::getSemester))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, List<Grade>>comparingByKey().reversed())
                .limit(2)
                .flatMap(entry -> entry.getValue().stream())
                .collect(toList());
    }

    private List<Grade> getFinalGradesForDiploma(ElectronicGradeBook gradeBook) {
        return gradeBook.getGrades().stream()
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
