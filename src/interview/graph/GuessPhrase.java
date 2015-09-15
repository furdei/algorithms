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
        int tries = 10000;

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
