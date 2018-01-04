package cachingSystem.classes;

import dataStructures.classes.Pair;

/**
 * Class that adapts the FIFOCache class to the ObservableCache abstract class.
 */
public class ObservableFIFOCache<K, V> extends ObservableCache<K, V> {


    private FIFOCache<K, V> cache = new FIFOCache<>();

    /**
     * Returns the value assigned to the input key or null. Sens onHit if the item is found or
     * onMiss if it is not.
     *
     * @param key the key to lookup
     * @return the value
     */
    @Override
    public V get(final K key) {
        V result = cache.get(key);
        if (result == null) {
            cacheListener.onMiss(key);
        } else {
            cacheListener.onHit(key);
        }
        return result;
    }

    /**
     * Puts the new key-value pair in the Cache (or updates existing), clears the stale elements and
     * sends onPut.
     *
     * @param key   the key
     * @param value the value
     */
    @Override
    public void put(final K key, final V value) {
        cache.put(key, value);
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
        return cache.size();
    }

    /**
     * Returns true if the Cache contains no elements.
     *
     * @return empty status: True/False
     */
    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Removes the key-value pair.
     *
     * @param key the key to be removed
     * @return the value
     */
    @Override
    public V remove(final K key) {
        return cache.remove(key);
    }

    /**
     * Clears the cache.
     */
    @Override
    public void clearAll() {
        cache.clearAll();
    }


    /**
     * Gets the eldest item in the Cache.
     *
     * @return the eldest pair.
     */
    @Override
    public Pair<K, V> getEldestEntry() {
        return cache.getEldestEntry();
    }
}
