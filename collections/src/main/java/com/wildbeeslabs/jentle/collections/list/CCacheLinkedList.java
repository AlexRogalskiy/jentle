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
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom linked list cache implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CCacheLinkedList<T> extends ACList<T, CCacheLinkedList.CCacheNode<T>> {

    /**
     * Default max cache size
     */
    public static final Integer DEFAULT_MAX_CACHE_SIZE = 10;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CCacheNode<T> extends ACListNodeExtended<T, CCacheNode<T>> {

        private String query;

        public CCacheNode() {
            this(null);
        }

        public CCacheNode(final T data) {
            this(data, null);
        }

        public CCacheNode(final T data, final String query) {
            this(data, null, null);
            this.query = query;
        }

        public CCacheNode(final T data, final CCacheNode<T> next, final CCacheNode<T> previous) {
            super(data, next, previous);
        }
    }

    protected Map<String, CCacheLinkedList.CCacheNode<T>> map;

    public CCacheLinkedList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCacheLinkedList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CCacheLinkedList(final CCacheLinkedList<T> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCacheLinkedList(final CCacheLinkedList<T> source, final Comparator<? super T> cmp) {
        super(source, cmp);
        this.map = new HashMap<>();
    }

    protected void moveToFront(final CCacheLinkedList.CCacheNode<T> node) {
        if (node == this.first) {
            return;
        }
        this.removeFromLinkedList(node);
        node.setNext(this.first);
        if (Objects.nonNull(this.first)) {
            this.first.setPrevious(node);
        }
        this.first = node;
        this.size++;
        if (Objects.isNull(this.last)) {
            this.last = node;
        }
    }

    protected void movetoFront(final String query) {
        final CCacheLinkedList.CCacheNode<T> node = this.map.get(query);
        this.moveToFront(node);
    }

    protected void removeFromLinkedList(final CCacheLinkedList.CCacheNode<T> node) {
        if (Objects.isNull(node)) {
            return;
        }
        if (Objects.nonNull(node.getNext()) || Objects.nonNull(node.getPrevious())) {
            this.size--;
        }
        final CCacheLinkedList.CCacheNode<T> prev = node.getPrevious();
        if (Objects.nonNull(prev)) {
            prev.setNext(node.getNext());
        }
        final CCacheLinkedList.CCacheNode<T> next = node.getNext();
        if (Objects.nonNull(node.getNext())) {
            next.setPrevious(prev);
        }
        if (node == this.first) {
            this.first = next;
        }
        if (node == this.last) {
            this.last = prev;
        }
        node.setNext(null);
        node.setPrevious(null);
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
            final CCacheLinkedList.CCacheNode<T> node = this.map.get(query);
            node.setData(data);
            this.moveToFront(node);
            return;
        }
        final CCacheLinkedList.CCacheNode<T> node = new CCacheLinkedList.CCacheNode<>(data, query);
        this.moveToFront(node);
        this.map.put(query, node);

        if (this.size > CCacheLinkedList.DEFAULT_MAX_CACHE_SIZE) {
            this.map.remove(this.last.getQuery());
            this.removeFromLinkedList(this.last);
        }
    }

    @Override
    public void insertAt(final T item, int index) {
        this.insertToAt(Optional.of(item), index);
        final CCacheLinkedList.CCacheNode<T> temp1 = this.getToAt(index - 1);
        final CCacheLinkedList.CCacheNode<T> temp2 = this.getToAt(index);
        final CCacheLinkedList.CCacheNode<T> node = this.insertToAt(Optional.of(item), index);
        temp2.setPrevious(node);
        node.setPrevious(temp1);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected CCacheLinkedList.CCacheNode<T> createNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CCacheLinkedList.CCacheNode<>(value.get());
        }
        return new CCacheLinkedList.CCacheNode<>();
    }
}
