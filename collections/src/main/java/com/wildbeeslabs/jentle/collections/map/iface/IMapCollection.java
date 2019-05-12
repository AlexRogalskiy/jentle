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
package com.wildbeeslabs.jentle.collections.map.iface;

import java.util.Iterator;
import java.util.Map;

/**
 * Custom {@link IMapLike} interface declaration
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IMapCollection<K, V> extends IMapLike<K, V>, Map<K, V> {

    /**
     * Returns keys {@link Iterator}
     *
     * @return keys {@link Iterator}
     */
    Iterator<? extends K> keysIterator();

    /**
     * Returns values {@link Iterator}
     *
     * @return values {@link Iterator}
     */
    Iterator<? extends V> valuesIterator();

    /**
     * Checks if the current queue is empty
     *
     * @return true - if the current queue is empty, false - otherwise
     */
    default boolean isEmpty() {
        return (0 == this.size());
    }
}
