package hash_table.iterator;

import hash_table.HashTable;
import hash_table.core.Entry;
import hash_table.exception.KeyNotFoundException;
import hash_table.exception.EmptyHashTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class HashTableIteratorTest {
    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    void testIteratorOnEmptyTable() {
        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorWithSingleElement() {
        hashTable.put("key1", 100);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertTrue(iterator.hasNext());
        Entry<String, Integer> entry = iterator.next();
        assertEquals("key1", entry.getKey());
        assertEquals(100, entry.getValue());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorWithMultipleElements() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);
        hashTable.put("key3", 300);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        Set<String> foundKeys = new HashSet<>();
        Set<Integer> foundValues = new HashSet<>();

        int count = 0;
        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            foundKeys.add(entry.getKey());
            foundValues.add(entry.getValue());
            count++;
        }

        assertEquals(3, count);
        assertTrue(foundKeys.contains("key1"));
        assertTrue(foundKeys.contains("key2"));
        assertTrue(foundKeys.contains("key3"));
        assertTrue(foundValues.contains(100));
        assertTrue(foundValues.contains(200));
        assertTrue(foundValues.contains(300));

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testConcurrentModificationException() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();

        hashTable.put("key3", 300);

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testConcurrentModificationWithUpdate() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();

        // Обновляем существующий элемент
        hashTable.update("key1", 150);

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testConcurrentModificationWithRemove() throws KeyNotFoundException, EmptyHashTableException {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();

        hashTable.remove("key2");

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testConcurrentModificationWithClear() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();

        hashTable.clear();

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testIteratorAfterResize() {
        for (int i = 0; i < 20; i++) {
            hashTable.put("key" + i, i);
        }

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        Set<String> foundKeys = new HashSet<>();
        int count = 0;

        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            foundKeys.add(entry.getKey());
            count++;
        }

        assertEquals(20, count);
        assertEquals(20, foundKeys.size());

        for (int i = 0; i < 20; i++) {
            assertTrue(foundKeys.contains("key" + i));
        }
    }

    @Test
    void testMultipleIterators() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);

        Iterator<Entry<String, Integer>> iterator1 = hashTable.iterator();
        Iterator<Entry<String, Integer>> iterator2 = hashTable.iterator();

        assertTrue(iterator1.hasNext());
        assertTrue(iterator2.hasNext());

        Entry<String, Integer> entry1 = iterator1.next();
        Entry<String, Integer> entry2 = iterator2.next();

        assertNotNull(entry1);
        assertNotNull(entry2);

        boolean hasNext1 = iterator1.hasNext();
        boolean hasNext2 = iterator2.hasNext();

        assertEquals(hasNext1, hasNext2);
    }

    @Test
    void testIteratorWithNullValues() {
        hashTable.put("key1", null);
        hashTable.put(null, 100);
        hashTable.put("key3", 300);

        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();

        Set<String> foundKeys = new HashSet<>();
        Set<Integer> foundValues = new HashSet<>();

        int count = 0;
        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            foundKeys.add(entry.getKey());
            foundValues.add(entry.getValue());
            count++;
        }

        assertEquals(3, count);
        assertTrue(foundKeys.contains("key1"));
        assertTrue(foundKeys.contains(null));
        assertTrue(foundKeys.contains("key3"));
        assertTrue(foundValues.contains(null));
        assertTrue(foundValues.contains(100));
        assertTrue(foundValues.contains(300));
    }

    @Test
    void testIteratorConsistency() {
        hashTable.put("key1", 100);
        hashTable.put("key2", 200);
        hashTable.put("key3", 300);

        Iterator<Entry<String, Integer>> iterator1 = hashTable.iterator();
        Iterator<Entry<String, Integer>> iterator2 = hashTable.iterator();

        Set<String> keys1 = new HashSet<>();
        Set<String> keys2 = new HashSet<>();

        while (iterator1.hasNext()) {
            keys1.add(iterator1.next().getKey());
        }

        while (iterator2.hasNext()) {
            keys2.add(iterator2.next().getKey());
        }

        assertEquals(keys1, keys2);
        assertEquals(3, keys1.size());
    }
}
