package interview.heap;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 Given a list of n sorted lists of numbers, write a method that returns one giant list of all the numbers in order.

 Example input:

 NSArray* input = @[
 @[@2, @5, @10],
 @[@25, @100, @105],
 @[@7, @56, @42],
 .......
 ];
 */
public class MergeWithPriorityQueue {

    private static class Top implements Comparable<Top> {
        int value;
        int arrayIndex;
        int indexInArray;

        public Top(int value, int arrayIndex, int indexInArray) {
            this.value = value;
            this.arrayIndex = arrayIndex;
            this.indexInArray = indexInArray;
        }

        @Override
        public int compareTo(Top p) {
            return value - p.value;
        }

        public String toString() {
            return "[" + arrayIndex + "][" + indexInArray + "]=" + value;
        }
    }

    public int[] mergeSort(int[][] arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }

        Queue<Top> minQueue = new PriorityQueue<>();
        int resultLength = 0;

        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null && arrays[i].length > 0) {
                minQueue.add(new Top(arrays[i][0], i, 0));
                resultLength += arrays[i].length;
            }
        }

        int[] result = new int[resultLength];
        int iResult = 0;

        while (minQueue.size() > 0) {
            System.out.println(minQueue);

            Top top = minQueue.poll();
            result[iResult++] = top.value;
            top.indexInArray++;

            if (top.indexInArray < arrays[top.arrayIndex].length) {
                top.value = arrays[top.arrayIndex][top.indexInArray];
                minQueue.add(top);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] arrays = new int[][] {
                { 4, 6, 9 },
                { 1, 5, 6, 8 },
                { 1, 2, 4, 9 },
                { 2, 7 }
        };

        MergeWithPriorityQueue s = new MergeWithPriorityQueue();
        int[] result = s.mergeSort(arrays);
        System.out.println(Arrays.toString(result));
    }

}
