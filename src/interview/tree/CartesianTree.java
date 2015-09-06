package interview.tree;

import java.util.Stack;

/**
 * Build Cartesian Tree on a given array
 *   9  3  7  1  8 12 10 20 15 18  5
 *            1
 *         /    \-----------------\
 *      3                          5
 *     / \         /--------------/
 *   9     7     8
 *                  \
 *                    10
 *                   /     \
 *                 12       15
 *                         /  \
 *                       20    18
 */
public class CartesianTree {

    public static class Node {
        private int value;
        private int position;
        private Node parent;
        private Node left;
        private Node right;

        public Node(int value, int position) {
            this.value = value;
            this.position = position;
        }

        @Override
        public String toString() {
            return String.format("[%d]=%d", position, value);
        }
    }

    public Node buildTree(int[] array) {
        if (array == null || array.length == 0) {
            return null;
        }

        Node prvNode = null;
        Node root = null;

        for (int i = 0; i < array.length; i++) {
            Node newNode = new Node(array[i], i);
            Node node = prvNode;
            Node prvClimbNode = null;

            while (node != null && node.value > array[i]) {
                prvClimbNode = node;
                node = node.parent;
            }

            if (node == null) {
                root = newNode;
                root.left = prvClimbNode;
                if (prvClimbNode != null) {
                    prvClimbNode.parent = root;
                }
            } else {
                newNode.left = node.right;
                if (node.right != null) {
                    node.right.parent = newNode;
                }

                node.right = newNode;
                newNode.parent = node;
            }

            prvNode = newNode;
        }

        return root;
    }

    public void printTree(Node root) {
        Stack<Node> stack = new Stack<>();
        Node node = root;

        while (stack.size() > 0 || node != null) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                node = stack.pop();
                System.out.println(node);
                node = node.right;
            }
        }
    }

    public static void main(String[] args) {
        CartesianTree ct = new CartesianTree();
        int[] array = new int[] {9, 3, 7, 1, 8, 12, 10, 20, 15, 18, 5};
        Node root = ct.buildTree(array);

        System.out.println("Tree's been created");

        // check if we keep an order
        ct.printTree(root);
    }
}
