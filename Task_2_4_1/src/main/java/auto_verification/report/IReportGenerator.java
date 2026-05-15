package auto_verification.report;

import auto_verification.model.report.ReportModel;

import java.io.File;
import java.io.IOException;

public interface IReportGenerator {
    void generateReport(ReportModel model, File outputFile) throws IOException;
}

