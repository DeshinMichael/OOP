package hash_table.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashFunctionTest {

    @Test
    void testHashWithNullKey() {
        int result = HashFunction.hash(null, 16);
        assertEquals(0, result);
    }

    @Test
    void testHashWithStringKeys() {
        String key1 = "test";
        String key2 = "another";
        int capacity = 16;

        int hash1 = HashFunction.hash(key1, capacity);
        int hash2 = HashFunction.hash(key2, capacity);

        assertTrue(hash1 >= 0 && hash1 < capacity);
        assertTrue(hash2 >= 0 && hash2 < capacity);

        assertEquals(hash1, HashFunction.hash(key1, capacity));
        assertEquals(hash2, HashFunction.hash(key2, capacity));
    }

    @Test
    void testHashWithIntegerKeys() {
        Integer key1 = 42;
        Integer key2 = -123;
        Integer key3 = 0;
        int capacity = 10;

        int hash1 = HashFunction.hash(key1, capacity);
        int hash2 = HashFunction.hash(key2, capacity);
        int hash3 = HashFunction.hash(key3, capacity);

        assertTrue(hash1 >= 0 && hash1 < capacity);
        assertTrue(hash2 >= 0 && hash2 < capacity);
        assertTrue(hash3 >= 0 && hash3 < capacity);
    }

    @Test
    void testHashWithDifferentCapacities() {
        String key = "test";

        int hash8 = HashFunction.hash(key, 8);
        int hash16 = HashFunction.hash(key, 16);
        int hash32 = HashFunction.hash(key, 32);

        assertTrue(hash8 >= 0 && hash8 < 8);
        assertTrue(hash16 >= 0 && hash16 < 16);
        assertTrue(hash32 >= 0 && hash32 < 32);
    }

    @Test
    void testHashWithNegativeHashCode() {
        Object negativeHashObject = new Object() {
            @Override
            public int hashCode() {
                return -1000;
            }
        };

        int capacity = 16;
        int hash = HashFunction.hash(negativeHashObject, capacity);

        assertTrue(hash >= 0 && hash < capacity);
    }

    @Test
    void testHashWithLargeHashCode() {
        Object largeHashObject = new Object() {
            @Override
            public int hashCode() {
                return Integer.MAX_VALUE;
            }
        };

        int capacity = 7;
        int hash = HashFunction.hash(largeHashObject, capacity);

        assertTrue(hash >= 0 && hash < capacity);
    }

    @Test
    void testHashWithMinIntegerHashCode() {
        Object minHashObject = new Object() {
            @Override
            public int hashCode() {
                return Integer.MIN_VALUE;
            }
        };

        int capacity = 16;
        int hash = HashFunction.hash(minHashObject, capacity);

        assertTrue(hash >= 0 && hash < capacity);
    }

    @Test
    void testHashDistribution() {
        int capacity = 100;
        String[] keys = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};

        int[] hashes = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            hashes[i] = HashFunction.hash(keys[i], capacity);
        }

        boolean allSame = true;
        for (int i = 1; i < hashes.length; i++) {
            if (hashes[i] != hashes[0]) {
                allSame = false;
                break;
            }
        }
        assertFalse(allSame, "Hash function should distribute different keys to different buckets");
    }

    @Test
    void testHashWithCustomObject() {
        CustomKey key1 = new CustomKey("test", 42);
        CustomKey key2 = new CustomKey("test", 42);
        CustomKey key3 = new CustomKey("different", 24);

        int capacity = 16;

        int hash1 = HashFunction.hash(key1, capacity);
        int hash2 = HashFunction.hash(key2, capacity);
        int hash3 = HashFunction.hash(key3, capacity);

        assertTrue(hash1 >= 0 && hash1 < capacity);
        assertTrue(hash2 >= 0 && hash2 < capacity);
        assertTrue(hash3 >= 0 && hash3 < capacity);

        assertEquals(hash1, hash2);
    }

    private static class CustomKey {
        private final String name;
        private final int value;

        CustomKey(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof CustomKey)) return false;
            CustomKey other = (CustomKey) obj;
            return name.equals(other.name) && value == other.value;
        }
    }
}

