package cachingSystem.classes;

import dataStructures.classes.Pair;

/**
 * Class that adapts the FIFOCache class to the ObservableCache abstract class.
 */
public class ObservableFIFOCache<K, V> extends ObservableCache<K, V> {

    /* TODO: implement the methods from ObservableCache and Cache */

    /* TODO: when adding a new key (the put method), don't forget to call clearStaleEntries */

    private FIFOCache<K, V> cache = new FIFOCache<>();

    @Override
    public V get(K key) {
        V result = cache.get(key);
        if (result == null) {
            cacheListener.onMiss(key);
        } else {
            cacheListener.onHit(key);
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        clearStaleEntries();
        cache.put(key, value);
        cacheListener.onPut(key, value);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public V remove(K key) {
        return cache.remove(key);
    }

    @Override
    public void clearAll() {
        cache.clearAll();
    }

    @Override
    public Pair<K, V> getEldestEntry() {
        return cache.getEldestEntry();
    }
}
