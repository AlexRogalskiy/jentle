package com.wildbeeslabs.jentle.collections.map.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utility to have 'intern' - like functionality, which holds single instance of wrapper for a given key
 */
public class InternMap<K, V> {
    private final ConcurrentMap<K, V> storage = new ConcurrentHashMap<K, V>();
    private final ValueConstructor<K, V> valueConstructor;

    public interface ValueConstructor<K, V> {
        V create(final K key);
    }

    public InternMap(final ValueConstructor<K, V> valueConstructor) {
        this.valueConstructor = valueConstructor;
    }

    public V interned(final K key) {
        V existingKey = this.storage.get(key);
        V newKey = null;
        if (Objects.isNull(existingKey)) {
            newKey = this.valueConstructor.create(key);
            existingKey = this.storage.putIfAbsent(key, newKey);
        }
        return Optional.ofNullable(existingKey).orElse(newKey);
    }

    public int size() {
        return this.storage.size();
    }
}
