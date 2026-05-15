package auto_verification.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectConfigTest {
    @Test
    void testBonuses() {
        ProjectConfig config = new ProjectConfig();
        config.addBonus("student1", "t1", 1.5);
        assertEquals(1.5, config.getBonus("student1", "t1"));
        assertEquals(0.0, config.getBonus("student1", "t2"));
        assertEquals(0.0, config.getBonus("student2", "t1"));
    }
    @Test
    void testAdders() {
        ProjectConfig config = new ProjectConfig();
        Group g = new Group("Grp");
        config.addGroup(g);
        assertEquals(1, config.getGroups().size());
        Task t = new Task("T1");
        config.addTask(t);
        assertEquals(1, config.getTasks().size());
    }
}

