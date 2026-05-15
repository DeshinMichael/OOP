package auto_verification.model;

public class CheckResult {
    public boolean buildOk = false;
    public boolean docsOk = false;
    public boolean styleOk = false;
    public boolean testOk = false;

    public int testsTotal = 0;
    public int testsFailed = 0;
    public int testsSkipped = 0;

    public CheckResult() {}

    public CheckResult(boolean buildOk, boolean docsOk, boolean styleOk, boolean testOk) {
        this.buildOk = buildOk;
        this.docsOk = docsOk;
        this.styleOk = styleOk;
        this.testOk = testOk;
    }
}
