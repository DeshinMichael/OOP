public class Main {
    public static void main(String[] args) {
        int[] arr = {9, 4, 3, 8, 10, 2, 5};
        HeapSort.heapSort(arr);
        printArray(arr);
    }

    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i < arr.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
}