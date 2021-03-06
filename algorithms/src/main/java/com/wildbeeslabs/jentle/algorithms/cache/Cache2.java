package com.wildbeeslabs.jentle.algorithms.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides fixed size, pre-allocated, least recently used replacement cache.
 */
public class Cache2<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public Cache2(final int capacity) {
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.capacity;
    }
}
