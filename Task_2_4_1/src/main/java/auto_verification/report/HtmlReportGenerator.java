package auto_verification.report;

import auto_verification.logger.AppLogger;
import auto_verification.model.report.ReportModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HtmlReportGenerator {
    private final AppLogger logger;

    public HtmlReportGenerator(AppLogger logger) {
        this.logger = logger;
    }

    public void generateReport(ReportModel model, File outputFile) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: monospace; padding: 20px; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-bottom: 30px; font-size: 14px; }");
        html.append("th, td { border: 1px solid black; padding: 6px; text-align: center; }");
        html.append(".task-title { background-color: #f8f9fa; font-weight: bold; ")
            .append("text-align: left; padding: 10px; border: 1px solid black; }");
        html.append("</style></head><body>");

        for (ReportModel.GroupReport group : model.getGroups()) {
            html.append("<h3>Группа ").append(group.getName()).append("</h3>");

            generateTaskTables(html, group);
            generateGroupStatisticsTable(html, group);
        }

        html.append("</body></html>");
        Files.writeString(outputFile.toPath(), html.toString());
        logger.info("Report successfully generated: " + outputFile.getAbsolutePath());
    }

    private void generateTaskTables(StringBuilder html, ReportModel.GroupReport group) {
        for (int i = 0; i < group.getTasks().size(); i++) {
            ReportModel.TaskInfo task = group.getTasks().get(i);
            
            html.append("<table>");
            html.append("<tr><th colspan='7' class='task-title'>Лабораторная ")
                    .append(task.getId())
                    .append(" (").append(task.getTitle()).append(")</th></tr>");
            html.append("<tr><th>Студент</th><th>Сборка</th><th>Документация</th>")
                    .append("<th>Style guide</th><th>Тесты</th>")
                    .append("<th>Доп. балл</th><th>Общий балл</th></tr>");

            for (ReportModel.StudentReport student : group.getStudents()) {
                ReportModel.TaskReport res = student.getTaskReports().get(i);

                html.append("<tr>");
                html.append("<td>").append(student.getFullName()).append("</td>");

                if (res.isHasCheckResult()) {
                    html.append("<td>").append(res.getBuildStatus()).append("</td>");
                    html.append("<td>").append(res.getDocsStatus()).append("</td>");
                    html.append("<td>").append(res.getStyleStatus()).append("</td>");
                    html.append("<td>").append(res.getTestsStatus()).append("</td>");
                    html.append("<td>").append(res.getBonus()).append("</td>");
                    html.append("<td>").append(res.getScore()).append("</td>");
                } else {
                    html.append("<td>-</td><td>-</td><td>-</td><td>0/0/0</td><td>0.0</td><td>0.0</td>");
                }
                html.append("</tr>");
            }
            html.append("</table>");
        }
    }

    private void generateGroupStatisticsTable(StringBuilder html, ReportModel.GroupReport group) {
        html.append("<table>");
        html.append("<tr><th colspan='").append(group.getTasks().size() + 4)
            .append("' class='task-title'>Общая статистика группы ").append(group.getName()).append("</th></tr>");
        
        html.append("<tr><th>Студент</th>");
        for (ReportModel.TaskInfo task : group.getTasks()) {
            html.append("<th>").append(task.getId()).append("</th>");
        }
        html.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>");

        for (ReportModel.StudentReport student : group.getStudents()) {
            html.append("<tr>");
            html.append("<td>").append(student.getFullName()).append("</td>");

            for (ReportModel.TaskReport tr : student.getTaskReports()) {
                if (tr.isHasCheckResult()) {
                    html.append("<td>").append(tr.getScore()).append("</td>");
                } else {
                    html.append("<td>0.0</td>");
                }
            }

            html.append("<td>").append(student.getTotalScore()).append("</td>");
            html.append("<td>").append(student.getActivityPercent()).append("%</td>");
            html.append("<td>").append(student.getFinalMark()).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
    }
}