package observerPattern.classes;

import observerPattern.interfaces.CacheListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The BroadcastListener broadcasts cache events to other listeners that have been added to it.
 */
public class BroadcastListener<K, V> implements CacheListener<K, V> {

    private List<CacheListener<K, V>> listeners = new ArrayList<>();

    /**
     * Add a listener to the broadcast list.
     *
     * @param listener the listener
     */
    public void addListener(final CacheListener<K, V> listener) {
        listeners.add(listener);
    }

    /**
     * Broadcasts the onHit event to the subscribed listeners.
     *
     * @param key the key on which onHit happened
     */
    @Override
    public void onHit(final K key) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onHit(key);
        }
    }

    /**
     * Broadcasts the onMiss event to the subscribed listeners.
     *
     * @param key the key on which onMiss happened
     */
    @Override
    public void onMiss(final K key) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onMiss(key);
        }
    }

    /**
     * Broadcasts the onPut event to the subscribed listeners.
     *
     * @param key the key on which onPut happened
     */
    @Override
    public void onPut(final K key, final V value) {
        for (CacheListener<K, V> listener : listeners) {
            listener.onPut(key, value);
        }
    }
}
