package observerPattern.classes;

import observerPattern.interfaces.CacheListener;

/**
 * The StatsListener collects hit / miss / update stats for a cache.
 *
 * @param <K>
 * @param <V>
 */
public class StatsListener<K, V> implements CacheListener<K, V> {

    private int hits = 0;
    private int misses = 0;
    private int puts = 0;

    /**
     * Increments the total hits counter for the cache.
     *
     * @param key the key
     */
    @Override
    public void onHit(final K key) {
        hits++;
    }

    /**
     * Increments the total misses counter for the cache.
     *
     * @param key the key
     */
    @Override
    public void onMiss(final K key) {
        misses++;
    }

    /**
     * Increments the total puts counter for the cache.
     *
     * @param key the key
     */
    @Override
    public void onPut(final K key, final V value) {
        puts++;
    }

    /**
     * Get the number of hits for the cache.
     *
     * @return number of hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * Get the number of misses for the cache.
     *
     * @return number of misses
     */
    public int getMisses() {
        return misses;
    }

    /**
     * Get the number of updates (put operations) for the cache.
     *
     * @return number of updates
     */
    public int getUpdates() {
        return puts;
    }

}
