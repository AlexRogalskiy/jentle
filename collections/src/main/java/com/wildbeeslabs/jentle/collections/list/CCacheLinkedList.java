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
package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom cache linked list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CCacheLinkedList<T> extends CLinkedList<T, CCacheLinkedList.CCacheNode<T>> {

    /**
     * Default max cache size
     */
    public static final Integer DEFAULT_MAX_SIZE = 10;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class CCacheNode<T> extends ACListNodeExtended<T, CCacheNode<T>> {

        private String query;

        public CCacheNode() {
            this(null, null);
        }

        public CCacheNode(final String query, final T data) {
            this(query, data, null, null);
        }

        public CCacheNode(final String query, final T data, final CCacheNode<T> next, final CCacheNode<T> previous) {
            super(data, next, previous);
            this.query = query;
        }
    }

    private Map<String, CCacheLinkedList.CCacheNode<T>> map;

    public CCacheLinkedList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCacheLinkedList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CCacheLinkedList(final CCacheNode<T> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCacheLinkedList(final CCacheNode<T> source, final Comparator<? super T> cmp) {
        super(source, cmp);
        this.map = new HashMap<>();
    }

    public void moveToFront(final CCacheLinkedList.CCacheNode<T> node) {

    }

    public void movetoFront(final String query) {

    }

    public void removeFromLinkedList(final CCacheLinkedList.CCacheNode<T> node) {

    }

    public T getData(final String query) {
        if (!this.map.containsKey(query)) {
            return null;
        }
        final CCacheLinkedList.CCacheNode<T> node = this.map.get(query);
        moveToFront(node);
        return node.getData();
    }

    public void insertData(final String query, final T data) {
        if (this.map.containsKey(query)) {
            final CCacheLinkedList.CCacheNode<T> node = map.get(query);
            node.setData(data);
            moveToFront(node);
            return;
        }
        final CCacheLinkedList.CCacheNode<T> node = new CCacheLinkedList.CCacheNode<>(query, data);
        moveToFront(node);
        this.map.put(query, node);

        if (this.size > DEFAULT_MAX_SIZE) {
            this.map.remove(this.last.getQuery());
            removeFromLinkedList(this.last);
        }
    }
}
