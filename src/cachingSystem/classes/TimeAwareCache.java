package cachingSystem.classes;

import cachingSystem.interfaces.CacheStalePolicy;
import dataStructures.classes.Pair;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

/**
 * The TimeAwareCache offers the same functionality as the LRUCache, but also stores a timestamp for
 * each element. The timestamp is updated after each get / put operation for a key. This
 * functionality allows for time based cache stale policies (e.g. removing entries that are older
 * than 1 second).
 */
public class TimeAwareCache<K, V> extends LRUCache<K, V> {

    private HashMap<K, Timestamp> times = new HashMap<>();

    @Override
    public void put(K key, V value) {
        super.put(key, value);
        setTimestampOfKey(key, Timestamp.from(Instant.now()));
    }

    @Override
    public V remove(K key) {
        times.remove(key);
        return super.remove(key);
    }

    /**
     * Get the timestamp associated with a key, or null if the key is not stored in the cache.
     *
     * @param key the key
     * @return the timestamp, or null
     */
    public Timestamp getTimestampOfKey(K key) {
        return times.get(key);
    }

    private void setTimestampOfKey(K key, Timestamp t) {
        times.put(key, t);
    }

    /**
     * Set a cache stale policy that should remove all elements older than @millisToExpire
     * milliseconds. This is a convenience method for setting a time based policy for the cache.
     *
     * @param millisToExpire the expiration time, in milliseconds
     */
    public void setExpirePolicy(long millisToExpire) {
        CacheStalePolicy<K, V> stalePolicy = new CacheStalePolicy<K, V>() {
            @Override
            public boolean shouldRemoveEldestEntry(Pair<K, V> entry) {
                long treshHoldTime = Timestamp.from(Instant.now()).getTime();
                treshHoldTime -= millisToExpire; //todo might require <=
                if (getTimestampOfKey(entry.getKey()).getTime() < treshHoldTime) {
                    return true;
                }
                return false;
            }
        };
        setStalePolicy(stalePolicy);
    }
}
