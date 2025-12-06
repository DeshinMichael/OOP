package grade_book.service;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.exception.TransferNotAllowedException;
import grade_book.model.ElectronicGradeBook;
import grade_book.model.Grade;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeBookServiceTest {

    private GradeBookService service;
    private ElectronicGradeBook gradeBook;

    @BeforeEach
    void setUp() {
        service = new GradeBookService();
        gradeBook = new ElectronicGradeBook("Student", StudyType.PAID, 3);
    }

    @Test
    void calculateCurrentAverageScore() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Sub3", ControlType.EXAM, 0, 1));
        assertEquals(4.5, service.calculateCurrentAverageScore(gradeBook));
    }

    @Test
    void calculateCurrentAverageScore_noGrades() {
        assertEquals(0.0, service.calculateCurrentAverageScore(gradeBook));
    }

    @Test
    void canTransferToBudget_alreadyBudget() {
        gradeBook.setStudyType(StudyType.BUDGET);
        assertFalse(service.canTransferToBudget(gradeBook));
    }

    @Test
    void canTransferToBudget_canTransfer() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 5, 2));
        assertTrue(service.canTransferToBudget(gradeBook));
    }

    @Test
    void canTransferToBudget_cannotTransfer() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 3, 2));
        assertFalse(service.canTransferToBudget(gradeBook));
    }

    @Test
    void canGetHonorsDiploma_noGrades() {
        assertTrue(service.canGetHonorsDiploma(gradeBook));
    }

    @Test
    void canGetHonorsDiploma_withSatisfactory() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 3, 1));
        assertFalse(service.canGetHonorsDiploma(gradeBook));
    }

    @Test
    void canGetHonorsDiploma_notEnoughExcellent() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Sub3", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Sub4", ControlType.EXAM, 4, 1));
        assertFalse(service.canGetHonorsDiploma(gradeBook));
    }

    @Test
    void canGetHonorsDiploma_badThesis() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub3", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub4", ControlType.EXAM, 4, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 4, 8));
        assertFalse(service.canGetHonorsDiploma(gradeBook));
    }

    @Test
    void canGetHonorsDiploma_canGet() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub3", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub4", ControlType.DIFFERENTIATED_CREDIT, 4, 1));
        gradeBook.addGrade(new Grade("Thesis", ControlType.THESIS_DEFENSE, 5, 8));
        assertTrue(service.canGetHonorsDiploma(gradeBook));
    }

    @Test
    void canGetIncreasedScholarship_paidStudy() {
        assertFalse(service.canGetIncreasedScholarship(gradeBook));
    }

    @Test
    void canGetIncreasedScholarship_noGradesInCurrentSemester() {
        gradeBook.setStudyType(StudyType.BUDGET);
        assertFalse(service.canGetIncreasedScholarship(gradeBook));
    }

    @Test
    void canGetIncreasedScholarship_notAllExcellent() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.setStudyType(StudyType.BUDGET);
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 3));
        gradeBook.addGrade(new Grade("Sub2", ControlType.DIFFERENTIATED_CREDIT, 4, 3));
        assertFalse(service.canGetIncreasedScholarship(gradeBook));
    }

    @Test
    void canGetIncreasedScholarship_canGet() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.setStudyType(StudyType.BUDGET);
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 3));
        gradeBook.addGrade(new Grade("Sub2", ControlType.DIFFERENTIATED_CREDIT, 5, 3));
        assertTrue(service.canGetIncreasedScholarship(gradeBook));
    }

    @Test
    void transferToBudget_allowed() throws TransferNotAllowedException, InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 5, 2));
        service.transferToBudget(gradeBook);
        assertEquals(StudyType.BUDGET, gradeBook.getStudyType());
    }

    @Test
    void transferToBudget_notAllowed() throws InvalidGradeException, InvalidSemesterException {
        gradeBook.addGrade(new Grade("Sub1", ControlType.EXAM, 5, 1));
        gradeBook.addGrade(new Grade("Sub2", ControlType.EXAM, 3, 2));
        assertThrows(TransferNotAllowedException.class, () -> service.transferToBudget(gradeBook));
        assertEquals(StudyType.PAID, gradeBook.getStudyType());
    }
}

