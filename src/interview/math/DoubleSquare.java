package interview.math;

import java.util.LinkedList;
import java.util.List;

/**
 * Let's say there is a double square number X, which can be expressed as the sum of two perfect squares,
 * for example, 10 is double square because 10 = 3^2 + 1^2
 * Determine the ways which it can be written as the sum of two squares
 */
public class DoubleSquare {

    public List<int[]> getSquares(int number) {
        if (number < 2) {
            return null;
        }

        int xsq;
        List<int[]> results = new LinkedList<>();

        for (int x = 1; (xsq = x * x) <= number / 2; x++) {
            int ysq = number - xsq;
            int y = (int) Math.sqrt(ysq);

            if (xsq + y * y == number) {
                int[] res = new int[2];
                res[0] = x;
                res[1] = y;
                results.add(res);
            }
        }

        return results;
    }

    public static void test(int number, DoubleSquare solution) {
        System.out.print("getSquares(" + number + ") => ");
        List<int[]> results = solution.getSquares(number);

        if (results == null) {
            System.out.println("null");
        } else {
            for (int[] entry : results) {
                System.out.print("[" + entry[0] + ", " + entry[1] + "] ");
            }
        }

        System.out.println("");
    }

    public static void main(String[] args) {
        DoubleSquare solution = new DoubleSquare();
        test(500, solution);
        test(100, solution);
        test(10, solution);
        test(5, solution);
        test(2, solution);
        test(1, solution);
        test(-100, solution);
    }

}
