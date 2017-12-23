package cachingSystem.classes;

import cachingSystem.interfaces.Cache;
import cachingSystem.interfaces.CacheStalePolicy;
import observerPattern.interfaces.CacheListener;

/**
 * Abstract class that adds support for listeners and stale element policies to the Cache
 * interface.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ObservableCache<K, V> implements Cache<K, V> {

    private CacheStalePolicy<K, V> stalePolicy;
    private CacheListener<K, V> cacheListener;

    /**
     * Set a policy for removing stale elements from the cache.
     *
     * @param stalePolicy
     */
    public void setStalePolicy(CacheStalePolicy<K, V> stalePolicy) {
        this.stalePolicy = stalePolicy;
    }

    /**
     * Set a listener for the cache.
     *
     * @param cacheListener
     */
    public void setCacheListener(CacheListener<K, V> cacheListener) {
        this.cacheListener = cacheListener;
    }

    /**
     * Clear the stale elements from the cache. This method must make use of the stale policy.
     *
     */
    public void clearStaleEntries() {
        while (stalePolicy.shouldRemoveEldestEntry(getEldestEntry())) {
            remove(getEldestEntry().getKey());
        }
    }
}
