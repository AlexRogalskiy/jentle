package com.wildbeeslabs.jentle.collections.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom hash-map-list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @param <T>
 * @param <E>
 * @since 2017-08-07
 */
public class CHashMapList<T, E> {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CHashMapList.class);

    protected final Map<T, ArrayList<E>> map;

    public CHashMapList() {
        this.map = new HashMap<>();
    }

    public void put(final T key, final E item) {
        if (!this.containsKey(key)) {
            this.map.put(key, new ArrayList<>());
        }
        this.map.get(key).add(item);
    }

    public void put(final T key, final ArrayList<E> items) {
        this.map.put(key, items);
    }

    public ArrayList<E> get(final T key) {
        return this.map.get(key);
    }

    public boolean containsKey(final T key) {
        return this.map.containsKey(key);
    }

    public boolean containsKeyValue(final T key, final E value) {
        ArrayList<E> items = this.get(key);
        if (null == items) {
            return false;
        }
        return items.contains(value);
    }

    public Set<T> keySet() {
        return this.map.keySet();
    }

    public Collection<ArrayList<E>> values() {
        return this.map.values();
    }

    @Override
    public String toString() {
        return String.format("CHashMapList {map: %s}", this.map.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CHashMapList<T, E> other = (CHashMapList<T, E>) obj;
        if (!Objects.equals(this.map, other.map)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.map);
        return hash;
    }
}
