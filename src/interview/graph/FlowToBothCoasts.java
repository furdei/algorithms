package interview.graph;

import java.util.*;

/**
 You are given a 2d rectangular array of positive integers representing the height map of a continent.
 The "Pacific ocean" touches the left and top edges of the array and the "Atlantic ocean" touches the right and bottom edges.
 - Find the "continental divide". That is, the list of grid points where water can flow either to the Pacific or the Atlantic.
 Water can only flow from a cell to another one with height equal or lower.

 Example:

 Pacific ~  ~  ~ |__
 ~ 1  2  2  3 (5) ~
 ~ 3  2  3 (4)(4) ~
 ~ 2  4 (5) 3  1  ~
 ~(6)(7) 1  4  5  ~
 ~(5) 1  1  2  4  ~
  |~  ~  ~ Atlantic

 The answer would be the list containing the coordinates of all circled cells:
 [(4,0), (3,1), (4,1), (2,2), (0,3), (1,3), (0,4)]

 Idea:
 1) Start from all map points that are neighbours of Pacific.
 Using BFS, find all the ways from Pacific to the Divide, climbing up.
 Mark all found points as "1"
 2) Start from all map points that are neighbours of Pacific.
 Using BFS, find all the ways from Pacific to the Divide, climbing up.
 Add "2" to all found points.
 3) All the point marked as "3" are the result.
 */
public class FlowToBothCoasts {

    private static final int FROM_PACIFIC = 1;
    private static final int FROM_ATLANTIC = 2;
    private static final int RESULT = FROM_PACIFIC + FROM_ATLANTIC;

    public List<int[]> findContinentalDivide(int[][] map) {
        if (map == null || map.length == 0) {
            return null;
        }

        int height = map.length;
        int width = map[0].length;
        for (int i = 1; i < height; i++) {
            if (map[i].length != width) {
                return null;
            }
        }

        int[][] colorMap = new int[height][width];

        // start from Pacific
        Queue<int[]> startFromPacific = new LinkedList<>();
        for (int i = 0; i < height; i++) {
            addPoint(startFromPacific, colorMap, i, 0, FROM_PACIFIC);
        }
        for (int i = 1; i < width; i++) {
            addPoint(startFromPacific, colorMap, 0, i, FROM_PACIFIC);
        }

        findWayUp(map, colorMap, startFromPacific, FROM_PACIFIC);

        // start from Atlantic
        Queue<int[]> startFromAtlantic = new LinkedList<>();
        for (int i = 0; i < height; i++) {
            addPoint(startFromAtlantic, colorMap, i, width - 1, FROM_ATLANTIC);
        }
        for (int i = 0; i < width - 1; i++) {
            addPoint(startFromAtlantic, colorMap, height - 1, i, FROM_ATLANTIC);
        }

        findWayUp(map, colorMap, startFromAtlantic, FROM_ATLANTIC);

        List<int[]> result = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (colorMap[i][j] == RESULT) {
                    result.add(new int[] { j, i });
                }
            }
        }

        return result;
    }

    private void addPoint(Queue<int[]> points, int[][] colorMap, int v, int h, int color) {
        colorMap[v][h] += color;
        points.add(new int[] { v, h });
    }

    private void findWayUp(int[][] map, int[][] colorMap, Queue<int[]> start, int color) {
        int height = map.length;
        int width = map[0].length;
        Queue<int[]> pointsDiscovered = start;

        while (pointsDiscovered.size() > 0) {
            int[] point = pointsDiscovered.poll();
            int v = point[0];
            int h = point[1];

            if (v > 0) {
                checkAndAdd(pointsDiscovered, map, colorMap, v, h, v - 1, h, color);
            }

            if (v < height - 1) {
                checkAndAdd(pointsDiscovered, map, colorMap, v, h, v + 1, h, color);
            }

            if (h > 0) {
                checkAndAdd(pointsDiscovered, map, colorMap, v, h, v, h - 1, color);
            }

            if (h < width - 1) {
                checkAndAdd(pointsDiscovered, map, colorMap, v, h, v, h + 1, color);
            }
        }
    }

    private void checkAndAdd(Queue<int[]> points, int[][] map, int[][] colorMap, int fv, int fh, int tv, int th, int exploreColor) {
        if (canMoveUp(map, colorMap, fv, fh, tv, th, exploreColor)) {
            addPoint(points, colorMap, tv, th, exploreColor);
        }
    }

    private boolean canMoveUp(int[][] map, int[][] colorMap, int fv, int fh, int tv, int th, int exploreColor) {
        return colorMap[tv][th] < exploreColor && map[tv][th] >= map[fv][fh];
    }

    public static void main(String[] args) {
        int map[][] = new int[][] {
                { 1, 2, 2, 3, 5 },
                { 3, 2, 3, 4, 4 },
                { 2, 4, 5, 3, 1 },
                { 6, 7, 1, 4, 5 },
                { 5, 1, 1, 2, 4 }
        };
        int map2[][] = new int[][] {
                { 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1 }
        };
        int map3[][] = new int[][] {
                { 9, 8, 7, 6, 4 },
                { 8, 7, 6, 5, 6 },
                { 7, 6, 5, 6, 7 },
                { 6, 5, 6, 7, 8 },
                { 4, 6, 7, 8, 9 }
        };

        FlowToBothCoasts flowToBothCoasts = new FlowToBothCoasts();
        List<int[]> result = flowToBothCoasts.findContinentalDivide(map);
        for (int[] point : result) {
            System.out.println(Arrays.toString(point));
        }
    }
}
