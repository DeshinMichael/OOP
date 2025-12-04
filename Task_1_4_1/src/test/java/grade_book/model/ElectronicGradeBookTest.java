package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicGradeBookTest {

    private ElectronicGradeBook gradeBook;

    @BeforeEach
    void setUp() {
        gradeBook = new ElectronicGradeBook("Student", StudyType.BUDGET, 1);
    }

    @Test
    void setCurrentSemester_shouldSetSemester_whenPositive() throws InvalidSemesterException {
        gradeBook.setCurrentSemester(2);
        assertEquals(2, gradeBook.getCurrentSemester());
    }

    @Test
    void setCurrentSemester_shouldThrowException_whenNotPositive() {
        assertThrows(InvalidSemesterException.class, () -> gradeBook.setCurrentSemester(0));
        assertThrows(InvalidSemesterException.class, () -> gradeBook.setCurrentSemester(-1));
    }

    @Test
    void addGrade_shouldAddRegularGrade() throws InvalidGradeException, InvalidSemesterException {
        Grade regularGrade = new Grade("Math", ControlType.EXAM, 5, 1);
        gradeBook.addGrade(regularGrade);
        assertTrue(gradeBook.getGrades().contains(regularGrade));
        assertNull(gradeBook.getThesisGrade());
    }

    @Test
    void addGrade_shouldSetThesisGrade() throws InvalidGradeException, InvalidSemesterException {
        Grade thesisGrade = new Grade("Thesis", ControlType.THESIS_DEFENSE, 5, 8);
        gradeBook.addGrade(thesisGrade);
        assertEquals(thesisGrade, gradeBook.getThesisGrade());
        assertFalse(gradeBook.getGrades().contains(thesisGrade));
    }
}

