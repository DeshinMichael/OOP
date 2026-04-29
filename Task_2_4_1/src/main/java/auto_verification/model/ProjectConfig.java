package auto_verification.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectConfig {
    private String workDir = "./workDir"; // Куда будем качать репозитории студентов
    private int semester = 1; // По умолчанию 1 семестр
    private Map<String, Map<String, Double>> bonuses = new HashMap<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<Group> groups = new ArrayList<>();

    public String getWorkDir() {
        return workDir;
    }
    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public List<Group> getGroups() {
        return groups;
    }

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
