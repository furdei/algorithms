package interview.math;

/**
 * Given two unsigned integer values, write a function that returns the first divided by the second.
 * You cannot (of course) use div or mod operators - only addition, subtraction and multiplication.
 * Discuss the strengths/weaknesses/algorithmic complexity of your solution.
 * Is there a better way to do it? If so, what, and what is its complexity?
 */
public class DivideInt {

    public int divide(int a, int b) {
        if (a < 0 || b <= 0) {
            return -1;
        }

        if (b > a || a == 0) {
            return 0;
        }

        int b2 = b;
        int result = 1;

        while (b2 < a) {
            b2 = b2 * 10;
            result *= 10;
        }

        while (b2 > a) {
            b2 = b2 - b;
            result--;
        }

        return result;
    }

    public static void test(DivideInt divideInt, int a, int b) {
        System.out.println(String.format("divide(%d, %d) => %d, check = %d", a, b, divideInt.divide(a, b), a / b));
    }

    public static void main(String[] args) {
        DivideInt divideInt = new DivideInt();
        test(divideInt, 4, 2);
        test(divideInt, 1000, 10);
        test(divideInt, 25345, 23);
        test(divideInt, 4, 5);
    }

}
