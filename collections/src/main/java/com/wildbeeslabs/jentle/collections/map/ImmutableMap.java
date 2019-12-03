package com.wildbeeslabs.jentle.collections.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Immutable maps.
 *
 * @author Martin Kouba
 */
public final class ImmutableMap {

    /**
     * @param map
     * @return an immutable copy of the given map
     */
    public static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        if (map.isEmpty()) {
            return Collections.emptyMap();
        }
        if (map.size() == 1) {
            Entry<K, V> entry = map.entrySet().iterator().next();
            return Collections.singletonMap(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(new HashMap<>(map));
    }

    /**
     * @param key1
     * @param value1
     * @return an immutable singleton map
     */
    public static <K, V> Map<K, V> of(K key, V value) {
        return Collections.singletonMap(key, value);
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return an immutable map for the specified keys and values
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2) {
        return ImmutableMap.<K, V>builder().put(key1, value1).put(key2, value2)
            .build();
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @return an immutable map for the specified keys and values
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2,
                                      K key3, V value3) {
        return ImmutableMap.<K, V>builder().put(key1, value1).put(key2, value2)
            .put(key3, value3).build();
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @param key4
     * @param value4
     * @return an immutable map for the specified keys and values
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2,
                                      K key3, V value3, K key4, V value4) {
        return ImmutableMap.<K, V>builder().put(key1, value1).put(key2, value2)
            .put(key3, value3).put(key4, value4).build();
    }

    /**
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @param key4
     * @param value4
     * @param key5
     * @param value5
     * @return an immutable map for the specified keys and values
     */
    public static <K, V> Map<K, V> of(K key1, V value1, K key2, V value2,
                                      K key3, V value3, K key4, V value4, K key5, V value5) {
        return ImmutableMap.<K, V>builder().put(key1, value1).put(key2, value2)
            .put(key3, value3).put(key4, value4).put(key5, value5).build();
    }

    /**
     * @return an immutable map builder
     */
    public static <K, V> ImmutableMapBuilder<K, V> builder() {
        return new ImmutableMapBuilder<>();
    }

    public static final class ImmutableMapBuilder<K, V> {

        private Map<K, V> entries;

        private ImmutableMapBuilder() {
            this.entries = new HashMap<>();
        }

        public ImmutableMapBuilder<K, V> put(K key, V value) {
            entries.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return copyOf(entries);
        }
    }
}
