package hash_table.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {
    private Entry<String, Integer> entry;

    @BeforeEach
    void setUp() {
        entry = new Entry<>("key1", 100);
    }

    @Test
    void testSetValue() {
        entry.setValue(200);
        assertEquals(200, entry.getValue());

        entry.setValue(null);
        assertNull(entry.getValue());
    }

    @Test
    void testSetNext() {
        Entry<String, Integer> nextEntry = new Entry<>("key2", 200);

        entry.setNext(nextEntry);
        assertEquals(nextEntry, entry.getNext());

        entry.setNext(null);
        assertNull(entry.getNext());
    }

    @Test
    void testEquals() {
        Entry<String, Integer> sameEntry = new Entry<>("key1", 100);
        Entry<String, Integer> differentKey = new Entry<>("key2", 100);
        Entry<String, Integer> differentValue = new Entry<>("key1", 200);
        Entry<String, Integer> nullKey = new Entry<>(null, 100);
        Entry<String, Integer> nullValue = new Entry<>("key1", null);
        Entry<String, Integer> bothNull = new Entry<>(null, null);

        assertTrue(entry.equals(entry)); // same object
        assertTrue(entry.equals(sameEntry));
        assertFalse(entry.equals(differentKey));
        assertFalse(entry.equals(differentValue));
        assertFalse(entry.equals(nullKey));
        assertFalse(entry.equals(nullValue));
        assertFalse(entry.equals(null));
        assertFalse(entry.equals("string"));

        // Test with null values
        Entry<String, Integer> anotherNullKey = new Entry<>(null, 100);
        Entry<String, Integer> anotherNullValue = new Entry<>("key1", null);
        Entry<String, Integer> anotherBothNull = new Entry<>(null, null);

        assertTrue(nullKey.equals(anotherNullKey));
        assertTrue(nullValue.equals(anotherNullValue));
        assertTrue(bothNull.equals(anotherBothNull));
    }

    @Test
    void testHashCode() {
        Entry<String, Integer> sameEntry = new Entry<>("key1", 100);
        Entry<String, Integer> differentEntry = new Entry<>("key2", 200);
        Entry<String, Integer> nullKeyEntry = new Entry<>(null, 100);
        Entry<String, Integer> nullValueEntry = new Entry<>("key1", null);
        Entry<String, Integer> bothNullEntry = new Entry<>(null, null);

        assertEquals(entry.hashCode(), sameEntry.hashCode());
        assertNotEquals(entry.hashCode(), differentEntry.hashCode());

        // Test consistency
        int hash1 = entry.hashCode();
        int hash2 = entry.hashCode();
        assertEquals(hash1, hash2);

        // Test with null values
        assertNotNull(nullKeyEntry.hashCode());
        assertNotNull(nullValueEntry.hashCode());
        assertNotNull(bothNullEntry.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("key1=100", entry.toString());

        Entry<String, Integer> nullKeyEntry = new Entry<>(null, 100);
        assertEquals("null=100", nullKeyEntry.toString());

        Entry<String, Integer> nullValueEntry = new Entry<>("key1", null);
        assertEquals("key1=null", nullValueEntry.toString());

        Entry<String, Integer> bothNullEntry = new Entry<>(null, null);
        assertEquals("null=null", bothNullEntry.toString());
    }

    @Test
    void testChaining() {
        Entry<String, Integer> first = new Entry<>("first", 1);
        Entry<String, Integer> second = new Entry<>("second", 2);
        Entry<String, Integer> third = new Entry<>("third", 3);

        first.setNext(second);
        second.setNext(third);

        assertEquals(second, first.getNext());
        assertEquals(third, second.getNext());
        assertNull(third.getNext());

        // Test modification of chain
        Entry<String, Integer> newSecond = new Entry<>("newSecond", 22);
        first.setNext(newSecond);

        assertEquals(newSecond, first.getNext());
        assertNull(newSecond.getNext());
    }
}

