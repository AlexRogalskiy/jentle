package com.wildbeeslabs.jentle.collections.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
public class CHashMapList<T, E> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

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
        if (Objects.isNull(items)) {
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
}
