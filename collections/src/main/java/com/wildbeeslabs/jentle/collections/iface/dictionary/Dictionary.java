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
package com.wildbeeslabs.jentle.collections.iface.dictionary;

import com.wildbeeslabs.jentle.collections.iface.collection.Collection;
import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;

import java.util.Enumeration;

/**
 * Custom base dictionary interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface Dictionary<K, V> extends Collection<KeyValueNode<K, V>> {

    /**
     * Removes element {@code V} by input key {@code K}
     *
     * @param key - initial input key {@code K}
     * @return value {@code V} of element to remove
     */
    V removeElement(final K key);

    /**
     * Inserts element by input key {@code K} and value {@code V}
     *
     * @param key   - initial input key {@code K}
     * @param value - initial input value {@code V}
     */
    void insertElement(final K key, final V value);

    /**
     * Finds element value {@code V} by input key {@code K}
     *
     * @param key - initial input key {@code K}
     * @return value {@code V} of element to find
     */
    V findElement(final K key);

    /**
     * Returns {@link Enumeration} of keys
     *
     * @return {@link Enumeration} of keys
     */
    Enumeration<K> keys();

    /**
     * Returns {@link Enumeration} of values
     *
     * @return {@link Enumeration} of values
     */
    Enumeration<V> elements();
}