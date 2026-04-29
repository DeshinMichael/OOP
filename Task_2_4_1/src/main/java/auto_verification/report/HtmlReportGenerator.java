package auto_verification.report;

import auto_verification.model.CheckResult;
import auto_verification.model.ProjectConfig;
import auto_verification.model.Student;
import auto_verification.model.Task;
import auto_verification.model.PipelineResult;
import auto_verification.scoring.ScoreCalculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class HtmlReportGenerator {
    private final ScoreCalculator calculator;

    public HtmlReportGenerator(ScoreCalculator calculator) {
        this.calculator = calculator;
    }

    public void generateReport(ProjectConfig config, PipelineResult data, File outputFile) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: monospace; padding: 20px; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-bottom: 30px; font-size: 14px; }");
        html.append("th, td { border: 1px solid black; padding: 6px; text-align: center; }");
        html.append(".task-title { background-color: #f8f9fa; font-weight: bold; text-align: left; padding: 10px; border: 1px solid black; }");
        html.append("</style></head><body>");

        for (auto_verification.model.Group group : config.getGroups()) {
            html.append("<h3>Группа ").append(group.getName()).append("</h3>");

            generateTaskTables(html, config, group, data);
            generateGroupStatisticsTable(html, config, group, data);
        }

        html.append("</body></html>");
        Files.writeString(outputFile.toPath(), html.toString());
        System.out.println("Report successfully generated: " + outputFile.getAbsolutePath());
    }

    private void generateTaskTables(StringBuilder html, ProjectConfig config, auto_verification.model.Group group, PipelineResult data) {
        for (Task task : config.getTasks()) {
            String taskName = task.getTitle() != null && !task.getTitle().isEmpty() ? task.getTitle() : task.getId();
            
            html.append("<table>");
            html.append("<tr><th colspan='7' class='task-title'>Лабораторная ").append(task.getId()).append(" (").append(taskName).append(")</th></tr>");
            html.append("<tr><th>Студент</th><th>Сборка</th><th>Документация</th><th>Style guide</th><th>Тесты</th><th>Доп. балл</th><th>Общий балл</th></tr>");

            for (Student student : group.getStudents()) {
                double bonus = config.getBonus(student.getGithubNickname(), task.getId());
                Map<Task, CheckResult> studentResults = data.reportData.get(student);
                CheckResult res = studentResults != null ? studentResults.get(task) : null;

                html.append("<tr>");
                html.append("<td>").append(student.getName()).append("</td>");

                if (res != null) {
                    double score = calculator.calculate(task, res, bonus);

                    html.append("<td>").append(res.buildOk ? "+" : "-").append("</td>");
                    html.append("<td>").append((task.getDocsCmd() == null || res.docsOk) ? "+" : "-").append("</td>");
                    html.append("<td>").append((task.getStyleCmd() == null || res.styleOk) ? "+" : "-").append("</td>");
                    
                    if (task.getTestCmd() == null) {
                        html.append("<td>-</td>");
                    } else if (!res.buildOk) {
                        html.append("<td>Пропущены</td>");
                    } else {
                        if (res.testsTotal == 0 && res.testOk) {
                            html.append("<td>Успешно</td>");
                        } else {
                            int passed = res.testsTotal - res.testsFailed - res.testsSkipped;
                            html.append("<td>").append(passed).append("/").append(res.testsFailed).append("/").append(res.testsSkipped).append("</td>");
                        }
                    }
                    
                    html.append("<td>").append(bonus).append("</td>");
                    html.append("<td>").append(score).append("</td>");
                } else {
                    html.append("<td>-</td><td>-</td><td>-</td><td>0/0/0</td><td>0.0</td><td>0.0</td>");
                }
                html.append("</tr>");
            }
            html.append("</table>");
        }
    }

    private void generateGroupStatisticsTable(StringBuilder html, ProjectConfig config, auto_verification.model.Group group, PipelineResult data) {
        html.append("<table>");
        html.append("<tr><th colspan='").append(config.getTasks().size() + 4)
            .append("' class='task-title'>Общая статистика группы ").append(group.getName()).append("</th></tr>");
        
        html.append("<tr><th>Студент</th>");
        for (Task task : config.getTasks()) {
            html.append("<th>").append(task.getId()).append("</th>");
        }
        html.append("<th>Сумма</th><th>Активность</th><th>Оценка</th></tr>");

        for (Student student : group.getStudents()) {
            html.append("<tr>");
            html.append("<td>").append(student.getName()).append("</td>");

            Map<Task, CheckResult> studentResults = data.reportData.get(student);
            double totalScore = 0;

            for (Task task : config.getTasks()) {
                double bonus = config.getBonus(student.getGithubNickname(), task.getId());
                CheckResult res = studentResults != null ? studentResults.get(task) : null;
                if (res != null) {
                    double score = calculator.calculate(task, res, bonus);
                    totalScore += score;
                    html.append("<td>").append(score).append("</td>");
                } else {
                    html.append("<td>0.0</td>");
                }
            }

            html.append("<td>").append(totalScore).append("</td>");
            
            double activity = data.activities.getOrDefault(student, 0.0);
            html.append("<td>").append((int) (activity * 100)).append("%</td>");

            String finalMark = "-";
            if (config.getSemester() == 1) {
                if (totalScore >= 13) finalMark = "5";
                else if (totalScore >= 10) finalMark = "4";
                else if (totalScore >= 7) finalMark = "3";
                else finalMark = "2";
            } else if (config.getSemester() == 2) {
                if (totalScore >= 5) finalMark = "5";
                else if (totalScore >= 4) finalMark = "4";
                else if (totalScore >= 3) finalMark = "3";
                else finalMark = "2";
            }

            html.append("<td>").append(finalMark).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
    }
}