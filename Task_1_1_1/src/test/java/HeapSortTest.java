import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HeapSortTest {

    @Test
    public void testHeapSortWithRegularArray() {
        int[] arr = {9, 4, 3, 8, 10, 2, 5};
        int[] expected = {2, 3, 4, 5, 8, 9, 10};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithDuplicates() {
        int[] arr = {5, 2, 8, 5, 3, 2, 8};
        int[] expected = {2, 2, 3, 5, 5, 8, 8};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithSingleElementArray() {
        int[] arr = {42};
        int[] expected = {42};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithEmptyArray() {
        int[] arr = {};
        int[] expected = {};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithNegativeNumbers() {
        int[] arr = {-5, 3, -10, 0, 7, -2};
        int[] expected = {-10, -5, -2, 0, 3, 7};

        HeapSort.heapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithNullArr() {
        int[] arr = null;
        HeapSort.heapSort(arr);

        assertArrayEquals(null, arr);
    }
}
