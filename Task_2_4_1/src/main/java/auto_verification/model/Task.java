package auto_verification.model;

public class Task {
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

    public String getId() {
        return id;
    }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDir() { return dir == null ? id : dir; }
    public void setDir(String dir) { this.dir = dir; }

    public String getSoftDeadline() {
        return softDeadline;
    }

    public void setSoftDeadline(String softDeadline) {
        this.softDeadline = softDeadline;
    }

    public String getHardDeadline() {
        return hardDeadline;
    }

    public void setHardDeadline(String hardDeadline) {
        this.hardDeadline = hardDeadline;
    }

    public String getBuildCmd() {
        return buildCmd;
    }
    public void setBuildCmd(String buildCmd) {
        this.buildCmd = buildCmd;
    }

    public String getDocsCmd() {
        return docsCmd;
    }
    public void setDocsCmd(String docsCmd) {
        this.docsCmd = docsCmd;
    }

    public String getStyleCmd() {
        return styleCmd;
    }
    public void setStyleCmd(String styleCmd) {
        this.styleCmd = styleCmd;
    }

    public String getTestCmd() {
        return testCmd;
    }
    public void setTestCmd(String testCmd) {
        this.testCmd = testCmd;
    }
}
