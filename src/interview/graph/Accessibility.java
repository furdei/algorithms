package interview.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * You are given an array of non-negative integers (0, 1, 2 etc).
 * The value in each element represents the number of hops you
 * may take to the next destination. Write a function that
 * determines when you start from the first element whether you
 * will be able to reach the last element of the array.
 *
 * if a value is 3, you can take either 0, 1, 2 or 3 hops.
 *
 * For eg: for the array with elements 1, 2, 0, 1, 0, 1, any
 * route you take from the first element, you will not be able
 * to reach the last element.
 */
public class Accessibility {

    public boolean isLastAccessibleFromFirst(int[] map) {
        if (map == null || map.length == 0) {
            return false;
        }

        if (map.length == 1) {
            return true;
        }

        boolean[] discovered = new boolean[map.length];
        Queue<Integer> visitQueue = new LinkedList<>();
        discover(visitQueue, discovered, 0);

        while (visitQueue.size() > 0) {
            Integer cell = visitQueue.remove();
            int hops = map[cell];

            if (hops > 0) {
                int start = Math.max(0, cell - hops);
                int finish = Math.min(map.length - 1, cell + hops);

                if (finish == map.length - 1) {
                    return true;
                }

                for (int i = start; i <= finish; i++) {
                    discover(visitQueue, discovered, i);
                }
            }
        }

        return false;
    }

    private void discover(Queue<Integer> visitQueue, boolean[] discovered, int newItem) {
        if (!discovered[newItem]) {
            visitQueue.add(newItem);
            discovered[newItem] = true;
        }
    }

    public static void main(String[] args) {
        Accessibility accessibility = new Accessibility();
        int[] map = new int[] {1, 2, 0, 1, 0, 1};
        System.out.println("isLastAccessibleFromFirst(" + Arrays.toString(map) + ") => "
                + accessibility.isLastAccessibleFromFirst(map));
    }

}
