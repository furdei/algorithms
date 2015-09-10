package interview.array;

import java.util.Arrays;

/**
 * select orderStatistics minimum in an array
 */
public class QuickSelection {

    public int select(int[] array, int orderStatistics) {
        if (array == null || orderStatistics < 0 || array.length <= orderStatistics) {
            return -1;
        }

        return select(array, orderStatistics, 0, array.length - 1);
    }

    public int select(int[] array, int orderStatistics, int from, int to) {
        if (to - from <= 0) {
            return array[orderStatistics];
        }

        int pivot = to;
        int l = from;
        int r = to - 1;

        while (l < r) {
            while (l < r && array[l] < array[pivot]) {
                l++;
            }

            while (l < r && array[r] > array[pivot]) {
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

        if (orderStatistics == l) {
            return array[orderStatistics];
        }

        if (orderStatistics < l) {
            return select(array, orderStatistics, from, l - 1);
        }

        return select(array, orderStatistics, l + 1, to);
    }

    private void swap(int[] array, int i, int j) {
        int buffer = array[i];
        array[i] = array[j];
        array[j] = buffer;
    }

    public static void main(String[] args) {
        QuickSelection quickSelection = new QuickSelection();
        int[] array = new int[] {23, 4, 1, 0, 12, 95, 1};
        //                       0, 1, 1, 4, 12, 23, 95
        System.out.print(Arrays.toString(array));
        System.out.print(" => ");
        int stat = quickSelection.select(array, 5);
        System.out.println(stat);
    }

}
