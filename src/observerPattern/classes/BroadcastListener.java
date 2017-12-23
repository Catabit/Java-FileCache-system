package observerPattern.classes;

import cachingSystem.interfaces.Cache;
import observerPattern.interfaces.CacheListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The BroadcastListener broadcasts cache events to other listeners that have been added to it.
 */
public class BroadcastListener<K, V> implements CacheListener<K, V> {

    List<CacheListener<K, V>> listeners = new ArrayList<>();

    /**
     * Add a listener to the broadcast list.
     *
     * @param listener the listener
     */
    public void addListener(CacheListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void onHit(K key) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onHit(key);
        }
    }

    @Override
    public void onMiss(K key) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onMiss(key);
        }
    }

    @Override
    public void onPut(K key, V value) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onPut(key, value);
        }
    }

    /* TODO: implement the CacheListener interface */
}
