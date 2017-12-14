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

import com.wildbeeslabs.jentle.collections.interfaces.IMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
 * Custom hash map set implementation
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
public class CHashMapSet<T, E> implements IMap<T, E> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    protected final Map<T, Set<E>> map;

    public CHashMapSet() {
        this.map = new HashMap<>();
    }

    public void put(final T key, final E item) {
        if (!this.containsKey(key)) {
            this.map.put(key, new HashSet<>());
        }
        this.map.get(key).add(item);
    }

    public void put(final T key, final Set<E> items) {
        this.map.put(key, items);
    }

    public Set<E> get(final T key) {
        return this.map.get(key);
    }

    public boolean containsKey(final T key) {
        return this.map.containsKey(key);
    }

    public boolean containsKeyValue(final T key, final E value) {
        final Set<E> items = this.get(key);
        if (Objects.isNull(items)) {
            return false;
        }
        return items.contains(value);
    }

    public Set<T> keySet() {
        return this.map.keySet();
    }

    public Collection<Set<E>> values() {
        return this.map.values();
    }
}
