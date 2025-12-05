package grade_book;

import grade_book.exception.InvalidGradeException;
import grade_book.exception.InvalidSemesterException;
import grade_book.exception.TransferNotAllowedException;
import grade_book.model.ElectronicGradeBook;
import grade_book.model.Grade;
import grade_book.model.enums.ControlType;
import grade_book.model.enums.StudyType;
import grade_book.service.GradeBookService;

public class Main {
    public static void main(String[] args) {
        try {
            ElectronicGradeBook gradeBook =
                    new ElectronicGradeBook("Ivanov Ivan Ivanovich",
                            StudyType.PAID,
                            4);

            gradeBook.addGrade(new Grade("Mathematical Analysis",
                    ControlType.EXAM, 5, 2));
            gradeBook.addGrade(new Grade("Linear Algebra",
                    ControlType.EXAM, 4, 2));
            gradeBook.addGrade(new Grade("Physical Education",
                    ControlType.CREDIT, 0, 2));

            gradeBook.addGrade(new Grade("Probability Theory",
                    ControlType.EXAM, 5, 3));
            gradeBook.addGrade(new Grade("Computer Science",
                    ControlType.DIFFERENTIATED_CREDIT, 5, 3));
            gradeBook.addGrade(new Grade("History",
                    ControlType.CREDIT, 0, 3));

            gradeBook.addGrade(new Grade("Algorithms and Data Structures",
                    ControlType.EXAM, 5, 4));
            gradeBook.addGrade(new Grade("Databases",
                    ControlType.EXAM, 5, 4));
            gradeBook.addGrade(new Grade("Operating Systems",
                    ControlType.DIFFERENTIATED_CREDIT, 5, 4));
            gradeBook.addGrade(new Grade("Physical Education",
                    ControlType.CREDIT, 0, 4));

            gradeBook.addGrade(new Grade("Thesis Defense",
                    ControlType.THESIS_DEFENSE, 5, 8));

            GradeBookService service = new GradeBookService();

            double avg = service.calculateCurrentAverageScore(gradeBook);
            System.out.println("Current average score for all semesters: " + avg);

            boolean canTransfer = service.canTransferToBudget(gradeBook);
            System.out.println("Eligible for transfer to budget-based study: " + canTransfer);

            boolean canHonors = service.canGetHonorsDiploma(gradeBook);
            System.out.println("Eligible for honors diploma: " + canHonors);

            boolean canIncreasedScholarship =
                    service.canGetIncreasedScholarship(gradeBook);
            System.out.println("Eligible for increased scholarship in the current semester: "
                    + canIncreasedScholarship);

            try {
                service.transferToBudget(gradeBook);
                System.out.println("Transfer completed. Current study type: "
                        + gradeBook.getStudyType());
            } catch (TransferNotAllowedException e) {
                System.out.println("Cannot transfer to budget-based study: " + e.getMessage());
            }

        } catch (InvalidGradeException | InvalidSemesterException e) {
            System.err.println("Data error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
