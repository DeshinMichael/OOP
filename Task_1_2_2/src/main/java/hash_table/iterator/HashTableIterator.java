package hash_table.iterator;

import hash_table.HashTable;
import hash_table.core.Entry;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTableIterator<K, V> implements Iterator<Entry<K, V>> {
    private final Entry<K, V>[] buckets;
    private final int capacity;
    private final int expectedModCount;
    private final HashTable<K, V> table;
    private int bucketIndex = 0;
    private Entry<K, V> current = null;

    public HashTableIterator(HashTable<K, V> table, int capacity) {
        this.table = table;
        this.buckets = table.getBuckets();
        this.capacity = capacity;
        this.expectedModCount = table.getModCount();
        this.current = findNextEntry();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Entry<K, V> next() {
        if (expectedModCount != table.getModCount()) {
            throw new ConcurrentModificationException();
        }

        if (current == null) {
            throw new NoSuchElementException("No more elements in iterator");
        }

        Entry<K, V> result = current;

        if (current.getNext() != null) {
            current = current.getNext();
        } else {
            bucketIndex++;
            current = findNextEntry();
        }

        return result;
    }

    private Entry<K, V> findNextEntry() {
        while (bucketIndex < capacity) {
            if (buckets[bucketIndex] != null) {
                return buckets[bucketIndex];
            }
            bucketIndex++;
        }
        return null;
    }
}
