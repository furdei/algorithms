package interview.array;

/**
 * Buffer is a FIFO array which holds items without moving items on add and remove operations
 */
public class Buffer<T> {

    private Object[] array;
    private int freeIndex = 0;
    private int usedIndex = 0;
    private int size = 0;

    public Buffer(int size) {
        array = new Object[size];
    }

    public void add(T item) {
        if (size == array.length) {
            throw new IllegalStateException("Buffer is full");
        }

        array[freeIndex++] = item;
        size++;

        if (freeIndex == array.length) {
            freeIndex = 0;
        }
    }

    public T remove() {
        if (size == 0) {
            throw new IllegalStateException("Buffer is empty");
        }

        T item = (T) array[usedIndex++];
        size--;

        if (usedIndex == array.length) {
            usedIndex = 0;
        }

        return item;
    }

    public static void main(String[] args) {
        Buffer<Integer> buffer = new Buffer<Integer>(3);

        System.out.println("Buffer.main Test 1");
        for (int i = 0; i < 3; i++) {
            buffer.add(i);
            System.out.println("Buffer.main added " + i);
        }
        for (int i = 0; i < 3; i++) {
            System.out.println("Buffer.main removed " + buffer.remove());
        }

        for (int i = 0; i < 3; i++) {
            buffer.add(i);
            System.out.println("Buffer.main added " + i);
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("Buffer.main removed " + buffer.remove());
        }
        for (int i = 0; i < 2; i++) {
            buffer.add(i);
            System.out.println("Buffer.main added " + i);
        }
        for (int i = 0; i < 3; i++) {
            System.out.println("Buffer.main removed " + buffer.remove());
        }
    }

}
