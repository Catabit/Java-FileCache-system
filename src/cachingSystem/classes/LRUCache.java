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

    /**
     * Gets the value assigned to the key and moves the item to the front of the list so that it is
     * not removed (not considered stale).
     * Sends out a onMiss event if the key is not found, and an onHit if it is found.
     *
     * @param key the key to lookup
     * @return the value
     */
    @Override
    public V get(final K key) {
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

    /**
     * Inserts the new key-value pair if it does not exist or it updates the contents. Either way
     * the item will be considered the most recently used and will be moved to the front of the
     * list.
     * Sends onPut.
     *
     * @param key   the key
     * @param value the value
     */
    @Override
    public void put(final K key, final V value) {
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

    /**
     * Returns the size of the Cache.
     *
     * @return the size
     */
    @Override
    public int size() {
        return hash.size();
    }

    /**
     * Returns true if the Cache contains no elements.
     *
     * @return empty status: True/False
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Removes the key-value pair.
     *
     * @param key the key to be removed
     * @return the value
     */
    @Override
    public V remove(final K key) {
        V result = null;
        if (hash.containsKey(key)) {
            result = hash.get(key).info.getValue();

            if (last == hash.get(key)) {
                last = last.prev;
                if (last != null) {
                    last.next = null;
                }
            } else if (first == hash.get(key)) {
                first = first.next;
                if (first != null) {
                    first.prev = null;
                }
            } else {
                Node n = hash.get(key);
                n.prev.next = n.next;
                n.next.prev = n.prev;
            }

            hash.remove(key);
        }
        return result;
    }

    /**
     * Clears the cache.
     */
    @Override
    public void clearAll() {
        first = null;
        last = null;
        hash.clear();
    }

    /**
     * Gets the least recently used item in the Cache.
     *
     * @return the least recently used pair.
     */
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


