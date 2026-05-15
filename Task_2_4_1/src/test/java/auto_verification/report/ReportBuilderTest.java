package auto_verification.report;

import auto_verification.model.CheckResult;
import auto_verification.model.Group;
import auto_verification.model.PipelineResult;
import auto_verification.model.ProjectConfig;
import auto_verification.model.Student;
import auto_verification.model.Task;
import auto_verification.model.report.ReportModel;
import auto_verification.scoring.ScoreCalculator;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportBuilderTest {

    @Test
    void testBuildReportModel() {
        ScoreCalculator calc = new ScoreCalculator();
        ReportBuilder builder = new ReportBuilder(calc);
        
        ProjectConfig config = new ProjectConfig();
        config.setSemester(1);
        
        Group g = new Group("Group1");
        Student s = new Student("johndoe", "John Doe", "url");
        g.getStudents().add(s);
        config.getGroups().add(g);
        
        Task t = new Task("T_1");
        t.setTitle("Task 1");
        t.setPoints(2);
        t.setBuildCmd("build");
        config.getTasks().add(t);
        
        Map<Student, Map<Task, CheckResult>> reportData = new ConcurrentHashMap<>();
        Map<Student, Double> activities = new ConcurrentHashMap<>();
        
        PipelineResult result = new PipelineResult(reportData, activities);
        CheckResult cr = new CheckResult();
        cr.buildOk = true;
        cr.testOk = true;
        
        Map<Task, CheckResult> crMap = new ConcurrentHashMap<>();
        crMap.put(t, cr);
        result.reportData.put(s, crMap);
        result.activities.put(s, 0.9);
        
        ReportModel model = builder.build(config, result);
        
        assertNotNull(model);
        assertEquals(1, model.getGroups().size());
        
        ReportModel.GroupReport gr = model.getGroups().get(0);
        assertEquals("Group1", gr.getName());
        assertEquals(1, gr.getTasks().size());
        assertEquals("T_1", gr.getTasks().get(0).getId());
        
        assertEquals(1, gr.getStudents().size());
        ReportModel.StudentReport sr = gr.getStudents().get(0);
        assertEquals("John Doe", sr.getFullName());
        assertEquals(90, sr.getActivityPercent());
        
        assertEquals(1, sr.getTaskReports().size());
        ReportModel.TaskReport tr = sr.getTaskReports().get(0);
        assertTrue(tr.isHasCheckResult());
        assertEquals("+", tr.getBuildStatus());
    }
}
