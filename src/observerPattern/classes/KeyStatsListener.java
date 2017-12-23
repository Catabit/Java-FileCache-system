package observerPattern.classes;

import java.util.List;

import observerPattern.interfaces.CacheListener;

/**
 * The KeyStatsListener collects key-level stats for cache operations.
 *
 * @param <K>
 * @param <V>
 */
public class KeyStatsListener<K, V> implements CacheListener<K, V> {


    @Override
    public void onHit(K key) {

    }

    @Override
    public void onMiss(K key) {

    }

    @Override
    public void onPut(K key, V value) {

    }

    /**
     * Get the number of hits for a key.
     *
     * @param key the key
     * @return number of hits
     */
    public int getKeyHits(K key) {
        /* TODO: implement getKeyHits */
        return 0;
    }

    /**
     * Get the number of misses for a key.
     *
     * @param key the key
     * @return number of misses
     */
    public int getKeyMisses(K key) {
        /* TODO: implement getKeyMisses */
        return 0;
    }

    /**
     * Get the number of updates for a key.
     *
     * @param key the key
     * @return number of updates
     */
    public int getKeyUpdates(K key) {
        /* TODO: implement getKeyUpdates */
        return 0;
    }

    /**
     * Get the @top most hit keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopHitKeys(int top) {
        /* TODO: implement getTopHitKeys */
        return null;
    }

    /**
     * Get the @top most missed keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopMissedKeys(int top) {
        /* TODO: implement getTopMissedKeys */
        return null;
    }

    /**
     * Get the @top most updated keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopUpdatedKeys(int top) {
        /* TODO: implement getTopUpdatedKeys */
        return null;
    }

    /* TODO: implement the CacheListener interface */
}
