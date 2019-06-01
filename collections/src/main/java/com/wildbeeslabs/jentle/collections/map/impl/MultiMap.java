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
package com.wildbeeslabs.jentle.collections.map.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Default multi map implementation
 *
 * @param <K> type of map key
 * @param <V> type of may value
 * @param <C> type of collection of values
 */
@Data
@EqualsAndHashCode
@ToString
public abstract class MultiMap<K, V, C extends Collection<V>> {
    protected final Map<K, C> m_objects;

    protected MultiMap(boolean isSorted) {
        if (isSorted) {
            m_objects = new LinkedHashMap<>();
        } else {
            m_objects = new HashMap<>();
        }
    }

    protected abstract C createValue();

    public boolean put(final K key, final V method) {
        boolean setExists = true;
        C l = m_objects.get(key);
        if (l == null) {
            setExists = false;
            l = createValue();
            m_objects.put(key, l);
        }
        return l.add(method) && setExists;
    }

    public C get(final K key) {
        C list = m_objects.get(key);
        if (list == null) {
            list = createValue();
            m_objects.put(key, list);
        }
        return list;
    }

    public List<K> getKeys() {
        return new ArrayList<>(keySet());
    }

    public Set<K> keySet() {
        return new HashSet<>(m_objects.keySet());
    }

    public boolean containsKey(final K k) {
        return m_objects.containsKey(k);
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int getSize() {
        return size();
    }

    public int size() {
        return m_objects.size();
    }

    public C remove(final K key) {
        return removeAll(key);
    }

    public boolean remove(final K key, final V value) {
        return get(key).remove(value);
    }

    public C removeAll(final K key) {
        return m_objects.remove(key);
    }

    public Set<Map.Entry<K, C>> getEntrySet() {
        return entrySet();
    }

    public Set<Map.Entry<K, C>> entrySet() {
        return m_objects.entrySet();
    }

    public Collection<C> getValues() {
        return values();
    }

    public Collection<C> values() {
        return m_objects.values();
    }

    public boolean putAll(final K k, final Collection<? extends V> values) {
        boolean result = false;
        for (final V v : values) {
            result = put(k, v) || result;
        }
        return result;
    }
}
