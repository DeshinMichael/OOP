package auto_verification.dsl;

import groovy.lang.Closure;
import groovy.lang.Script;
import auto_verification.model.Group;
import auto_verification.model.ProjectConfig;
import auto_verification.model.Student;
import auto_verification.model.Task;

import java.io.File;
import java.io.IOException;

public abstract class DslDelegatingScript extends Script {
    
    private final ProjectConfig config = new ProjectConfig();
    
    public ProjectConfig getConfig() {
        return config;
    }

    // Поддержка импорта других конфигурационных файлов
    public void importConfig(String path) throws IOException {
        System.out.println("Import configuration from: " + path);
        ProjectConfig imported = ConfigParser.parse(new File(path));
        this.config.getTasks().addAll(imported.getTasks());
        this.config.getGroups().addAll(imported.getGroups());
    }

    // Блок настроек "settings { workDir = '...' }"
    public void settings(Closure<?> closure) {
        closure.setDelegate(config);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    // Блок задачи "task('hw1') { branch = 'main'; build = '...'; ... }"
    public void task(String id, Closure<?> closure) {
        Task t = new Task(id);
        closure.setDelegate(t);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
        config.addTask(t);
    }

    // Блок группы "group('201') { student('Name', 'url') }"
    public void group(String name, Closure<?> closure) {
        Group g = new Group(name);
        Object delegate = new Object() {
            public void student(String githubNickname, String fullName, String repoUrl) {
                g.addStudent(new Student(githubNickname, fullName, repoUrl));
            }
        };
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
        config.addGroup(g);
    }

    // Блок бонусов
    public void bonus(String studentNickname, String taskId, double points) {
        config.addBonus(studentNickname, taskId, points);
    }
}
