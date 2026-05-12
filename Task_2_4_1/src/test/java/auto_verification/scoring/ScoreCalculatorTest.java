package auto_verification.scoring;
import auto_verification.model.CheckResult;
import auto_verification.model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
class ScoreCalculatorTest {
    @Test
    void testCalculate_BuildFailed() {
        ScoreCalculator calc = new ScoreCalculator();
        Task t = new Task("t1");
        t.setPoints(10);
        CheckResult cr = new CheckResult();
        cr.buildOk = false;
        assertEquals(2.0, calc.calculate(t, cr, 2.0));
        assertEquals(0.0, calc.calculate(t, cr, -2.0));
    }
    @Test
    void testCalculate_AllOk() {
        ScoreCalculator calc = new ScoreCalculator();
        Task t = new Task("t1");
        t.setPoints(10);
        CheckResult cr = new CheckResult();
        cr.buildOk = true;
        cr.testOk = true;
        cr.testsFailed = 0;
        cr.testsSkipped = 0;
        assertEquals(12.0, calc.calculate(t, cr, 2.0));
        assertEquals(8.0, calc.calculate(t, cr, -2.0));
        assertEquals(0.0, calc.calculate(t, cr, -12.0));
    }
}

