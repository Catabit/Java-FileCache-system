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
            return hash.get(key).info.getValue();
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (hash.containsKey(key)) {
            Node r = hash.get(key);
            //remove it from the list
            if (r.prev != null) {
                r.prev.next = r.next;
            }
            if (r.next != null) {
                r.next.prev = r.prev;
            }

            //add it to the front of the list
            r.prev = null;
            r.next = first; //todo might be an issue if first is null
            first.prev = r;
            first = r;

        } else {
            Node newNode = new Node(new Pair<>(key, value));
            newNode.prev = null;
            newNode.next = first;
            if (first != null) {
                first.prev = newNode;
            }
            if (first == null && last == null) { // empty list
                last = newNode;
            }
            first = newNode;
        }

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
            Node r = hash.get(key);

            //remove it from the linked list
            if (r.prev != null) {
                r.prev.next = r.next;
            }
            if (r.next != null) {
                r.next.prev = r.prev;
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
        return last.info;
    }

    /* TODO: implement the methods from ObservableCache and Cache */

    private final class Node {
        private Node next, prev;

        private Pair<K, V> info;

        Node(final Pair<K, V> inInfo) {
            this.info = inInfo;
        }

        public Pair<K, V> getInfo() {
            return info;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }
    }
}


