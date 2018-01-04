package cachingSystem;

import cachingSystem.classes.ObservableCache;
import cachingSystem.classes.ObservableFIFOCache;
import cachingSystem.classes.LRUCache;
import cachingSystem.classes.TimeAwareCache;
import cachingSystem.interfaces.CacheStalePolicy;
import dataStructures.classes.Pair;
import observerPattern.classes.BroadcastListener;
import observerPattern.interfaces.CacheListener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public final class FileCache {

    public enum Strategy {
        FIFO,
        LRU,
    }

    public static cachingSystem.FileCache createCacheWithCapacity(
            final cachingSystem.FileCache.Strategy strategy,
            final int capacity) {
        ObservableCache<String, String> dataCache;

        switch (strategy) {

            case FIFO:
                dataCache = new ObservableFIFOCache<>();
                break;
            case LRU:
                dataCache = new LRUCache<>();
                break;
            default:
                throw new IllegalArgumentException("Unsupported cache strategy: " + strategy);
        }

        dataCache.setStalePolicy(new CacheStalePolicy<String, String>() {
            @Override
            public boolean shouldRemoveEldestEntry(final Pair<String, String> entry) {
                return dataCache.size() > capacity;
            }
        });

        return new cachingSystem.FileCache(dataCache);
    }

    public static cachingSystem.FileCache createCacheWithExpiration(final long millisToExpire) {
        TimeAwareCache<String, String> dataCache = new TimeAwareCache<>();

        dataCache.setExpirePolicy(millisToExpire);

        return new cachingSystem.FileCache(dataCache);
    }

    private FileCache(final ObservableCache<String, String> dataCache) {
        this.dataCache = dataCache;
        this.broadcastListener = new BroadcastListener<>();

        this.dataCache.setCacheListener(broadcastListener);

        broadcastListener.addListener(createCacheListener());
    }

    private CacheListener<String, String> createCacheListener() {
        return new CacheListener<String, String>() {

            @Override
            public void onHit(final String key) {
            }

            @Override
            public void onMiss(final String key) {
                try {
                    Reader fr = new FileReader(key);
                    StringBuilder sb = new StringBuilder();
                    int next;
                    next = fr.read();
                    while (next != -1) {
                        sb.append((char) next);
                        next = fr.read();
                    }
                    dataCache.put(key, sb.toString());
                } catch (FileNotFoundException excp) {
                    System.out.println("File not found!");
                } catch (java.io.IOException excp) {
                    System.out.println("IO Exception!");
                }
            }

            @Override
            public void onPut(final String key, final String value) {
            }
        };
    }

    public String getFileContents(final String path) {
        String fileContents;

        do {
            fileContents = dataCache.get(path);
        } while (fileContents == null);

        return fileContents;
    }

    public void putFileContents(final String path, final String contents) {
        dataCache.put(path, contents);
    }

    public void addListener(final CacheListener<String, String> listener) {
        broadcastListener.addListener(listener);
    }

    private ObservableCache<String, String> dataCache;
    private BroadcastListener<String, String> broadcastListener;
}
