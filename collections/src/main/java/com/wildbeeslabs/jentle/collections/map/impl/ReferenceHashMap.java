package com.wildbeeslabs.jentle.collections.map.impl;

import java.util.*;

public class ReferenceHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private final Map<K, Identical<V>> map = new HashMap<>();

    private static class Identical<T> {
        final T held;

        Identical(T hold) {
            held = hold;
        }

        @Override
        public boolean equals(Object it) {
            return it != null && held == it;
        }

        @Override
        public int hashCode() {
            return held.hashCode();
        }
    }

    @Override
    public V get(Object key) {
        Identical<V> value = map.get(key);
        return value == null ? null : value.held;
    }

    @Override
    public V put(K key, V value) {
        Identical<V> replaced = map.put(key, new Identical<>(value));
        return replaced == null ? null : replaced.held;
    }

    private class MyEntry implements Map.Entry<K, V> {

        private final K key;
        private V value;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (Entry<K, Identical<V>> entry : map.entrySet()) {
            entries.add(new MyEntry(entry.getKey(), entry.getValue().held));
        }
        return entries;
    }
}
