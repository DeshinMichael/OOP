package auto_verification.model;

import java.util.Map;

public class PipelineResult {
    public final Map<Student, Map<Task, CheckResult>> reportData;
    public final Map<Student, Double> activities;

    public PipelineResult(Map<Student, Map<Task, CheckResult>> reportData, Map<Student, Double> activities) {
        this.reportData = reportData;
        this.activities = activities;
    }
}

