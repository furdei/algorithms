package interview.stack;

import java.util.Arrays;
import java.util.Stack;

/**
 Given a matrix with 1's and 0's, a rectangle can be made with 1's.
 What is the maximum area of the rectangle.

 00010
 11100
 11110
 11000
 11010
 In this test case the result needs to be 8.

 How:
 00010 00010
 11100 11 100
 11110 11 110
 11000 11 000
 11010 11 010

 If you see above the 11's are used from the first two columns
 and last four rows making the area or count of 1's to be 8.
 */
public class BiggestRectangleReducedToSkyline {

    public int biggestRectangle(int[][] map) {
        if (map == null || map.length == 0) {
            return 0;
        }

        int h = map.length;
        int w = map[0].length;

        int[][] skylines = new int[h][w];
        for (int i = 0; i < w; i++) {
            skylines[0][i] = map[0][i];
        }

        int max = findMaxSquare(skylines[0]);

        for (int i = 1; i < h; i++) {
            for (int j = 0; j < w; j++) {
                skylines[i][j] = map[i][j] > 0 ? skylines[i-1][j] + map[i][j] : 0;
            }

            int lineMax = findMaxSquare(skylines[i]);
            if (lineMax > max) {
                max = lineMax;
            }
        }

        printMatrix(skylines);

        return max;
    }

    private static class Rect {
        int start;
        int height;

        public Rect(int start, int height) {
            this.start = start;
            this.height = height;
        }
    }

    private int findMaxSquare(int[] array) {
        Stack<Rect> stack = new Stack<>();
        int maxSq = 0;
        int prvH = 0;

        for (int i = 0; i <= array.length; i++) {
            int h = i < array.length ? array[i] : 0;
            if (h > prvH) {
                stack.push(new Rect(i, h));
            } else if (h < prvH) {
                // need to 'close' and consider all the rectangles in a stack
                // whose starting height is greater than h
                boolean pop = true;
                while (stack.size() > 0 && pop) {
                    Rect r = stack.pop();
                    pop = r.height > h;

                    if (pop) {
                        int square = (i - r.start) * r.height;
                        if (square > maxSq) {
                            maxSq = square;
                        }
                    } else {
                        // we've popped more than we need
                        stack.push(r);
                    }
                }
            }

            prvH = h;
        }

        return maxSq;
    }

    private static void printMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            System.out.println(Arrays.toString(m[i]));
        }
    }

    public static void main(String[] args) {
        int[][] map = new int[][] {
                { 0, 1, 0, 1, 0 },
                { 1, 1, 1, 0, 0 },
                { 1, 1, 1, 1, 0 },
                { 1, 1, 0, 0, 0 },
                { 1, 1, 1, 1, 0 }
        };
        BiggestRectangleReducedToSkyline s = new BiggestRectangleReducedToSkyline();
        System.out.println(s.biggestRectangle(map));
    }

}
