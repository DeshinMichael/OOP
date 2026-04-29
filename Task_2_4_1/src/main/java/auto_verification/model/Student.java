package auto_verification.model;

import java.util.Objects;

public class Student {
    private final String githubNickname;
    private final String fullName;
    private final String repoUrl;

    public Student(String githubNickname, String fullName, String repoUrl) {
        this.githubNickname = githubNickname;
        this.fullName = fullName;
        this.repoUrl = repoUrl;
    }

    public String getGithubNickname() {
        return githubNickname;
    }

    public String getName() {
        return fullName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(githubNickname, student.githubNickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(githubNickname);
    }
}
