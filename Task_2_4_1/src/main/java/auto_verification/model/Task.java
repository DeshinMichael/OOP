package auto_verification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    @Setter(lombok.AccessLevel.NONE)
    private final String id;
    private String branch = "main";
    private int points = 0;

    private String title = "";
    private String dir; // Папка задачи внутри репозитория
    private String softDeadline = "";
    private String hardDeadline = "";

    // Команды
    private String buildCmd;
    private String docsCmd;
    private String styleCmd;
    private String testCmd;
    
    public Task(String id) {
        this.id = id;
    }

    public String getDir() { return dir == null ? id : dir; }
}
