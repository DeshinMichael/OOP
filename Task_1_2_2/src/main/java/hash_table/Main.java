package hash_table;

import hash_table.core.Entry;
import hash_table.exception.EmptyHashTableException;
import hash_table.exception.KeyNotFoundException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> table = new HashTable<>();

        table.put("apple", 5);
        table.put("banana", 3);
        table.put("orange", 8);
        System.out.println("Added fruits: " + table);

        try {
            System.out.println("apple = " + table.get("apple"));
            table.update("apple", 10);
            System.out.println("Updated apple = " + table.get("apple"));
        } catch (KeyNotFoundException | EmptyHashTableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Contains banana: " + table.containsKey("banana"));

        System.out.println("All elements:");
        for (Entry<String, Integer> entry : table) {
            System.out.println("  " + entry);
        }

        try {
            Iterator<Entry<String, Integer>> iterator = table.iterator();
            iterator.next();
            table.put("grape", 12);
            iterator.next();
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException caught");
        }

        HashTable<String, Integer> other = new HashTable<>();
        other.put("apple", 10);
        other.put("banana", 3);
        other.put("orange", 8);
        other.put("grape", 12);
        System.out.println("Tables equal: " + table.equals(other));

        try {
            table.remove("banana");
            System.out.println("After removal: " + table);
        } catch (KeyNotFoundException | EmptyHashTableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
