package hash_table.exception;

public class KeyNotFoundException extends Exception {

    public KeyNotFoundException(Object key) {
        super("Key not found: " + key);
    }
}
