package cachingSystem.classes;

import dataStructures.classes.Pair;
import java.util.HashMap;

/**
 * This cache is very similar to the FIFOCache, but guarantees O(1) complexity for the get, put and
 * remove operations.
 */
public class LRUCache<K, V> extends ObservableCache<K, V> {

    private Node first = null;
    private Node last = null;

    private HashMap<K, Node> hash = new HashMap<>();

    @Override
    public V get(K key) {
        if (hash.containsKey(key)) {
            V result = hash.get(key).info.getValue();
            cacheListener.onHit(key);

            Node r = hash.get(key);

            if (r == first) {
                return result;
            }

            Node prev = r.prev;
            Node next = r.next;

            if (r == last) {
                prev.next = null;
                last = prev;
            } else {
                prev.next = next;
                next.prev = prev;
            }

            r.next = first;
            first.prev = r;
            first = r;
            r.prev = null;

            return result;
        }
        cacheListener.onMiss(key);
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (hash.containsKey(key)) {
            hash.get(key).info.setValue(value);

            Node r = hash.get(key);

            if (r != first) {

                Node prev = r.prev;
                Node next = r.next;

                if (r == last) {
                    prev.next = null;
                    last = prev;
                } else {
                    prev.next = next;
                    next.prev = prev;
                }

                r.next = first;
                first.prev = r;
                first = r;
                r.prev = null;
            }
        } else {
            Node newNode = new Node(new Pair<>(key, value));
            newNode.prev = null;
            newNode.next = first;
            if (first != null) {
                first.prev = newNode;
            }
            first = newNode;

            hash.put(key, newNode);
            if (size() == 1) {
                last = newNode;
            }
        }

        clearStaleEntries();
        cacheListener.onPut(key, value);
    }

    @Override
    public int size() {
        return hash.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public V remove(K key) {
        V result = null;
        if (hash.containsKey(key)) {
            result = hash.get(key).info.getValue();

            last = last.prev;
            if (last != null) {
                last.next = null;
            }

            hash.remove(key);
        }
        return result;
    }

    @Override
    public void clearAll() {
        first = null;
        last = null;
        hash.clear();
    }

    @Override
    public Pair<K, V> getEldestEntry() {
        if (last != null) {
            return last.info;
        }
        return null;
    }


    private final class Node {
        private Node next, prev;

        private Pair<K, V> info;

        Node(final Pair<K, V> inInfo) {
            this.info = inInfo;
        }
    }
}


