package auto_verification.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectConfig {

    @Getter @Setter
    private String workDir = "./workDir"; // Куда будем качать репозитории студентов

    @Getter @Setter
    private int semester = 1; // По умолчанию 1 семестр
    private Map<String, Map<String, Double>> bonuses = new HashMap<>();

    @Getter
    private final List<Task> tasks = new ArrayList<>();

    @Getter
    private final List<Group> groups = new ArrayList<>();

    public void addBonus(String studentNickname, String taskId, double points) {
        bonuses.putIfAbsent(studentNickname, new HashMap<>());
        bonuses.get(studentNickname).put(taskId, points);
    }

    public double getBonus(String studentNickname, String taskId) {
        return bonuses.getOrDefault(studentNickname, new HashMap<>()).getOrDefault(taskId, 0.0);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
    public void addGroup(Group group) {
        groups.add(group);
    }
}
