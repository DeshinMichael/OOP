package auto_verification.dsl;

import auto_verification.model.ProjectConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigParserTest {

    @Test
    void testParseValidConfig(@TempDir Path tempDir) throws IOException {
        String baseDslContent = "group('grp1') {\n" +
                "    student('stud1', 'Student One', 'testRepo')\n" +
                "}\n" +
                "task('T_1') {\n" +
                "    branch = 'main'\n" +
                "    points = 1\n" +
                "}";

        File tempFile = tempDir.resolve("config.groovy").toFile();
        Files.writeString(tempFile.toPath(), baseDslContent);

        ProjectConfig config = ConfigParser.parse(tempFile);
        assertNotNull(config);
        assertEquals(1, config.getGroups().size());
        assertEquals("stud1", config.getGroups().getFirst().getStudents().getFirst().getGithubNickname());
        assertEquals(1, config.getTasks().size());
        assertEquals(1, config.getTasks().getFirst().getPoints());
    }
}
