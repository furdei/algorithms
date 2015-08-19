package interview.array;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * In an array of size N find all combination of exactly M elements
 * giving sum SUM
 *
 * This follows naive approach and has complexity O(n^m) !
 */
public class MSumN {

    List<int[]> findSums(int[] array, int m, long sum) {
        if (array == null || array.length < m || m < 1) {
            return null;
        }

        List<int[]> sums = new LinkedList<>();
        int[] index = new int[m];

        for (int i = 0; i < m; i++) {
            index[i] = i;
        }

        while (index[0] < array.length - m) {
            long currentSum = array[index[0]];
            for (int i = 1; i < m; i++) {
                currentSum += array[index[i]];
            }

            if (currentSum == sum) {
                int[] result = new int[m];
                for (int i = 0; i < m; i++) {
                    result[i] = array[index[i]];
                }
                sums.add(result);
            }

            int i = m - 1;
            while (i >= 0 && (++index[i] > array.length - m + i)) {
                i--;
            }

            if (i >= 0) {
                for (int j = i + 1; j < m; j++) {
                    index[j] = index[j - 1] + 1;
                }
            }
        }

        return sums;
    }

    public static void main(String[] args) {
        MSumN nSumM2 = new MSumN();
        int[] array = { 3, 4, 5, 6, 7, 8, 9 };
        List<int[]> sums = nSumM2.findSums(array, 3, 18);
        for (int[] sum : sums) {
            System.out.println("NSumM2.main result: " + Arrays.toString(sum));
        }
    }

}
