package interview.graph;

import java.util.*;

/**
 Given a function

 getRandomTripplet()

 which returns a random triplet of letters from a string.
 You don't know the string using calls to this function you have to correctly guess the string.
 the length of the string is also given.

 Lets say the string is helloworld the function getRandomTriplet will return things like

 hlo
 hew
 wld
 owo

 the function maintains the relative order of the letters. so it will never return

 ohl since h is before o in the string.
 owe since w is after e

 The string is not known you are only given length of the string.
 */
public class GuessPhrase {

    private static String secret = "helloworld";
    private static Random rand = new Random();
    static {
        rand.setSeed(System.currentTimeMillis());
    }

    private static final int N = secret.length();
    private static final int M = 3;

    public String getRandomTripplet() {
        int indices[] = new int[M];
        for (int i = 0; i < M; i++) {
            boolean generate = true;
            while (generate) {
                indices[i] = rand.nextInt(N);
                int j = i - 1;
                while (j >= 0 && indices[j] != indices[i]) {
                    j--;
                }
                generate = j >= 0;
            }
        }

        StringBuilder sb = new StringBuilder();
        Arrays.sort(indices);
        for (int i = 0; i < M; i++) {
            sb.append(secret.charAt(indices[i]));
        }
        return sb.toString();
    }

    public void guess() {
        Map<Character, Integer> stat = new HashMap<>();
        Map<Character, Integer> statFirst = new HashMap<>();
        Map<Character, Integer> seenDuplicates = new HashMap<>();
        Map<Character, Node> graph = new HashMap<>();
        int tries = 100000000;

        while (tries > 0) {
            String rand = getRandomTripplet();
            //System.out.println(rand);
            incStat(statFirst, rand.charAt(0));
            Map<Character, Integer> duplicates = new HashMap<>();
            for (int i = 0; i < rand.length(); i++) {
                incStat(stat, rand.charAt(i));
                incStat(duplicates, rand.charAt(i));
            }
            for (Map.Entry<Character, Integer> d : duplicates.entrySet()) {
                if (d.getKey() == 'r' && d.getValue() > 1) {
                    System.out.println(rand);
                }
                Integer sd = seenDuplicates.get(d.getKey());
                if (sd == null) {
                    sd = 0;
                }
                if (d.getValue() > sd) {
                    seenDuplicates.put(d.getKey(), d.getValue());
                }
            }
            for (int i = 0; i < rand.length() - 1; i++) {
                Node from = getNode(graph, rand.charAt(i));
                Node to = getNode(graph, rand.charAt(i + 1));
                connect(from, to);
            }
            tries--;
        }

        dumpStat("Stat", stat);
        dumpStat("Stat First", statFirst);
        dumpStat("Seen Duplicates", seenDuplicates);
        dumpGraph("Graph", graph.values());
        findExpensivePath(graph, 'h', seenDuplicates);
    }

    private void findExpensivePath(Map<Character, Node> graph, char start, Map<Character, Integer> occurences) {
        int maxCost = 0;
        String maxPath = "";
        Stack<PathState> stack = new Stack<PathState>();
        stack.push(new PathState("", 0, new HashMap<Character, Integer>(), graph.get(start)));
        List<CompletePath> list = new ArrayList<>();

        while (stack.size() > 0) {
            PathState state = stack.pop();

            int visitedCount = markVisited(state.visited, state.current.c);
            if (visitedCount <= occurences.get(state.current.c)) {
                // we can move on
                // check if we visited all the nodes yet
                int unvisitedCount = 0;

                for (Map.Entry<Character, Integer> occurence : occurences.entrySet()) {
                    Integer visited = state.visited.get(occurence.getKey());
                    if (visited == null) {
                        visited = 0;
                    }
                    unvisitedCount += occurence.getValue() - visited;
                    if (unvisitedCount > 0) {
                        break;
                    }
                }

                if (unvisitedCount == 0) {
                    // a valid path, could be one of the optimal
                    String foundPath = state.route + state.current.c;
                    //System.out.println("Cost: " + state.cost + " Path: " + foundPath);
                    list.add(new CompletePath(state.cost, foundPath));

                    if (state.cost > maxCost) {
                        maxCost = state.cost;
                        maxPath = foundPath;
                    }
                } else {
                    // try all the possible routs from here
                    for (Map.Entry<Node, Integer> next : state.current.next.entrySet()) {
                        stack.push(new PathState(
                                state.route + state.current.c,
                                state.cost + next.getValue(),
                                copyOf(state.visited),
                                next.getKey()));
                    }
                }
            }
        }

        System.out.println("Max Cost: " + maxCost + " Path: " + maxPath);
        Collections.sort(list, new Comparator<CompletePath>() {
            public int compare(CompletePath a, CompletePath b) {
                return b.cost - a.cost;
            }
        });
        for (CompletePath p : list) {
            System.out.println(p);
        }
    }

    private static class CompletePath {
        int cost;
        String path;

        public CompletePath(int cost, String path) {
            this.cost = cost;
            this.path = path;
        }

        public String toString() {
            return "Cost: " + cost + " Path: " + path;
        }
    }

    private int markVisited(Map<Character, Integer> visited, char c) {
        Integer count = visited.get(c);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        visited.put(c, count);
        return count;
    }

    private static class PathState {
        String route;
        int cost;
        Map<Character, Integer> visited;
        Node current;

        public PathState(String route, int cost, Map<Character, Integer> visited, Node current) {
            this.route = route;
            this.cost = cost;
            this.visited = visited;
            this.current = current;
        }

        public String toString() {
            return "R: " + route + " C: " + cost + " V: " + visited + " N: " + current;
        }
    }

    private Map<Character, Integer> copyOf(Map<Character, Integer> src) {
        Map<Character, Integer> dst = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : src.entrySet()) {
            dst.put(entry.getKey(), entry.getValue());
        }
        return dst;
    }

    private void dumpGraph(String title, Collection<Node> graph) {
        System.out.println(title + ":");
        for (Node node : graph) {
            System.out.println(node);
        }
    }

    private static class Node {
        char c;
        Map<Node, Integer> next;
        Map<Node, Integer> prev;

        public Node(char c) {
            this.c = c;
            next = new HashMap<>();
            prev = new HashMap<>();
        }

        public String toString() {
            String nextStr = "";
            for (Node node : next.keySet()) {
                nextStr += node.c + "(" + next.get(node) + ") ";
            }
            return c + " -> " + nextStr;
        }
    }

    private Node getNode(Map<Character, Node> graph, char c) {
        Node node = graph.get(c);
        if (node == null) {
            node = new Node(c);
            graph.put(c, node);
        }
        return node;
    }

    private void connect(Node from, Node to) {
        Integer count = from.next.get(to);
        if (count != null) {
            count++;
        } else {
            count = 1;
        }
        from.next.put(to, count);
        to.prev.put(from, count);
    }

    private void incStat(Map<Character, Integer> stat, char c) {
        Integer count = stat.get(c);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        stat.put(c, count);
    }

    private void dumpStat(String title, Map<Character, Integer> stat) {
        Map.Entry<Character, Integer>[] array =
                (Map.Entry<Character, Integer>[]) new Map.Entry[stat.size()];
        stat.entrySet().toArray(array);
        Arrays.sort(array, new Comparator<Map.Entry<Character, Integer>>() {
            public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });
        System.out.println(title + ": " + Arrays.toString(array));
    }

    public static void main(String[] args) {
        GuessPhrase s = new GuessPhrase();
        s.guess();
    }
}
