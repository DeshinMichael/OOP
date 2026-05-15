package auto_verification.runner;

import java.io.File;

public interface Runner {
    boolean run(String logPrefix, File projectDir, String commandLine);
}

