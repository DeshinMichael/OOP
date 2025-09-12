/**
 * Utility class for printing integer arrays.
 */
public class ArrayPrinter {
    private ArrayPrinter() {}

    public static void printArray(int[] arr) {
        if (arr == null) {
            System.err.println("Ошибка: входной массив равен null");
            return;
        }

        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i < arr.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
}
