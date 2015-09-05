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
        if (array == null || array.length < m || m < 1) {
            return null;
        }

        List<int[]> sums = new LinkedList<>();
        int halfM = m / 2;

        int[] index = new int[halfM];
        for (int i = 0; i < halfM; i++) {
            index[i] = i;
        }

        Map<Integer, List<Set<Integer>>> hashSums = new HashMap<>();

        while (index[0] < array.length - halfM) {
            int currentSum = array[index[0]];
            Set<Integer> indices = new HashSet<>();
            for (int i = 0; i < halfM; i++) {
                indices.add(index[i]);
            }

            for (int i = 1; i < halfM; i++) {
                currentSum += array[index[i]];
            }

            List<Set<Integer>> listOfSums = hashSums.get(currentSum);
            if (listOfSums == null) {
                listOfSums = new LinkedList<>();
                hashSums.put(currentSum, listOfSums);
            }
            listOfSums.add(indices);

            int i = halfM - 1;
            while (i >= 0 && (++index[i] > array.length - halfM + i)) {
                i--;
            }

            if (i >= 0) {
                for (int j = i + 1; j < halfM; j++) {
                    index[j] = index[j - 1] + 1;
                }
            }
        }

        Set<IntArray> processedIndices = new HashSet<>();

        for (Map.Entry<Integer, List<Set<Integer>>> halfSum : hashSums.entrySet()) {
            int targetSum = sum - halfSum.getKey();
            List<Set<Integer>> indices = hashSums.get(targetSum);

            if (indices != null) {
                for (Set<Integer> ind1 : halfSum.getValue()) {
                    for (Set<Integer> ind2 : indices) {
                        Set<Integer> indicesCopy = new HashSet<>(ind1);
                        indicesCopy.retainAll(ind2);
                        if (indicesCopy.size() == 0) {
                            int[] indicesArray = new int[m];
                            Iterator<Integer> i = ind1.iterator();
                            Iterator<Integer> j = ind2.iterator();
                            Integer nextI = i.next();
                            Integer nextJ = j.next();

                            for (int k = 0; k < m; k++) {
                                if (nextI != null && nextJ != null && nextI <= nextJ || (nextI != null && nextJ == null)) {
                                    indicesArray[k] = array[nextI];
                                    nextI = i.hasNext() ? i.next() : null;
                                } else {
                                    indicesArray[k] = array[nextJ];
                                    nextJ = j.hasNext() ? j.next() : null;
                                }
                            }

                            IntArray cache = new IntArray();
                            cache.array = Arrays.copyOf(indicesArray, m);

                            if (!processedIndices.contains(cache)) {
                                sums.add(indicesArray);
                                processedIndices.add(cache);
                            }
                        }
                    }
                }
            }
        }

        return sums;
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
    }

}