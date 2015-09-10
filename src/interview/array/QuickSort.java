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
                swap(array, l, r);
                l++;
                r--;
            }
        }

        while (l < to && array[l] <= array[pivot]) {
            l++;
        }

        swap(array, l, pivot);

        sort(array, from, r - 1);
        sort(array, l + 1, to);
    }

    private void swap(int[] array, int i, int j) {
        int buffer = array[i];
        array[i] = array[j];
        array[j] = buffer;
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
