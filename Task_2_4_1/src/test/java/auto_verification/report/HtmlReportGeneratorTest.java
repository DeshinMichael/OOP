package auto_verification.report;

import auto_verification.model.report.ReportModel;
import auto_verification.logger.AppLogger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HtmlReportGeneratorTest {
    @Test
    void testGenerate() throws IOException {
        AppLogger logger = new AppLogger() {
            @Override public void info(String m) {}
            @Override public void error(String m) {}
        };
        HtmlReportGenerator gen = new HtmlReportGenerator(logger);
        ReportModel model = new ReportModel();
        
        ReportModel.GroupReport gr = new ReportModel.GroupReport();
        gr.setName("Test Group");
        ReportModel.TaskInfo ti = new ReportModel.TaskInfo();
        ti.setId("T1");
        ti.setTitle("Task 1");
        gr.setTasks(Collections.singletonList(ti));
        ReportModel.StudentReport sr = new ReportModel.StudentReport();
        sr.setFullName("John Doe");
        sr.setActivityPercent(100);
        sr.setTotalScore(10.0);
        sr.setFinalMark("5");
        ReportModel.TaskReport tr = new ReportModel.TaskReport();
        tr.setHasCheckResult(true);
        tr.setBuildStatus("+");
        tr.setStyleStatus("+");
        tr.setDocsStatus("+");
        tr.setTestsStatus("1/0/0");
        tr.setScore(5.0);
        tr.setBonus(0.0);
        sr.setTaskReports(Collections.singletonList(tr));
        gr.setStudents(Collections.singletonList(sr));
        model.setGroups(Collections.singletonList(gr));
        
        File reportDir = Files.createTempDirectory("report").toFile();
        File htmlFile = new File(reportDir, "report.html");
        gen.generateReport(model, htmlFile);
        
        assertTrue(htmlFile.exists());
        
        String content = Files.readString(htmlFile.toPath());
        assertTrue(content.contains("Test Group"));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("T1"));
    }
}
