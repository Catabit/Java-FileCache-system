package cachingSystem.classes;

import cachingSystem.interfaces.CacheStalePolicy;
import dataStructures.classes.Pair;

import java.sql.Timestamp;
import java.util.TreeMap;

/**
 * The TimeAwareCache offers the same functionality as the LRUCache, but also stores a timestamp for
 * each element. The timestamp is updated after each get / put operation for a key. This
 * functionality allows for time based cache stale policies (e.g. removing entries that are older
 * than 1 second).
 */
public class TimeAwareCache<K, V> extends LRUCache<K, V> {

    private TreeMap<K, Timestamp> times = new TreeMap<>();

    /**
     * Inserts the new key-value pair if it does not exist or it updates the contents. Either way
     * the item will be considered the most recently used and will be moved to the front of the
     * list. A timestamp is added or updated for the said pair.
     * Sends onPut.
     *
     * @param key   the key
     * @param value the value
     */
    @Override
    public void put(final K key, final V value) {
        if (!times.containsKey(key)) {
            setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
            super.put(key, value);
        } else {
            super.put(key, value);
            setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
        }
        clearStaleEntries();
    }

    /**
     * Removes the key-value pair.
     *
     * @param key the key to be removed
     * @return the value
     */
    @Override
    public V remove(final K key) {
        times.remove(key);
        return super.remove(key);
    }

    /**
     * Gets the value assigned to the key and moves the item to the front of the list so that it is
     * not removed (not considered stale). Updates the timestamp assigned to the key.
     * Sends out a onMiss event if the key is not found, and an onHit if it is found.
     *
     * @param key the key to lookup
     * @return the value
     */
    @Override
    public V get(final K key) {
        if (!isEmpty()) {
            clearStaleEntries();
        }
        setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
        return super.get(key);
    }


    /**
     * Get the timestamp associated with a key, or null if the key is not stored in the cache.
     *
     * @param key the key
     * @return the timestamp, or null
     */
    public Timestamp getTimestampOfKey(final K key) {
        return times.get(key);
    }

    private void setTimestampOfKey(final K key, final Timestamp t) {
        times.put(key, t);
    }

    /**
     * Set a cache stale policy that should remove all elements older than @millisToExpire
     * milliseconds. This is a convenience method for setting a time based policy for the cache.
     *
     * @param millisToExpire the expiration time, in milliseconds
     */
    public void setExpirePolicy(final long millisToExpire) {
        CacheStalePolicy<K, V> stalePolicy = new CacheStalePolicy<K, V>() {
            @Override
            public boolean shouldRemoveEldestEntry(final Pair<K, V> entry) {
                if (entry == null) {
                    return false;
                }
                return System.currentTimeMillis() - getTimestampOfKey(entry.getKey()).getTime()
                        > millisToExpire;
            }
        };
        setStalePolicy(stalePolicy);
    }
}
