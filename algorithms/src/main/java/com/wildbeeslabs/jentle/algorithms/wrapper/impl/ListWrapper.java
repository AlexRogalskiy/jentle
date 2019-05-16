/*
 * The MIT License
 *
 * Copyright 2018 Pivotal Software, Inc..
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
package com.wildbeeslabs.jentle.algorithms.wrapper.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * List wrapper implementation
 *
 * @param <V> type of list item
 * @param <T> type of list
 */
@Data
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ListWrapper<V, T extends List<V> & Serializable> implements Serializable {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -5001564153325429130L;

    /**
     * Wrapped {@link List} instance
     */
    private final T list;

    public static <V, T extends List<V> & Serializable> Map<String, Serializable> putMap(final Map<String, Serializable> intent, final String name, final T list) {
        intent.put(name, new ListWrapper<>(list));
        return intent;
    }

    public static <V, T extends List<V> & Serializable> T getMap(final Map<String, Serializable> intent, final String name) {
        final Serializable elem = intent.get(name);
        return Objects.isNull(elem) ? (T) Collections.emptyList() : ((ListWrapper<V, T>) elem).getList();
    }
}
