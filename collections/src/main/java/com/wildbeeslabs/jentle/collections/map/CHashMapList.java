/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.collections.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom hash map list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CHashMapList<K, V> extends ACBaseExtended<K, V, List<V>> {

    /**
     * Default map size
     */
    public static final Integer DEFAULT_MAP_SIZE = 10;

    protected final Map<K, List<V>> map;

    public CHashMapList() {
        this(CHashMapList.DEFAULT_MAP_SIZE);
    }

    public CHashMapList(int size) {
        assert (size > 0);
        this.map = new HashMap<>(size);
    }

    @Override
    public void put(final K key, final V item) {
        if (!this.containsKey(key)) {
            this.map.put(key, Collections.EMPTY_LIST);
        }
        this.map.get(key).add(item);
    }

    @Override
    public void put(final K key, final List<V> items) {
        this.map.put(key, items);
    }

    @Override
    public List<V> get(final K key) {
        return this.map.get(key);
    }

    public Set<V> getAsSet(final K key) {
        return this.toSet(this.get(key));
    }

    @Override
    public boolean containsKey(final K key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsKeyValue(final K key, final V value) {
        final List<V> items = this.get(key);
        if (Objects.isNull(items)) {
            return false;
        }
        return items.contains(value);
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return this.map.values();
    }

    public Set<V> valueSet() {
        return this.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
