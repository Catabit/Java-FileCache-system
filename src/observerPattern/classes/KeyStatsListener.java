package observerPattern.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        return getFirstEntries(hits, top);
    }

    /**
     * Get the @top most missed keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopMissedKeys(int top) {
        return getFirstEntries(misses, top);
    }

    /**
     * Get the @top most updated keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopUpdatedKeys(int top) {
        return getFirstEntries(puts, top);
    }


    private List<K> getFirstEntries(final TreeMap<K, Integer> map, final int top) {
        List<K> keys = new ArrayList<>();

        List<Map.Entry<K, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comp());
        Iterator<Map.Entry<K, Integer>> iter = list.iterator();

        for (int i = 0; i < top; i++) {
            keys.add(iter.next().getKey());
        }
        return keys;
    }


    private class Comp implements Comparator<Map.Entry<K, Integer>> {
        public int compare(Map.Entry<K, Integer> o1, Map.Entry<K, Integer> o2) {
            return (-1) * (o1.getValue() - o2.getValue());
        }
    }
}
