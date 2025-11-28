package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    @Test
    void constructor_shouldThrowInvalidGradeException_whenScoreIsNegative() {
        assertThrows(InvalidGradeException.class, () ->
            new Grade("Math", ControlType.EXAM, -1, 1));
    }

    @Test
    void constructor_shouldThrowInvalidGradeException_whenScoreIsGreaterThan5() {
        assertThrows(InvalidGradeException.class, () ->
            new Grade("Math", ControlType.EXAM, 6, 1));
    }

    @Test
    void constructor_shouldThrowInvalidSemesterException_whenSemesterIsZero() {
        assertThrows(InvalidSemesterException.class, () ->
            new Grade("Math", ControlType.EXAM, 5, 0));
    }

    @Test
    void constructor_shouldThrowInvalidSemesterException_whenSemesterIsNegative() {
        assertThrows(InvalidSemesterException.class, () ->
            new Grade("Math", ControlType.EXAM, 5, -1));
    }

    @Test
    void constructor_withRetakeFlag_shouldThrowInvalidGradeException_whenScoreIsInvalid() {
        assertThrows(InvalidGradeException.class, () ->
            new Grade("Math", ControlType.EXAM, -1, 1, true));
        assertThrows(InvalidGradeException.class, () ->
            new Grade("Math", ControlType.EXAM, 6, 1, false));
    }

    @Test
    void constructor_withRetakeFlag_shouldThrowInvalidSemesterException_whenSemesterIsInvalid() {
        assertThrows(InvalidSemesterException.class, () ->
            new Grade("Math", ControlType.EXAM, 5, 0, true));
        assertThrows(InvalidSemesterException.class, () ->
            new Grade("Math", ControlType.EXAM, 5, -1, false));
    }

    @Test
    void isExcellent_shouldReturnTrue_whenScoreIs5() throws Exception {
        Grade grade = new Grade("Math", ControlType.EXAM, 5, 1);
        assertTrue(grade.isExcellent());
    }

    @Test
    void isExcellent_shouldReturnFalse_whenScoreIsNot5() throws Exception {
        Grade grade4 = new Grade("Math", ControlType.EXAM, 4, 1);
        Grade grade3 = new Grade("Math", ControlType.EXAM, 3, 1);
        Grade grade2 = new Grade("Math", ControlType.EXAM, 2, 1);
        Grade grade0 = new Grade("Math", ControlType.CREDIT, 0, 1);

        assertFalse(grade4.isExcellent());
        assertFalse(grade3.isExcellent());
        assertFalse(grade2.isExcellent());
        assertFalse(grade0.isExcellent());
    }

    @Test
    void isSatisfactory_shouldReturnTrue_whenScoreIs3() throws Exception {
        Grade grade = new Grade("Math", ControlType.EXAM, 3, 1);
        assertTrue(grade.isSatisfactory());
    }

    @Test
    void isSatisfactory_shouldReturnFalse_whenScoreIsNot3() throws Exception {
        Grade grade5 = new Grade("Math", ControlType.EXAM, 5, 1);
        Grade grade4 = new Grade("Math", ControlType.EXAM, 4, 1);
        Grade grade2 = new Grade("Math", ControlType.EXAM, 2, 1);
        Grade grade0 = new Grade("Math", ControlType.CREDIT, 0, 1);

        assertFalse(grade5.isSatisfactory());
        assertFalse(grade4.isSatisfactory());
        assertFalse(grade2.isSatisfactory());
        assertFalse(grade0.isSatisfactory());
    }

    @Test
    void isPassed_shouldReturnTrue_whenScoreIs3OrHigher() throws Exception {
        Grade grade5 = new Grade("Math", ControlType.EXAM, 5, 1);
        Grade grade4 = new Grade("Math", ControlType.EXAM, 4, 1);
        Grade grade3 = new Grade("Math", ControlType.EXAM, 3, 1);

        assertTrue(grade5.isPassed());
        assertTrue(grade4.isPassed());
        assertTrue(grade3.isPassed());
    }

    @Test
    void isPassed_shouldReturnTrue_whenScoreIs0AndControlTypeIsCredit() throws Exception {
        Grade creditGrade = new Grade("Math", ControlType.CREDIT, 0, 1);
        assertTrue(creditGrade.isPassed());
    }

    @Test
    void isPassed_shouldReturnFalse_whenScoreIs2() throws Exception {
        Grade grade = new Grade("Math", ControlType.EXAM, 2, 1);
        assertFalse(grade.isPassed());
    }

    @Test
    void isPassed_shouldReturnFalse_whenScoreIs0AndControlTypeIsNotCredit() throws Exception {
        Grade examGrade = new Grade("Math", ControlType.EXAM, 0, 1);
        Grade controlGrade = new Grade("Math", ControlType.CONTROL_WORK, 0, 1);

        assertFalse(examGrade.isPassed());
        assertFalse(controlGrade.isPassed());
    }

    @Test
    void isPassed_shouldReturnFalse_whenScoreIs1() throws Exception {
        Grade grade = new Grade("Math", ControlType.EXAM, 1, 1);
        assertFalse(grade.isPassed());
    }

    @Test
    void isPassed_shouldWorkWithAllControlTypes() throws Exception {
        Grade assignment = new Grade("Math", ControlType.ASSIGNMENT, 4, 1);
        Grade colloquium = new Grade("Physics", ControlType.COLLOQUIUM, 3, 1);
        Grade diffCredit = new Grade("Chemistry", ControlType.DIFFERENTIATED_CREDIT, 5, 1);
        Grade practice = new Grade("Practice", ControlType.PRACTICE_DEFENSE, 4, 1);

        assertTrue(assignment.isPassed());
        assertTrue(colloquium.isPassed());
        assertTrue(diffCredit.isPassed());
        assertTrue(practice.isPassed());
    }

    @Test
    void constructor_should_create_valid_grade_with_boundary_values() throws Exception {
        Grade minGrade = new Grade("Subject", ControlType.EXAM, 0, 1);
        Grade maxGrade = new Grade("Subject", ControlType.EXAM, 5, 1);
        Grade maxSemester = new Grade("Subject", ControlType.EXAM, 5, 8);

        assertEquals(0, minGrade.getScore());
        assertEquals(5, maxGrade.getScore());
        assertEquals(8, maxSemester.getSemester());
    }

    @Test
    void constructor_with_retake_should_create_valid_grade() throws Exception {
        Grade retakeGrade = new Grade("Math", ControlType.EXAM, 5, 1, true);
        Grade normalGrade = new Grade("Physics", ControlType.EXAM, 4, 2, false);

        assertTrue(retakeGrade.isRetake());
        assertFalse(normalGrade.isRetake());
    }

    @Test
    void isExcellent_should_handle_edge_cases() throws Exception {
        Grade grade1 = new Grade("Subject", ControlType.EXAM, 1, 1);
        Grade grade0 = new Grade("Subject", ControlType.EXAM, 0, 1);

        assertFalse(grade1.isExcellent());
        assertFalse(grade0.isExcellent());
    }

    @Test
    void isSatisfactory_should_handle_edge_cases() throws Exception {
        Grade grade1 = new Grade("Subject", ControlType.EXAM, 1, 1);
        Grade grade5 = new Grade("Subject", ControlType.EXAM, 5, 1);

        assertFalse(grade1.isSatisfactory());
        assertFalse(grade5.isSatisfactory());
    }
}
