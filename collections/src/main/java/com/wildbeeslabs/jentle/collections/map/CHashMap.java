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
import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom hash map implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
public class CHashMap<K, V> implements IMap<K, V> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    /**
     * Default map capacity
     */
    protected static final Integer DEFAULT_MAP_CAPACITY = 4;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class CLinkedListNode<K, V> extends ACListNodeExtended<V, CLinkedListNode<K, V>> {

        protected K key;

        public CLinkedListNode() {
            this(null, null);
        }

        public CLinkedListNode(final K key, final V value) {
            this(key, value, null, null);
        }

        public CLinkedListNode(final K key, final V value, final CLinkedListNode<K, V> next, final CLinkedListNode<K, V> previous) {
            super(value, next, previous);
            this.key = key;
        }
    }

    protected final List<CLinkedListNode<K, V>> array;

    public CHashMap() {
        this(CHashMap.DEFAULT_MAP_CAPACITY);
    }

    public CHashMap(int capacity) {
        this.array = new ArrayList<>(capacity);
//        this.array.ensureCapacity(capacity);
//        for (int i = 0; i < capacity; i++) {
//            this.array.add(null);
//        }
    }

    public void put(final K key, final V value) {
        CLinkedListNode<K, V> node = this.getNodeForKey(key);
        if (Objects.nonNull(key)) {
            node.setData(value);
            return;
        }
        node = new CLinkedListNode<>(key, value);
        int index = this.getIndexForKey(key);
        if (Objects.nonNull(this.array.get(index))) {
            node.setNext(this.array.get(index));
            node.getNext().setPrevious(node);
        }
        this.array.set(index, node);
    }

    public void remove(final K key) {
        final CLinkedListNode<K, V> node = this.getNodeForKey(key);
        if (Objects.nonNull(node.getPrevious())) {
            node.getPrevious().setNext(node.getNext());
        } else {
            int hashKey = getIndexForKey(key);
            this.array.set(hashKey, node.getNext());
        }
        if (Objects.nonNull(node.getNext())) {
            node.getNext().setPrevious(node.getPrevious());
        }
    }

    public V get(final K key) {
        final CLinkedListNode<K, V> node = this.getNodeForKey(key);
        return (Objects.isNull(node) ? null : node.getData());
    }

    protected CLinkedListNode<K, V> getNodeForKey(final K key) {
        int index = getIndexForKey(key);
        CLinkedListNode<K, V> current = this.array.get(index);
        while (Objects.nonNull(current)) {
            if (key == current.getKey()) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    public int getIndexForKey(final K key) {
        return Math.abs(key.hashCode() % this.array.size());
    }
}