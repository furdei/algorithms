package interview.array;

import java.util.Arrays;

/**
 * Given an array of integer, find the maximum drop between two array elements,
 * given that second element comes after the first one.
 *
 * Linear.
 * Implementation: we traverse an array and find it's maximum.
 * Every time we compare current item with the maximum so far.
 * if the drop is more than maximum drop, we take it as a new maximum.
 */
public class FindMaxDrop {

    public int findMaxDrop(int[] a) {
        if (a == null || a.length < 2) {
            return -1;
        }

        int maxValue = a[0];
        int maxDrop = -1;

        for (int i = 1; i < a.length; i++) {
            if (a[i] > maxValue) {
                maxValue = a[i];
            } else {
                if (maxValue - a[i] > maxDrop) {
                    maxDrop = maxValue - a[i];
                }
            }
        }

        return maxDrop;
    }

    public static void main(String[] args) {
        int[][] testCases = new int[][] {
                { 0, 100, 50, 25, 110, 30 },
                { 0, 100, 50, 25, 110, 40 },
                { 0, 1, 2, 3, 4, 5, 6, 7 },
                { 7, 6, 5, 4, 3, 2, 1, 0 },
                { 3, 3 },
                { 3 }
        };

        FindMaxDrop findMaxDrop = new FindMaxDrop();

        for (int[] testCase : testCases) {
            System.out.println("FindMaxDrop.main findMaxDrop(" + Arrays.toString(testCase) + ")= "
                    + findMaxDrop.findMaxDrop(testCase));
        }
    }

}
