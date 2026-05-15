package auto_verification.report;

import auto_verification.model.CheckResult;
import auto_verification.model.Group;
import auto_verification.model.PipelineResult;
import auto_verification.model.ProjectConfig;
import auto_verification.model.Student;
import auto_verification.model.Task;
import auto_verification.model.report.ReportModel;
import auto_verification.scoring.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportBuilder {
    private final ScoreCalculator calculator;

    public ReportBuilder(ScoreCalculator calculator) {
        this.calculator = calculator;
    }

    public ReportModel build(ProjectConfig config, PipelineResult data) {
        ReportModel model = new ReportModel();
        List<ReportModel.GroupReport> groupReports = new ArrayList<>();

        for (Group group : config.getGroups()) {
            groupReports.add(buildGroupReport(group, config, data));
        }

        model.setGroups(groupReports);
        return model;
    }

    private ReportModel.GroupReport buildGroupReport(Group group, ProjectConfig config, PipelineResult data) {
        ReportModel.GroupReport groupReport = new ReportModel.GroupReport();
        groupReport.setName(group.getName());
        groupReport.setTasks(buildTaskInfos(config));

        List<ReportModel.StudentReport> studentReports = new ArrayList<>();
        for (Student student : group.getStudents()) {
            studentReports.add(buildStudentReport(student, config, data));
        }
        groupReport.setStudents(studentReports);
        return groupReport;
    }

    private List<ReportModel.TaskInfo> buildTaskInfos(ProjectConfig config) {
        List<ReportModel.TaskInfo> taskInfos = new ArrayList<>();
        for (Task task : config.getTasks()) {
            ReportModel.TaskInfo info = new ReportModel.TaskInfo();
            info.setId(task.getId());
            String taskTitle = task.getTitle() != null && !task.getTitle().isEmpty()
                    ? task.getTitle()
                    : task.getId();
            info.setTitle(taskTitle);
            taskInfos.add(info);
        }
        return taskInfos;
    }

    private ReportModel.StudentReport buildStudentReport(Student student, ProjectConfig config, PipelineResult data) {
        ReportModel.StudentReport studentReport = new ReportModel.StudentReport();
        studentReport.setFullName(student.getFullName());

        Map<Task, CheckResult> studentResults = data.reportData.get(student);
        double totalScore = 0;
        List<ReportModel.TaskReport> taskReports = new ArrayList<>();

        for (Task task : config.getTasks()) {
            double bonus = config.getBonus(student.getGithubNickname(), task.getId());
            CheckResult res = studentResults != null ? studentResults.get(task) : null;

            ReportModel.TaskReport tr = buildTaskReport(task, res, bonus);
            totalScore += tr.getScore();
            taskReports.add(tr);
        }

        studentReport.setTaskReports(taskReports);
        studentReport.setTotalScore(totalScore);

        double activity = data.activities.getOrDefault(student, 0.0);
        studentReport.setActivityPercent((int) (activity * 100));

        studentReport.setFinalMark(calculateFinalMark(config.getSemester(), totalScore));

        return studentReport;
    }

    private ReportModel.TaskReport buildTaskReport(Task task, CheckResult res, double bonus) {
        ReportModel.TaskReport tr = new ReportModel.TaskReport();
        if (res != null) {
            tr.setHasCheckResult(true);
            double score = calculator.calculate(task, res, bonus);
            tr.setScore(score);
            tr.setBonus(bonus);

            tr.setBuildStatus(res.buildOk ? "+" : "-");
            tr.setDocsStatus((task.getDocsCmd() == null || res.docsOk) ? "+" : "-");
            tr.setStyleStatus((task.getStyleCmd() == null || res.styleOk) ? "+" : "-");

            if (task.getTestCmd() == null) {
                tr.setTestsStatus("-");
            } else if (!res.buildOk) {
                tr.setTestsStatus("Пропущены");
            } else {
                if (res.testsTotal == 0 && res.testOk) {
                    tr.setTestsStatus("Успешно");
                } else {
                    int passed = res.testsTotal - res.testsFailed - res.testsSkipped;
                    tr.setTestsStatus(passed + "/" + res.testsFailed + "/" + res.testsSkipped);
                }
            }
        } else {
            tr.setHasCheckResult(false);
            tr.setScore(0.0);
        }
        return tr;
    }

    private String calculateFinalMark(int semester, double totalScore) {
        if (semester == 1) {
            if (totalScore >= 13) return "5";
            if (totalScore >= 10) return "4";
            if (totalScore >= 7) return "3";
            return "2";
        } else if (semester == 2) {
            if (totalScore >= 5) return "5";
            if (totalScore >= 4) return "4";
            if (totalScore >= 3) return "3";
            return "2";
        }
        return "-";
    }
}
