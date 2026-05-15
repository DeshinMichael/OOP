package auto_verification.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StudentTest {
    @Test
    void testEqualsAndHashCode() {
        Student s1 = new Student("nick1", "Name 1", "url1");
        Student s2 = new Student("nick1", "Name 2", "url2");
        Student s3 = new Student("nick3", "Name 1", "url1");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertNotEquals(s1, null);
        assertNotEquals(s1, new Object());
    }
}

