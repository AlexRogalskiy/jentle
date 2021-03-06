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
