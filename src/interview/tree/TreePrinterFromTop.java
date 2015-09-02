package interview.tree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created on 8/7/2015
 *
 * 0             A
 *             /  \ \
 * 1          B    C D
 *          /  \   \
 * 2       E   F    G
 *              \
 * 3            I
 *
 * Result: I E F G B C D A
 *
 * @author Stepan.Furdey
 */
public class TreePrinterFromTop<T> {

    private static class Node<T> {
        private T data;
        private Collection<Node<T>> children;

        public Node(T data) {
            this.data = data;
            children = new LinkedList<>();
        }

        public Collection<Node<T>> getChildren() {
            return children;
        }

        public void addChild(Node<T> child) {
            children.add(child);
        }
    }

    public void printTheTreeFromTop(Node<T> root) {
        if (root == null) {
            return;
        }

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);

        while (queue.size() > 0) {
            Node<T> current = queue.remove();

            for (Node<T> child : current.getChildren()) {
                queue.add(child);
            }

            System.out.print(current.data.toString() + " ");
        }
    }

    public static void main(String[] args) {
        Node<String> a = new Node<>("A");
        Node<String> b = new Node<>("B");
        Node<String> c = new Node<>("C");
        Node<String> d = new Node<>("D");
        Node<String> e = new Node<>("E");
        Node<String> f = new Node<>("F");
        Node<String> g = new Node<>("G");
        Node<String> h = new Node<>("H");
        a.addChild(b);
        a.addChild(c);
        b.addChild(d);
        b.addChild(e);
        c.addChild(f);
        c.addChild(g);
        e.addChild(h);

        TreePrinterFromTop treePrinter = new TreePrinterFromTop();
        treePrinter.printTheTreeFromTop(a);
    }

}
