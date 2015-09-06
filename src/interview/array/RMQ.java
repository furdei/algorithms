package interview.array;

/**
 * Find a minimum in a range.
 *
 * This solution offers a constant-time response, but needs nlogn pre-processing time
 */
public class RMQ {

    private int[] array;
    private int[][] sparseTable;
    private int levelsCount;
    private int arrayLength;
    private int[] log;

    public RMQ(int[] array) {
        preprocess(array);
    }

    private int fl(int i) {
        return i == 1 ? 0 : fl(i / 2) + 1;
    }

    private void preprocess(int[] array) {
        this.array = array;

        if (array == null) {
            arrayLength = 0;
        } else {
            arrayLength = array.length;
            levelsCount = 1;
            int length = 1;

            while (length <= arrayLength) {
                length = length << 1;
                levelsCount++;
            }

            levelsCount--;
            sparseTable = new int[levelsCount][arrayLength];
            log = new int[arrayLength];

            // level 0 is a source array
            for (int i = 0; i < arrayLength; i++) {
                sparseTable[0][i] = i;

                // precompute all the logarithms
                log[i] = fl(i + 1);
            }

            // fill in a sparse table by levels
            for (int i = 1; i < levelsCount; i++) {
                int rangeSize = 1 << i;
                int prvRangeSize = 1 << (i - 1);
                int rangesCount = arrayLength - rangeSize + 1;

                for (int j = 0; j < rangesCount; j++) {
                    int index1 = sparseTable[i - 1][j];
                    int index2 = sparseTable[i - 1][j + prvRangeSize];
                    sparseTable[i][j] = array[index1] < array[index2] ? index1 : index2;
                }
            }
        }
    }

    public int getMinPosition(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0
                || fromIndex >= arrayLength || toIndex >= arrayLength
                || toIndex < fromIndex) {
            return -1;
        }

        int rangeSize = toIndex - fromIndex + 1;
        int level = log[rangeSize - 1];
        int precomputedRangeSize = 1 << level;

        if (rangeSize == precomputedRangeSize) {
            return sparseTable[level][fromIndex];
        }

        // rangeSize is a bit bigger
        int index1 = sparseTable[level][fromIndex];
        int index2 = sparseTable[level][toIndex - precomputedRangeSize + 1];
        return array[index1] < array[index2] ? index1 : index2;
    }

    public static void test(RMQ rmq, int[] array, int fromIndex, int toIndex) {
        int minIndex = rmq.getMinPosition(fromIndex, toIndex);
        System.out.println(String.format("RMQ(%d, %d) => [%d]=%d",
                fromIndex, toIndex, minIndex, array[minIndex]));
    }

    public static void main(String[] args) {
        int[] array = new int[] {9, 3, 7, 1, 8, 12, 10, 20, 15, 18, 5};
        RMQ rmq = new RMQ(array);
        test(rmq, array, 0, 2);
        test(rmq, array, 0, 3);
        test(rmq, array, 2, 5);
        test(rmq, array, 2, 2);
        test(rmq, array, 8, 10);
        test(rmq, array, 0, array.length - 1);
    }

}
