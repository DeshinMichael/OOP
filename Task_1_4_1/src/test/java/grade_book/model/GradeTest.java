package grade_book.model;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.model.enums.ControlType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    @Test
    void constructor_throwsInvalidGradeException() {
        assertThrows(InvalidGradeException.class, () -> new Grade("Sub", ControlType.EXAM, -1, 1));
        assertThrows(InvalidGradeException.class, () -> new Grade("Sub", ControlType.EXAM, 6, 1));
    }

    @Test
    void isExcellent() throws InvalidGradeException, InvalidSemesterException {
        Grade excellentGrade = new Grade("Sub", ControlType.EXAM, 5, 1);
        Grade notExcellentGrade = new Grade("Sub", ControlType.EXAM, 4, 1);

        assertTrue(excellentGrade.isExcellent());
        assertFalse(notExcellentGrade.isExcellent());
    }

    @Test
    void isSatisfactory() throws InvalidGradeException, InvalidSemesterException {
        Grade satisfactoryGrade = new Grade("Sub", ControlType.EXAM, 3, 1);
        Grade notSatisfactoryGrade = new Grade("Sub", ControlType.EXAM, 4, 1);

        assertTrue(satisfactoryGrade.isSatisfactory());
        assertFalse(notSatisfactoryGrade.isSatisfactory());
    }

    @Test
    void isPassed() throws InvalidGradeException, InvalidSemesterException {
        Grade passedExam = new Grade("Sub", ControlType.EXAM, 3, 1);
        Grade failedExam = new Grade("Sub", ControlType.EXAM, 2, 1);
        Grade passedCredit = new Grade("Sub", ControlType.CREDIT, 0, 1);
        Grade failedCredit = new Grade("Sub", ControlType.CREDIT, 1, 1);

        assertTrue(passedExam.isPassed());
        assertFalse(failedExam.isPassed());
        assertTrue(passedCredit.isPassed());
        assertFalse(failedCredit.isPassed());
    }
}
