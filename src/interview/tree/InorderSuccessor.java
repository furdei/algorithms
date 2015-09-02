package interview.tree;

/**
 * Find inorder successor in a binary search tree
 *
 *                       10
 *                      /  \
 *                     5    15
 *                    / \
 *                   3   8
 */
public class InorderSuccessor {

    public static class Node {
        int data;
        Node left;
        Node right;

        public Node(int i) {
            data = i;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }

    public Node findInorderSuccessor(Node root, Node search) {
        return findInorderSuccessor(root, search, null);
    }

    private Node findInorderSuccessor(Node current, Node search, Node lastLeftTurn) {
        if (current == null || search == null) {
            return null;
        }

        if (current.data == search.data) {
            // inorder successor is the minimum in the right subtree, if right subtree exists
            if (current.right != null) {
                return findMin(current.right);
            }

            // there is no right subtree
            return lastLeftTurn;
        }

        if (search.data < current.data) {
            return findInorderSuccessor(current.left, search, current);
        } else {
            return findInorderSuccessor(current.right, search, lastLeftTurn);
        }
    }

    private Node findMin(Node current) {
        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    public static void main(String[] args) {
        InorderSuccessor inorderSuccessor = new InorderSuccessor();
        Node n3 = new Node(3);
        Node n5 = new Node(5);
        Node n8 = new Node(8);
        Node n10 = new Node(10);
        Node n15 = new Node(15);
        n5.left = n3;
        n5.right = n8;
        n10.left = n5;
        n10.right = n15;
        Node root = n10;

        System.out.println("InorderSuccessor.main s(3)=" + inorderSuccessor.findInorderSuccessor(root, new Node(3)));
        System.out.println("InorderSuccessor.main s(5)=" + inorderSuccessor.findInorderSuccessor(root, new Node(5)));
        System.out.println("InorderSuccessor.main s(8)=" + inorderSuccessor.findInorderSuccessor(root, new Node(8)));
        System.out.println("InorderSuccessor.main s(10)=" + inorderSuccessor.findInorderSuccessor(root, new Node(10)));
        System.out.println("InorderSuccessor.main s(15)=" + inorderSuccessor.findInorderSuccessor(root, new Node(15)));
    }

}
