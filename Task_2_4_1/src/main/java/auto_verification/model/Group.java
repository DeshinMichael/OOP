package auto_verification.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Group {
    private final String name;
    private final List<Student> students = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public void addStudent(Student student) {
        students.add(student);
    }
}
