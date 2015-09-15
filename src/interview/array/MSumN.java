package interview.array;

import java.util.*;

/**
 * In an array of size N find all combination of exactly M elements
 * giving sum SUM
 *
 * This follows naive approach and has complexity O(n^m) !
 */
public class MSumN {

    public List<int[]> findSums(int[] array, int m, int sum) {
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

    public List<int[]> findSumsBranches(int[] srcArray, int m, int sum) {
        if (srcArray == null || srcArray.length < m || m < 1) {
            return null;
        }

        int[] array = Arrays.copyOf(srcArray, srcArray.length);
        Arrays.sort(array);

        List<int[]> sums = new LinkedList<>();
        int[] index = new int[m];
        int currentSum = 0;

        for (int i = 0; i < m - 1; i++) {
            index[i] = i;
            currentSum += array[i];
        }

        //   m: 2
        // sum: 7
        // ind:  0  1  2  3  4  5
        // arr: -3  1  2  5  9 15
        // len: 6

        boolean done = false;

        while (!done) {
            // use binary search to find the last index in a sorted array
            int searchStartIndex = m == 1 ? 0 : index[m - 2] + 1;
            int searchValue = sum - currentSum;
            int targetIndex = Arrays.binarySearch(array, searchStartIndex, array.length, searchValue);

            if (targetIndex >= searchStartIndex && targetIndex < array.length) {
                // searchValue is found, but there could be several identical values
                int countDuplicates = 1;
                int i = targetIndex;
                while (i > 0 && array[i] == array[i - 1]) {
                    i--;
                    countDuplicates++;
                }
                i = targetIndex;
                while (i < array.length - 1 && array[i] == array[i + 1]) {
                    i++;
                    countDuplicates++;
                }

                // add the result
                index[m - 1] = targetIndex;
                int[] result = new int[m];

                for (i = 0; i < m; i++) {
                    result[i] = array[index[i]];
                }

                for (i = 0; i < countDuplicates; i++) {
                    sums.add(result);
                }
            }

            // the end of the branch
            done = true;

            if (m > 1) {
                int i = m - 2;

                while (i >= 0 && done) {
                    currentSum -= array[index[i]];
                    index[i]++;

                    if (index[i] == array.length - m + i + 1) {
                        i--;
                    } else {
                        done = false;
                    }
                }

                if (i >= 0) {
                    currentSum += array[index[i]];

                    for (i++; i <= m - 2; i++) {
                        index[i] = index[i - 1] + 1;
                        currentSum += array[index[i]];
                    }
                }
            }
        }

        return sums;
    }

    private static class IntArray {

        private int[] array;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntArray intArray = (IntArray) o;

            if (!Arrays.equals(array, intArray.array)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }
    }

    public List<int[]> findSumsWithHashMap(int[] array, int m, int sum) {
        if (array == null || array.length < m || m < 2) {
            return null;
        }

        Map<Integer, List<int[]>> cache = new HashMap<>(); // sum -> list of position pairs
        int l = array.length;
        int m2 = m / 2;
        int[] indices = new int[m2];
        int curSum = 0;
        List<int[]> result = new LinkedList<>();
        Set<HashResult> hashResults = new HashSet<>();

        for (int i = 0; i < m2; i++) {
            indices[i] = i;
            curSum += array[i];
        }

        // m = 4
        // m2= 2
        // l = 7
        //     0  1  2  3  4  5  6
        // a = 3, 5, 9, 1, 4, 8, 2
        // i = ^  ^
        // i =                ^  ^
        //                    0  1

        while (indices[0] <= l - m2) {
            int targetSum = sum - curSum;
            List<int[]> options = cache.get(targetSum);

            if (options != null) {
                for (int[] option : options) {
                    // check if an option contains one of currently analyzed indices
                    boolean duplicate = false;
                    for (int i = 0; i < m2 && !duplicate; i++) {
                        int optI = Arrays.binarySearch(option, indices[i]);
                        duplicate = optI >= 0 && optI < m2;
                    }

                    if (!duplicate) {
                        HashResult hashResult = new HashResult();
                        hashResult.result = new int[m];
                        mergeIndices(hashResult.result, option, indices);

                        if (!hashResults.contains(hashResult)) {
                            hashResults.add(hashResult);
                            int[] newRes = new int[m];
                            for (int i = 0; i < m; i++) {
                                newRes[i] = array[hashResult.result[i]];
                            }
                            result.add(newRes);
                        }
                    }
                }
            }

            options = cache.get(curSum);
            if (options == null) {
                options = new LinkedList<>();
                cache.put(curSum, options);
            }
            options.add(Arrays.copyOf(indices, m2));

            curSum = incIndices(array, indices, curSum);
        }

        return result;
    }

    private static class HashResult {
        int[] result;

        @Override
        public int hashCode() {
            return Arrays.hashCode(result);
        }

        @Override
        public boolean equals(Object a) {
            return Arrays.equals(result, ((HashResult)a).result);
        }
    }

    // increment indices and return a new sum
    private int incIndices(int[] array, int[] indices, int sum) {
        int l = array.length;
        int m2 = indices.length;
        int i = m2 - 1;
        sum -= array[indices[i]];
        indices[i]++;

        while (i >= 0 && indices[i] > l - m2 + i) {
            if (--i >= 0) {
                sum -= array[indices[i]];
                indices[i]++;
            }
        }

        if (i >= 0) {
            for (int j = i; j < m2; j++) {
                if (j > i) {
                    indices[j] = indices[j - 1] + 1;
                }
                sum += array[indices[j]];
            }
        }

        return sum;
    }

    private void mergeIndices(int[] dst, int[] src1, int[] src2) {
        int l1 = src1.length;
        int l2 = src2.length;
        int i1 = 0;
        int i2 = 0;
        int d = 0;

        while (i1 < l1 || i2 < l2) {
            if (i1 < l1 && i2 < l2) {
                if (src1[i1] < src2[i2]) {
                    dst[d++] = src1[i1++];
                } else {
                    dst[d++] = src2[i2++];
                }
            } else if (i1 < l1) {
                dst[d++] = src1[i1++];
            } else {
                dst[d++] = src2[i2++];
            }
        }
    }

    public static void main(String[] args) {
        MSumN nSumM2 = new MSumN();
        int[] array = { 3, 4, 5, 6, 7, 8, 9 };
        int m = 4;
        int targetSum = 22;
        List<int[]> sums = nSumM2.findSums(array, m, targetSum);
        for (int[] sum : sums) {
            System.out.println("NSumM2.main result 1: " + Arrays.toString(sum));
        }
        List<int[]> sums2 = nSumM2.findSumsBranches(array, m, targetSum);
        for (int[] sum : sums2) {
            System.out.println("NSumM2.main result 2: " + Arrays.toString(sum));
        }
        List<int[]> sums3 = nSumM2.findSumsWithHashMap(array, m, targetSum);
        for (int[] sum : sums3) {
            System.out.println("NSumM2.main result 3: " + Arrays.toString(sum));
        }
    }

}
