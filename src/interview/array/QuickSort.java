package interview.array;

import java.util.Arrays;

/**
 * A quick sort algorithm
 */
public class QuickSort {

    public void sort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }

        sort(array, 0, array.length - 1);
    }

    private void sort(int[] array, int from, int to) {
        if (to - from <= 0) {
            return;
        }

        // pick the pivot
        int pivot = to;
        int l = from;
        int r = to - 1;

        while (l < r) {
            while (array[l] < array[pivot] && l < r) {
                l++;
            }

            while (array[r] > array[pivot] && l < r) {
                r--;
            }

            if (l < r) {
                int buffer = array[l];
                array[l] = array[r];
                array[r] = buffer;
                l++;
                r--;
            }
        }

        if (array[r] > array[pivot]) {
            int buffer = array[r];
            array[r] = array[pivot];
            array[pivot] = buffer;
        }

        sort(array, from, r - 1);
        sort(array, r + 1, to);
    }

    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        int[] array = new int[] {3, 6, 9, 1, 2, 6, 8, 12, 45, 1, 3, 4};
        System.out.print(Arrays.toString(array));
        System.out.print(" => ");
        quickSort.sort(array);
        System.out.println(Arrays.toString(array));
    }

}
