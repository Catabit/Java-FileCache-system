package cachingSystem.classes;

import cachingSystem.interfaces.CacheStalePolicy;
import dataStructures.classes.Pair;

import java.sql.Timestamp;
import java.util.*;

/**
 * The TimeAwareCache offers the same functionality as the LRUCache, but also stores a timestamp for
 * each element. The timestamp is updated after each get / put operation for a key. This
 * functionality allows for time based cache stale policies (e.g. removing entries that are older
 * than 1 second).
 */
public class TimeAwareCache<K, V> extends LRUCache<K, V> {

    private TreeMap<K, Timestamp> times = new TreeMap<>();

    @Override
    public void put(K key, V value) {
        if (!times.containsKey(key)) {
            setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
            super.put(key, value);
        } else {
            super.put(key, value);
            setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
        }
        clearStaleEntries();
    }

    @Override
    public V remove(K key) {
        times.remove(key);
        return super.remove(key);
    }

    @Override
    public V get(K key) {
        if (!isEmpty())
            clearStaleEntries();
        //setTimestampOfKey(key, new Timestamp(System.currentTimeMillis()));
        return super.get(key);
    }

    /*@Override
    public Pair<K, V> getEldestEntry() {
        List<Map.Entry<K, Timestamp>> list = new LinkedList<>(times.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, Timestamp>>() {
            @Override
            public int compare(Map.Entry<K, Timestamp> o1, Map.Entry<K, Timestamp> o2) {
                return (int) (o1.getValue().getTime() - o2.getValue().getTime());
            }
        });
        //System.out.println("ELDEST " + list.get(0).getKey());
        return new Pair<>(list.get(0).getKey(), get(list.get(0).getKey()));
    }*/

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
                // System.out.println(getTimestampOfKey(entry.getKey()).getTime() + " - " + System.currentTimeMillis() +
                //         " = " + (getTimestampOfKey(entry.getKey()).getTime() - System.currentTimeMillis()));
                // System.out.println(millisToExpire);
                if (entry == null) {
                    return false;
                }
                if (System.currentTimeMillis() - getTimestampOfKey(entry.getKey()).getTime() > millisToExpire) {
                    return true;
                }
                return false;
            }
        };
        setStalePolicy(stalePolicy);
    }
}
