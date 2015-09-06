package interview.graph;

import java.util.*;

/**
 * Given n nodes and an X node, find k nearest nodes to X
 */
public class NearestNodesFinder {

    public static class Node {
        public String label;
        public Collection<Node> neighbours;

        public Node(String label) {
            this.label = label;
            neighbours = new LinkedList<>();
        }

        public String toString() {
            return label;
        }
    }

    public List<Node> findKNearest(Node root, int k) {
        if (root == null || k < 1) {
            return null;
        }

        List<Node> found = new LinkedList<>();
        Queue<Node> discovered = new LinkedList<>();
        discovered.add(root);
        Set<Node> visited = new HashSet<>();

        while (found.size() < k && discovered.size() > 0) {
            Node node = discovered.remove();
            visited.add(node);

            if (node != root) {
                found.add(node);
            }

            for (Node neighbour : node.neighbours) {
                if (!visited.contains(neighbour)) {
                    discovered.add(neighbour);
                }
            }
        }

        return found;
    }

    public static void main(String[] args) {
        // h
        // |
        // a -> b -> c -> d
        // | /
        // e -> f -> g


        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        Node g = new Node("g");
        Node h = new Node("h");

        a.neighbours.add(b);
        a.neighbours.add(e);
        a.neighbours.add(h);
        b.neighbours.add(c);
        c.neighbours.add(d);
        e.neighbours.add(b);
        e.neighbours.add(f);
        f.neighbours.add(g);

        NearestNodesFinder finder = new NearestNodesFinder();
        List<Node> found = finder.findKNearest(a, 5);

        for (Node node : found) {
            System.out.println(node);
        }
    }

}
