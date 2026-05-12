package auto_verification.model.report;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportModel {
    private List<GroupReport> groups;

    @Getter @Setter
    public static class GroupReport {
        private String name;
        private List<TaskInfo> tasks;
        private List<StudentReport> students;
    }

    @Getter @Setter
    public static class TaskInfo {
        private String id;
        private String title;
    }

    @Getter @Setter
    public static class StudentReport {
        private String fullName;
        private List<TaskReport> taskReports;
        private double totalScore;
        private int activityPercent;
        private String finalMark;
    }

    @Getter @Setter
    public static class TaskReport {
        private String buildStatus;
        private String docsStatus;
        private String styleStatus;
        private String testsStatus;
        private double bonus;
        private double score;
        private boolean hasCheckResult;
    }
}

