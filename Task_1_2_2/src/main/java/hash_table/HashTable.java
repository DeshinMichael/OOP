package hash_table;

import hash_table.core.Entry;
import hash_table.core.HashFunction;
import hash_table.exception.EmptyHashTableException;
import hash_table.exception.KeyNotFoundException;
import hash_table.iterator.HashTableIterator;
import java.util.*;

public class HashTable<K, V> implements Iterable<Entry<K, V>> {
    private Entry<K, V>[] buckets;
    private int size;
    private int capacity;
    private int modCount = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final double MIN_LOAD_FACTOR = 0.25;

    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = createBuckets(capacity);
        this.size = 0;
    }

    public int getModCount() {
        return modCount;
    }

    public Entry<K, V>[] getBuckets() {
        return buckets;
    }

    public void put(K key, V value) {
        int index = HashFunction.hash(key, capacity);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.setNext(buckets[index]);
        buckets[index] = newEntry;
        size++;
        modCount++;

        if (size > capacity * LOAD_FACTOR) {
            resize();
        }
    }

    public V get(K key) throws KeyNotFoundException, EmptyHashTableException {
        if (isEmpty()) {
            throw new EmptyHashTableException("Cannot get from empty table");
        }

        int index = HashFunction.hash(key, capacity);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        throw new KeyNotFoundException(key);
    }

    public void update(K key, V value) throws KeyNotFoundException, EmptyHashTableException {
        if (isEmpty()) {
            throw new EmptyHashTableException("Cannot update in empty table");
        }

        int index = HashFunction.hash(key, capacity);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                modCount++;
                return;
            }
            current = current.getNext();
        }
        throw new KeyNotFoundException(key);
    }

    public V remove(K key) throws KeyNotFoundException, EmptyHashTableException {
        if (isEmpty()) {
            throw new EmptyHashTableException("Cannot remove from empty table");
        }

        int index = HashFunction.hash(key, capacity);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                if (prev == null) {
                    buckets[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                modCount++;

                checkShrink();
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        throw new KeyNotFoundException(key);
    }

    public boolean containsKey(K key) {
        int index = HashFunction.hash(key, capacity);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.getKey(), key)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        if (capacity > DEFAULT_CAPACITY * 4) {
            capacity = DEFAULT_CAPACITY;
            buckets = createBuckets(capacity);
        } else {
            Arrays.fill(buckets, null);
        }
        size = 0;
        modCount++;
    }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator<>(this, capacity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashTable<K, V> other = (HashTable<K, V>) o;
        if (size != other.size) return false;

        for (Entry<K, V> entry : this) {
            if (!other.containsKey(entry.getKey())) {
                return false;
            }
            try {
                V otherValue = other.get(entry.getKey());
                if (!Objects.equals(entry.getValue(), otherValue)) {
                    return false;
                }
            } catch (KeyNotFoundException | EmptyHashTableException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;

        for (Entry<K, V> entry : this) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.toString());
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }

    private void resize() {
        rehash(capacity * 2);
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] createBuckets(int capacity) {
        return new Entry[capacity];
    }

    private void checkShrink() {
        if (capacity > DEFAULT_CAPACITY && size < capacity * MIN_LOAD_FACTOR) {
            shrink();
        }
    }

    private void shrink() {
        int newCapacity = Math.max(DEFAULT_CAPACITY, capacity / 2);
        if (newCapacity != capacity) {
            rehash(newCapacity);
        }
    }

    private void rehash(int newCapacity) {
        Entry<K, V>[] oldBuckets = buckets;
        int oldCapacity = capacity;

        capacity = newCapacity;
        buckets = createBuckets(capacity);
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> current = oldBuckets[i];
            while (current != null) {
                Entry<K, V> next = current.getNext();
                int newIndex = HashFunction.hash(current.getKey(), capacity);
                current.setNext(buckets[newIndex]);
                buckets[newIndex] = current;
                size++;
                current = next;
            }
        }
        modCount++;
    }
}
