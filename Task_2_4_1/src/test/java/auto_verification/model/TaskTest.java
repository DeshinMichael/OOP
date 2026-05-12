package auto_verification.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    void testGetDir() {
        Task t = new Task("T_1");
        assertEquals("T_1", t.getDir());
        t.setDir("Custom_Dir");
        assertEquals("Custom_Dir", t.getDir());
    }
}
