package interview.array;

import java.util.Arrays;

/**
 * Select orderStatistics minimum in an array
 * This implementation is optimized to use 3 groups partition method
 */
public class QuickSelection2 {

    int findOrderStatistics(int[] srcArray, int statisticsOrder) {
        if (srcArray == null || statisticsOrder < 0 || statisticsOrder >= srcArray.length) {
            return -1;
        }

        int sortStart = 0;
        int sortFinish = srcArray.length - 1;

        int[] array = Arrays.copyOf(srcArray, srcArray.length);

        while (true) {
            Partition partition = partition(array, array[sortStart], sortStart, sortFinish);

            if (statisticsOrder < partition.left) {
                sortFinish = partition.left - 1;
            } else if (statisticsOrder > partition.right) {
                sortStart = partition.right + 1;
            } else {
                return array[partition.left];
            }
        }
    }

    private static class Partition {
        int left;
        int right;
    }

    private Partition partition(int[] array, int pivot, int from, int to) {
        int firstPivot = from;
        int l = from;
        int r = to;

        while (l < r) {
            if (array[l] < pivot) {
                if (l > firstPivot) {
                    swap(array, l, firstPivot);
                }

                l++;
                firstPivot++;
            } else if (array[l] > pivot) {
                while (l < r && array[r] > pivot) {
                    r--;
                }

                if (l < r) {
                    swap(array, l, r);
                    r--;
                }
            } else {
                l++;
            }
        }

        while (array[r] > pivot) {
            r--;
        }

        Partition partition = new Partition();
        partition.left = firstPivot;
        partition.right = r;
        return partition;
    }

    private void swap(int[] array, int i, int j) {
        int buffer = array[i];
        array[i] = array[j];
        array[j] = buffer;
    }

    public static void test(int[] array, int orderStatisitics, QuickSelection2 solution) {
        System.out.println("test: " + Arrays.toString(array) + ", " + orderStatisitics + " => "
                + solution.findOrderStatistics(array, orderStatisitics));
    }

    public static void main(String[] args) {
        QuickSelection2 solution = new QuickSelection2();
        int[] array = new int[] {1, 8, 2, 4, 9, 3, 1, 0, 2};
        //                       0, 1, 1, 2, 2, 3, 4, 8, 9
        for (int i = -1; i <= array.length; i++) {
            test(array, i, solution);
        }
        test(null, 0, solution);
    }

}
