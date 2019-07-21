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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clients of this class should only use the  {@link #getMessageCountAndThenIncrement} method. Other methods inherited
 * via LinkedHashMap are not thread safe.
 */
public class LRUMessageCache extends LinkedHashMap<String, Integer> {

    private static final long serialVersionUID = 1L;
    final int cacheSize;

    LRUMessageCache(int cacheSize) {
        super((int) (cacheSize * (4.0f / 3)), 0.75f, true);
        if (cacheSize < 1) {
            throw new IllegalArgumentException("Cache size cannot be smaller than 1");
        }
        this.cacheSize = cacheSize;
    }

    int getMessageCountAndThenIncrement(String msg) {
        // don't insert null elements
        if (msg == null) {
            return 0;
        }

        Integer i;
        // LinkedHashMap is not LinkedHashMap. See also LBCLASSIC-255
        synchronized (this) {
            i = super.get(msg);
            if (i == null) {
                i = 0;
            } else {
                i = i + 1;
            }
            super.put(msg, i);
        }
        return i;
    }

    // called indirectly by get() or put() which are already supposed to be
    // called from within a synchronized block
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return (size() > cacheSize);
    }

    @Override
    synchronized public void clear() {
        super.clear();
    }
}
