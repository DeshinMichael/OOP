package auto_verification.scoring;

import auto_verification.model.CheckResult;
import auto_verification.model.Task;

public class ScoreCalculator {

    // + Дополнительные баллы
    public double calculate(Task task, CheckResult result, double bonusPoints) {
        // Если сборка или тесты упали (есть ошибки или пропуски)
        if (!result.buildOk || !result.testOk || result.testsFailed > 0 || result.testsSkipped > 0) {
            return Math.max(0.0, bonusPoints); // Задача не сдана (можно получить только положительный доп бонус)
        }
        // Если всё ок - даём балл + доп. баллы (штрафы)
        return Math.max(0.0, task.getPoints() + bonusPoints);
    }
}
