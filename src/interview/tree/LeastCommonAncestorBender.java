package interview.tree;

import interview.array.RMQ;

import java.util.*;

/**
 * LCA Problem, Bender Algorithm
 * Without a link to parent.
 * The problem is reduced to an RMQ problem
 */
public class LeastCommonAncestorBender {

    public static class Node {
        private int value;
        private Node left;
        private Node right;

        public Node(int value) {
            this.value = value;
        }

        public int hashCode() {
            return value;
        }

        public boolean equals(Node node) {
            if (node == null) {
                return false;
            }

            return value == node.value;
        }

        public String toString() {
            return Integer.toString(value);
        }

        public void setLeft(Node node) {
            left = node;
        }

        public void setRight(Node node) {
            right = node;
        }
    }

    private List<Node> eulersTour;
    private List<Integer> eulersTourDepth;
    private Map<Node, Integer> positionInEulersTour;
    private RMQ rmq;

    public LeastCommonAncestorBender(Node root) {
        if (root != null) {
            preprocess(root);
        }
    }

    private void preprocess(Node root) {
        buildEulersTour(root);

        int[] array = new int[eulersTourDepth.size()];
        for (int i = 0; i < eulersTourDepth.size(); i++) {
            array[i] = eulersTourDepth.get(i);
        }

        rmq = new RMQ(array);
    }

    private void buildEulersTour(Node root) {
        eulersTour = new ArrayList<>();
        eulersTourDepth = new ArrayList<>();
        positionInEulersTour = new HashMap<>();

        Stack<Node> stack = new Stack<>();
        Node current = root;
        int depth = 0;

        while (stack.size() > 0 || current != null) {
            if (current != null) {
                addNode(current, depth, eulersTourDepth.size());
                stack.push(current);
                current = current.left;
                depth++;
            } else {
                depth--;
                current = stack.pop();
                addNode(current, depth, eulersTourDepth.size());
                current = current.right;
                depth++;
            }
        }
    }

    private void addNode(Node node, int depth, int position) {
        if (positionInEulersTour.get(node) == null) {
            positionInEulersTour.put(node, position);
        }

        eulersTour.add(node);
        eulersTourDepth.add(depth);
    }

    public Node findLCA(Node x, Node y) {
        Integer posX = positionInEulersTour.get(x);
        Integer posY = positionInEulersTour.get(y);

        if (posX == null || posY == null) {
            return null;
        }

        if (posY < posX) {
            Integer n = posY;
            posY = posX;
            posX = n;
        }

        int minIndex = rmq.getMinPosition(posX, posY);
        return eulersTour.get(minIndex);
    }

    public static void test(LeastCommonAncestorBender lca, Node x, Node y) {
        Node ancestor = lca.findLCA(x, y);
        System.out.println(String.format("LCA(%s, %s) => %s", x, y, ancestor));
    }

    public static void main(String[] args) {
        //         5
        //       /  \
        //     1     6
        //   /  \
        //  9    8

        Node five = new Node(5);
        Node one = new Node(1);
        Node nine = new Node(9);
        Node eight = new Node(8);
        Node six = new Node(6);
        five.setLeft(one);
        five.setRight(six);
        one.setLeft(nine);
        one.setRight(eight);

        LeastCommonAncestorBender lca = new LeastCommonAncestorBender(five);
        test(lca, nine, eight);
        test(lca, eight, six);
        test(lca, five, six);
        test(lca, six, five);
        test(lca, one, one);
    }
}
