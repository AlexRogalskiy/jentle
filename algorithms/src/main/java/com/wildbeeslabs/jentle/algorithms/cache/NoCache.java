package com.wildbeeslabs.jentle.algorithms.cache;

/**
 * Cache2 implementation that does absolutely nothing. Objects aren't cached, making it a special case implementation
 * for
 * the case when caching is disabled.
 *
 * @author Allard Buijze
 * @since 0.3
 */
public final class NoCache implements Cache {

    /**
     * Creates a singleton reference the the NoCache implementation.
     */
    public static final NoCache INSTANCE = new NoCache();

    private NoCache() {
    }

    @Override
    public <K, V> V get(K key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
    }

    @Override
    public boolean putIfAbsent(Object key, Object value) {
        return true;
    }

    @Override
    public boolean remove(Object key) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public void registerCacheEntryListener(EntryListener cacheEntryListener) {
    }
}
