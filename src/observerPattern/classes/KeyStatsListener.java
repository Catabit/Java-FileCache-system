package observerPattern.classes;

import java.util.*;

import observerPattern.interfaces.CacheListener;

/**
 * The KeyStatsListener collects key-level stats for cache operations.
 *
 * @param <K>
 * @param <V>
 */
public class KeyStatsListener<K, V> implements CacheListener<K, V> {

    private TreeMap<K, Integer> hits = new TreeMap<>(Collections.reverseOrder());
    private TreeMap<K, Integer> misses = new TreeMap<>(Collections.reverseOrder());
    private TreeMap<K, Integer> puts = new TreeMap<>(Collections.reverseOrder());

    @Override
    public void onHit(K key) {
        int count;
        if (hits.containsKey(key)) {
            count = hits.get(key) + 1;
        } else {
            count = 1;
        }
        hits.put(key, count);
    }

    @Override
    public void onMiss(K key) {
        int count;
        if (misses.containsKey(key)) {
            count = misses.get(key) + 1;
        } else {
            count = 1;
        }
        misses.put(key, count);

    }

    @Override
    public void onPut(K key, V value) {
        int count;
        if (puts.containsKey(key)) {
            count = puts.get(key) + 1;
        } else {
            count = 1;
        }
        puts.put(key, count);
    }

    /**
     * Get the number of hits for a key.
     *
     * @param key the key
     * @return number of hits
     */
    public int getKeyHits(K key) {
        return hits.get(key);
    }

    /**
     * Get the number of misses for a key.
     *
     * @param key the key
     * @return number of misses
     */
    public int getKeyMisses(K key) {
        return misses.get(key);
    }

    /**
     * Get the number of updates for a key.
     *
     * @param key the key
     * @return number of updates
     */
    public int getKeyUpdates(K key) {
        return puts.get(key);
    }

    /**
     * Get the @top most hit keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopHitKeys(int top) {
        List<K> keys = new ArrayList<>();
        Iterator<Map.Entry<K, Integer>> iter = hits.entrySet().iterator();
        for (int i = 0; i < top; i++) {
            keys.add(iter.next().getKey());
        }
        return keys;
    }

    /**
     * Get the @top most missed keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopMissedKeys(int top) {
        List<K> keys = new ArrayList<>();
        Iterator<Map.Entry<K, Integer>> iter = misses.entrySet().iterator();
        for (int i = 0; i < top; i++) {
            keys.add(iter.next().getKey());
        }
        return keys;
    }

    /**
     * Get the @top most updated keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopUpdatedKeys(int top) {
        List<K> keys = new ArrayList<>();
        Iterator<Map.Entry<K, Integer>> iter = puts.entrySet().iterator();
        for (int i = 0; i < top; i++) {
            keys.add(iter.next().getKey());
        }
        return keys;
    }

    /* TODO: implement the CacheListener interface */
}
