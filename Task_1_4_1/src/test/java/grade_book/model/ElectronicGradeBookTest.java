package grade_book.model;

import grade_book.exception.InvalidSemesterException;
import grade_book.exception.TransferNotAllowedException;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectronicGradeBookTest {

    private ElectronicGradeBook gradeBook;

    @BeforeEach
    void setUp() {
        gradeBook = new ElectronicGradeBook("John Doe", StudyType.PAID, 1);
    }

    @Test
    void addGrade_shouldAddGradeToList_whenNotThesisDefense() throws Exception {
        Grade grade = new Grade("Math", ControlType.EXAM, 5, 1);
        gradeBook.addGrade(grade);

        assertTrue(gradeBook.getGrades().contains(grade));
        assertEquals(1, gradeBook.getGrades().size());
    }

    @Test
    void addGrade_shouldSetThesisGrade_whenThesisDefense() throws Exception {
        Grade thesisGrade = new Grade("Thesis", ControlType.THESIS_DEFENSE, 5, 8);
        gradeBook.addGrade(thesisGrade);

        assertEquals(thesisGrade, gradeBook.getThesisGrade());
        assertEquals(0, gradeBook.getGrades().size());
    }

    @Test
    void addGrade_shouldOverwriteThesisGrade() throws Exception {
        Grade firstThesis = new Grade("Thesis1", ControlType.THESIS_DEFENSE, 4, 8);
        Grade secondThesis = new Grade("Thesis2", ControlType.THESIS_DEFENSE, 5, 8);

        gradeBook.addGrade(firstThesis);
        gradeBook.addGrade(secondThesis);

        assertEquals(secondThesis, gradeBook.getThesisGrade());
    }

    @Test
    void getCurrentAverageScore_shouldReturnZero_whenNoGrades() {
        assertEquals(0.0, gradeBook.getCurrentAverageScore());
    }

    @Test
    void getCurrentAverageScore_shouldCalculateAverage_whenHasGrades() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 3, 1));

        assertEquals(4.0, gradeBook.getCurrentAverageScore());
    }

    @Test
    void getCurrentAverageScore_shouldIgnoreGradesWithZeroSemester() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 2));

        assertEquals(4.5, gradeBook.getCurrentAverageScore());
    }

    @Test
    void getCurrentAverageScore_shouldIgnoreThesisGrade() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 3, 8));

        assertEquals(5.0, gradeBook.getCurrentAverageScore());
    }

    @Test
    void canTransferToBudget_shouldReturnFalse_whenAlreadyBudget() {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);
        assertFalse(budgetBook.canTransferToBudget());
    }

    @Test
    void canTransferToBudget_shouldReturnTrue_whenNoSatisfactoryExamsInLastTwoSessions() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 2));
        gradeBook.addGrade(new Grade("Biology", ControlType.EXAM, 4, 2));

        assertTrue(gradeBook.canTransferToBudget());
    }

    @Test
    void canTransferToBudget_shouldReturnFalse_whenHasSatisfactoryExamInLastTwoSessions() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 3, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 2));

        assertFalse(gradeBook.canTransferToBudget());
    }

    @Test
    void canTransferToBudget_shouldReturnTrue_whenNoExamsInLastTwoSessions() throws Exception {
        gradeBook.addGrade(new Grade("Assignment", ControlType.ASSIGNMENT, 3, 1));
        gradeBook.addGrade(new Grade("Credit", ControlType.CREDIT, 0, 1));

        assertTrue(gradeBook.canTransferToBudget());
    }

    @Test
    void canTransferToBudget_shouldConsiderOnlyExams() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Assignment", ControlType.ASSIGNMENT, 3, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 2));

        assertTrue(gradeBook.canTransferToBudget());
    }

    @Test
    void canGetHonorsDiploma_shouldReturnTrue_whenNoDiplomaGrades() {
        assertTrue(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldReturnFalse_whenHasSatisfactoryGrades() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 1));

        assertFalse(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldReturnTrue_when75PercentExcellentAndExcellentThesis() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Biology", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 5, 8));

        assertTrue(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldReturnFalse_whenLessThan75PercentExcellent() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Biology", ControlType.EXAM, 4, 1));

        assertFalse(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldReturnFalse_whenThesisNotExcellent() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Biology", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 4, 8));

        assertFalse(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldHandleDuplicateSubjects() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 2));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 1));

        assertTrue(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetHonorsDiploma_shouldIgnoreGradesWithZeroScore() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 0, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.DIFFERENTIATED_CREDIT, 5, 1));

        assertTrue(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetIncreasedScholarship_shouldReturnFalse_whenPaidStudy() {
        assertFalse(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    void canGetIncreasedScholarship_shouldReturnFalse_whenNoCurrentSemesterGrades() {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);
        assertFalse(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void canGetIncreasedScholarship_shouldReturnTrue_whenAllCurrentSemesterGradesExcellent() throws Exception {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);
        budgetBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        budgetBook.addGrade(new Grade("Physics", ControlType.DIFFERENTIATED_CREDIT, 5, 1));

        assertTrue(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void canGetIncreasedScholarship_shouldReturnFalse_whenNotAllCurrentSemesterGradesExcellent() throws Exception {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);
        budgetBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        budgetBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 1));

        assertFalse(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void canGetIncreasedScholarship_shouldIgnoreNonExamGrades() throws Exception {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);
        budgetBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        budgetBook.addGrade(new Grade("Assignment", ControlType.ASSIGNMENT, 3, 1));

        assertTrue(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void canGetIncreasedScholarship_shouldIgnoreOtherSemesters() throws Exception {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 2);
        budgetBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));
        budgetBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 2));

        assertTrue(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void transferToBudget_shouldChangeStudyTypeToBudget_whenCanTransfer() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 1));

        gradeBook.transferToBudget();
        assertEquals(StudyType.BUDGET, gradeBook.getStudyType());
    }

    @Test
    void transferToBudget_shouldThrowException_whenCannotTransfer() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));

        TransferNotAllowedException exception = assertThrows(TransferNotAllowedException.class,
            () -> gradeBook.transferToBudget());
        assertEquals("Impossible to transfer to budget", exception.getMessage());
    }

    @Test
    void transferToBudget_shouldNotChangeStudyType_whenExceptionThrown() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));
        StudyType originalType = gradeBook.getStudyType();

        assertThrows(TransferNotAllowedException.class, () -> gradeBook.transferToBudget());
        assertEquals(originalType, gradeBook.getStudyType());
    }

    @Test
    void setCurrentSemester_shouldUpdateCurrentSemester_whenValidSemester() throws Exception {
        gradeBook.setCurrentSemester(3);
        assertEquals(3, gradeBook.getCurrentSemester());
    }

    @Test
    void setCurrentSemester_shouldThrowException_whenInvalidSemester() {
        InvalidSemesterException exception1 = assertThrows(InvalidSemesterException.class,
            () -> gradeBook.setCurrentSemester(0));
        assertEquals("Semester must be positive", exception1.getMessage());

        InvalidSemesterException exception2 = assertThrows(InvalidSemesterException.class,
            () -> gradeBook.setCurrentSemester(-5));
        assertEquals("Semester must be positive", exception2.getMessage());
    }

    @Test
    void setCurrentSemester_shouldNotChangeSemester_whenExceptionThrown() {
        int originalSemester = gradeBook.getCurrentSemester();

        assertThrows(InvalidSemesterException.class, () -> gradeBook.setCurrentSemester(0));
        assertEquals(originalSemester, gradeBook.getCurrentSemester());
    }

    @Test
    void setCurrentSemester_shouldAcceptLargeSemesters() throws Exception {
        gradeBook.setCurrentSemester(10);
        assertEquals(10, gradeBook.getCurrentSemester());
    }

    @Test
    void canTransferToBudget_should_handle_multiple_sessions_correctly() throws Exception {
        gradeBook.setCurrentSemester(4);
        gradeBook.addGrade(new Grade("Math1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics1", ControlType.EXAM, 4, 2));
        gradeBook.addGrade(new Grade("Math2", ControlType.EXAM, 5, 3));
        gradeBook.addGrade(new Grade("Physics2", ControlType.EXAM, 3, 4));

        assertFalse(gradeBook.canTransferToBudget());
    }

    @Test
    void canGetHonorsDiploma_should_ignore_non_diploma_control_types() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Assignment", ControlType.ASSIGNMENT, 3, 1));
        gradeBook.addGrade(new Grade("Control", ControlType.CONTROL_WORK, 2, 1));
        gradeBook.addGrade(new Grade("Colloquium", ControlType.COLLOQUIUM, 5, 1));

        assertTrue(gradeBook.canGetHonorsDiploma());
    }

    @Test
    void canGetIncreasedScholarship_should_consider_only_current_semester() throws Exception {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 3);
        budgetBook.addGrade(new Grade("Math", ControlType.EXAM, 3, 1));
        budgetBook.addGrade(new Grade("Physics", ControlType.EXAM, 4, 2));
        budgetBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 3));
        budgetBook.addGrade(new Grade("Biology", ControlType.DIFFERENTIATED_CREDIT, 5, 3));

        assertTrue(budgetBook.canGetIncreasedScholarship());
    }

    @Test
    void addGrade_should_handle_multiple_thesis_grades() throws Exception {
        Grade thesis1 = new Grade("Bachelor Thesis", ControlType.THESIS_DEFENSE, 4, 8);
        Grade thesis2 = new Grade("Master Thesis", ControlType.THESIS_DEFENSE, 5, 8);

        gradeBook.addGrade(thesis1);
        assertEquals(thesis1, gradeBook.getThesisGrade());

        gradeBook.addGrade(thesis2);
        assertEquals(thesis2, gradeBook.getThesisGrade());
        assertNotEquals(thesis1, gradeBook.getThesisGrade());
    }

    @Test
    void transferToBudget_should_work_when_already_budget() {
        ElectronicGradeBook budgetBook = new ElectronicGradeBook("Jane", StudyType.BUDGET, 1);

        assertThrows(TransferNotAllowedException.class, () -> budgetBook.transferToBudget());
    }

    @Test
    void getCurrentAverageScore_should_handle_empty_grades_list() {
        assertEquals(0.0, gradeBook.getCurrentAverageScore());
    }

    @Test
    void canGetHonorsDiploma_should_handle_exactly_75_percent_excellent() throws Exception {
        gradeBook.addGrade(new Grade("Math", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Physics", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Chemistry", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Biology", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 5, 8));

        assertTrue(gradeBook.canGetHonorsDiploma());
    }
}
