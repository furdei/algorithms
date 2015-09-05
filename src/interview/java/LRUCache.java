package interview.java;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implement LRU Cache
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 8;
    private final int capacity;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> lruCache = new LRUCache<>(4);
        lruCache.put(1, "one");
        lruCache.put(2, "two");
        lruCache.put(3, "three");
        lruCache.put(4, "four");
        lruCache.put(5, "five");

        for (Map.Entry<Integer, String> entry : lruCache.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }

}
