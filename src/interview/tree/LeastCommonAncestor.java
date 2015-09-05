package interview.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * LCA Problem
 */
public class LeastCommonAncestor {

    private static class Node {
        private final String label;
        private Node parent;
        private Collection<Node> children = new LinkedList<>();

        private Node(String label) {
            this.label = label;
        }

        public void addChild(Node child) {
            child.parent = this;
            children.add(child);
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public Node findLCA(Node x, Node y) {
        if (x == null || y == null) {
            return null;
        }

        if (x == y) {
            return x;
        }

        Set<Node> visited = new HashSet<>();
        visited.add(x);
        visited.add(y);

        Node node = x.parent;

        while (node != null) {
            if (visited.contains(node)) {
                return node;
            }

            visited.add(node);
            node = node.parent;
        }

        node = y.parent;

        while (node != null) {
            if (visited.contains(node)) {
                return node;
            }

            node = node.parent;
        }

        return null;
    }

    public static void main(String[] args) {
        //       a
        //      / \
        //     b   c
        //    /
        //   d
        //  / \
        // e   f

        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");

        a.addChild(b);
        a.addChild(c);
        b.addChild(d);
        d.addChild(e);
        d.addChild(f);

        LeastCommonAncestor lca = new LeastCommonAncestor();

        System.out.println("lca(" + e + ", " + f + ") = " + lca.findLCA(e, f));
        System.out.println("lca(" + d + ", " + c + ") = " + lca.findLCA(d, c));
        System.out.println("lca(" + e + ", " + b + ") = " + lca.findLCA(e, b));
        System.out.println("lca(" + b + ", " + e + ") = " + lca.findLCA(b, e));
        System.out.println("lca(null, null) = " + lca.findLCA(null, null));
    }

}
