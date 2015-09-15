package interview.recursion;

/**
 Given an array of positive integers that represents possible points a team could score in an individual play.
 Now there are two teams play against each other. Their final scores are S and S'.
 How would you compute the maximum number of times the team that leads could have changed?
 For example, if S=10 and S'=6. The lead could have changed 4 times:
 Team 1 scores 2, then Team 2 scores 3 (lead change);
 Team 1 scores 2 (lead change), Team 2 score 0 (no lead change);
 Team 1 scores 0, Team 2 scores 3 (lead change);
 Team 1 scores 3, Team 2 scores 0 (lead change);
 Team 1 scores 3, Team 2 scores 0 (no lead change).
 */
public class ScoreChanges {

    public int maxChanges(int a, int b) {
        if (a < 2 || b < 2) {
            return -1;
        }

        int max = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j) {
                    int aNew = a - getScores(i);
                    int bNew = b - getScores(j);
                    int rec = maxChanges(aNew, bNew);

                    if (rec >= 0) {
                        int inc = a > b && aNew < bNew || a < b && aNew > bNew ? 1 : 0;
                        int count = inc + rec;

                        if (count > max) {
                            max = count;
                        }
                    }
                }
            }
        }

        return max;
    }

    private int getScores(int index) {
        return index == 1 ? 2 : index == 2 ? 3 : 0;
    }

    public static void main(String[] args) {
        ScoreChanges s = new ScoreChanges();
        System.out.println(s.maxChanges(10, 6));
    }

}
