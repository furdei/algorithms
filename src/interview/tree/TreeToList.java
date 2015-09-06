package interview.tree;

import java.util.Stack;

/**
 * Convert a binary tree to double linked list
 */
public class TreeToList<T> {

    public static class ListNode<T> {
        private T value;
        private ListNode<T> next;
        private ListNode<T> previous;

        public ListNode(T value) {
            this.value = value;
        }
    }

    public static class TreeNode<T> {
        private T value;
        private TreeNode<T> left;
        private TreeNode<T> right;

        public TreeNode(T value) {
            this.value = value;
        }
    }

    public ListNode<T> treeToList(TreeNode<T> root) {
        if (root == null) {
            return null;
        }

        ListNode<T> listFirst = null;
        ListNode<T> listLast = null;

        Stack<TreeNode<T>> discovered = new Stack<TreeNode<T>>();
        TreeNode<T> treeCurrent = root;

        while (discovered.size() > 0 || treeCurrent != null) {
            if (treeCurrent != null) {
                discovered.push(treeCurrent);
                treeCurrent = treeCurrent.left;
            } else {
                treeCurrent = discovered.pop();

                // add to the list
                ListNode<T> listNode = new ListNode<>(treeCurrent.value);
                if (listFirst == null) {
                    listFirst = listNode;
                    listLast = listNode;
                } else {
                    listNode.previous = listLast;
                    listLast.next = listNode;
                    listLast = listNode;
                }

                treeCurrent = treeCurrent.right;
            }
        }

        return listFirst;
    }

    public static void main(String[] args) {
        //      d
        //   b     e
        // a  c      f
        TreeNode<String> a = new TreeNode<>("a");
        TreeNode<String> b = new TreeNode<>("b");
        TreeNode<String> c = new TreeNode<>("c");
        TreeNode<String> d = new TreeNode<>("d");
        TreeNode<String> e = new TreeNode<>("e");
        TreeNode<String> f = new TreeNode<>("f");
        d.left = b;
        d.right = e;
        b.left = a;
        b.right = c;
        e.right = f;

        TreeToList<String> converter = new TreeToList<>();
        ListNode<String> list = converter.treeToList(d);
        ListNode<String> last = null;

        for (ListNode<String> node = list; node != null; node = node.next) {
            System.out.print(node.value + " ");
            last = node;
        }

        System.out.println("");

        for (ListNode<String> node = last; node != null; node = node.previous) {
            System.out.print(node.value + " ");
        }
    }

}
