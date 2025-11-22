package hash_table.core;

public class HashFunction {
    public static <K> int hash(K key, int capacity) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % capacity;
    }
}
