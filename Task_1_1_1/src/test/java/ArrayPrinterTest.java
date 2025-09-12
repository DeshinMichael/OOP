import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ArrayPrinter.
 */
public class ArrayPrinterTest {

    @Test
    public void testPrintArrayWithRegularArray() {
        int[] arr = {1, 2, 3, 4, 5};
        assertDoesNotThrow(() -> ArrayPrinter.printArray(arr));
    }

    @Test
    public void testPrintArrayWithEmptyArray() {
        int[] arr = {};
        assertDoesNotThrow(() -> ArrayPrinter.printArray(arr));
    }

    @Test
    public void testPrintArrayWithNullArray() {
        assertDoesNotThrow(() -> ArrayPrinter.printArray(null));
    }
}