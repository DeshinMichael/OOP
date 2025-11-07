package hash_table;

import hash_table.core.Entry;
import hash_table.exception.EmptyHashTableException;
import hash_table.exception.KeyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {
    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    void testPutAndGet() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        assertEquals(100, hashTable.get("key1"));
        assertEquals(200, hashTable.get("key2"));
        assertEquals(2, hashTable.getSize());
    }

    @Test
    void testPutUpdateExistingKey() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.put("key1", 150);

        assertEquals(150, hashTable.get("key1"));
        assertEquals(1, hashTable.getSize());
    }

    @Test
    void testPutNullKey() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put(null, 100);
        assertEquals(100, hashTable.get(null));
    }

    @Test
    void testGetFromEmptyTable() {
        assertThrows(EmptyHashTableException.class, () -> hashTable.get("key1"));
    }

    @Test
    void testGetNonExistentKey() {
        hashTable.put("key1", 100);
        assertThrows(KeyNotFoundException.class, () -> hashTable.get("key2"));
    }

    @Test
    void testUpdate() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.update("key1", 200);

        assertEquals(200, hashTable.get("key1"));
    }

    @Test
    void testUpdateEmptyTable() {
        assertThrows(EmptyHashTableException.class, () -> hashTable.update("key1", 100));
    }

    @Test
    void testUpdateNonExistentKey() {
        hashTable.put("key1", 100);
        assertThrows(KeyNotFoundException.class, () -> hashTable.update("key2", 200));
    }

    @Test
    void testRemove() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Integer removed = hashTable.remove("key1");

        assertEquals(100, removed);
        assertEquals(1, hashTable.getSize());
        assertFalse(hashTable.containsKey("key1"));
    }

    @Test
    void testRemoveFromEmptyTable() {
        assertThrows(EmptyHashTableException.class, () -> hashTable.remove("key1"));
    }

    @Test
    void testRemoveNonExistentKey() {
        hashTable.put("key1", 100);
        assertThrows(KeyNotFoundException.class, () -> hashTable.remove("key2"));
    }

    @Test
    void testContainsKey() {
        hashTable.put("key1", 100);

        assertTrue(hashTable.containsKey("key1"));
        assertFalse(hashTable.containsKey("key2"));
        assertFalse(hashTable.containsKey(null));

        hashTable.put(null, 200);
        assertTrue(hashTable.containsKey(null));
    }

    @Test
    void testIsEmpty() {
        assertTrue(hashTable.isEmpty());

        hashTable.put("key1", 100);
        assertFalse(hashTable.isEmpty());

        hashTable.clear();
        assertTrue(hashTable.isEmpty());
    }

    @Test
    void testClear() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        hashTable.clear();

        assertTrue(hashTable.isEmpty());
        assertEquals(0, hashTable.getSize());
    }

    @Test
    void testResize() throws KeyNotFoundException, EmptyHashTableException {
        for (int i = 0; i < 20; i++) {
            hashTable.put("key" + i, i);
        }

        assertEquals(20, hashTable.getSize());

        for (int i = 0; i < 20; i++) {
            assertEquals(i, hashTable.get("key" + i));
        }
    }

    @Test
    void testShrink() throws KeyNotFoundException, EmptyHashTableException {
        for (int i = 0; i < 20; i++) {
            hashTable.put("key" + i, i);
        }

        for (int i = 0; i < 18; i++) {
            hashTable.remove("key" + i);
        }

        assertEquals(2, hashTable.getSize());
        assertTrue(hashTable.containsKey("key18"));
        assertTrue(hashTable.containsKey("key19"));
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> other = new HashTable<>();

        assertTrue(hashTable.equals(other));

        hashTable.put("key1", 100);
        assertFalse(hashTable.equals(other));

        other.put("key1", 100);
        assertTrue(hashTable.equals(other));

        other.put("key1", 200);
        assertFalse(hashTable.equals(other));

        assertFalse(hashTable.equals(null));
        assertFalse(hashTable.equals("string"));
    }

    @Test
    void testToString() {
        assertTrue(hashTable.toString().equals("{}"));

        hashTable.put("key1", 100);
        String result = hashTable.toString();
        assertTrue(result.contains("key1=100"));
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void testIterator() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);
        hashTable.put("key3", 300);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();
        int count = 0;

        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }

        assertEquals(3, count);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testCollisionHandling() throws KeyNotFoundException, EmptyHashTableException {
        HashTable<TestKey, String> table = new HashTable<>();
        TestKey key1 = new TestKey("a", 1);
        TestKey key2 = new TestKey("b", 1);

        table.put(key1, "value1");
        table.put(key2, "value2");

        assertEquals("value1", table.get(key1));
        assertEquals("value2", table.get(key2));
        assertEquals(2, table.getSize());
    }

    private static class TestKey {
        private final String name;
        private final int hash;

        TestKey(String name, int hash) {
            this.name = name;
            this.hash = hash;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestKey)) return false;
            TestKey other = (TestKey) obj;
            return name.equals(other.name);
        }
    }
}
