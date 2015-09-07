package interview.array;

/**
 * Given an array of positive integers and a target integer, find if there is a consecutive subarray
 * that sums to the target. E.g, given {5,6,4,12}, findsum(10)=true, findsum(12)=false.
 */
public class ConsecutiveSubarray {

    private enum State {
        GROW, SHRINK
    };

    public boolean findsum(int[] array, int targetSum) {
        // given {5,6,4,12}, findsum(10)=true, findsum(12)=false.
        if (array == null || array.length < 2) {
            return false;
        }

        int from = 0;
        int to = 1;
        int sum = array[0] + array[1];
        State state = sum < targetSum ? State.GROW : State.SHRINK;

        while (true) {
            if (sum == targetSum) {
                return true;
            }

            switch (state) {
                case GROW:
                    to++;

                    if (to == array.length) {
                        return false;
                    }

                    sum += array[to];
                    break;

                case SHRINK:
                    sum -= array[from];

                    if (to - from == 1) {
                        to++;

                        if (to == array.length) {
                            return false;
                        }

                        sum += array[to];
                    }

                    from++;
                    break;
            }

            state = sum < targetSum ? State.GROW : State.SHRINK;
        }
    }

    public static void main(String[] args) {
        int[] array = new int[] {5,6,4,12};
        ConsecutiveSubarray consecutiveSubarray = new ConsecutiveSubarray();
        System.out.println("findsum(10) => " + consecutiveSubarray.findsum(array, 10));
        System.out.println("findsum(11) => " + consecutiveSubarray.findsum(array, 11));
        System.out.println("findsum(12) => " + consecutiveSubarray.findsum(array, 12));
        System.out.println("findsum(16) => " + consecutiveSubarray.findsum(array, 16));
        System.out.println("findsum(22) => " + consecutiveSubarray.findsum(array, 22));
        System.out.println("findsum(99) => " + consecutiveSubarray.findsum(array, 99));
    }
}
