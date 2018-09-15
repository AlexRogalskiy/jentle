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

import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * Custom hash map list cache implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CCache<K, V> extends ACBaseMap<K, V> {

    /**
     * Default cache size
     */
    public static final Integer DEFAULT_MAX_CACHE_SIZE = 10;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CCacheNode<K, V> extends ACListNodeExtended<CCacheNode.CNode<K, V>, CCacheNode<K, V>> {

        @Data
        @EqualsAndHashCode
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public static class CNode<K, V> {

            private K key;
            private V value;
        }

        public CCacheNode() {
            this(null, null);
        }

        public CCacheNode(final K key, final V value) {
            this(key, value, null, null);
        }

        public CCacheNode(final K key, final V value, final CCacheNode<K, V> next, final CCacheNode<K, V> previous) {
            super(new CCacheNode.CNode<>(key, value), next, previous);
        }
    }

    protected int maxCacheSize;
    protected CCache.CCacheNode<K, V> head;
    protected CCache.CCacheNode<K, V> tail;
    protected Map<K, CCache.CCacheNode<K, V>> map;

    public CCache() {
        this(CCache.DEFAULT_MAX_CACHE_SIZE);
    }

    public CCache(int maxCacheSize) {
        assert (maxCacheSize > 0);
        this.maxCacheSize = maxCacheSize;
        this.head = this.tail = null;
        this.map = new HashMap<>(this.maxCacheSize);
    }

    public V getValue(final K key) {
        final CCache.CCacheNode<K, V> item = this.map.get(key);
        if (Objects.isNull(item)) {
            return null;
        }
        if (item != this.head) {
            this.removeFromList(item);
            this.insertAtFront(item);
        }
        return item.getData().getValue();
    }

    private void removeFromList(final CCache.CCacheNode<K, V> node) {
        if (Objects.isNull(node)) {
            return;
        }
        if (Objects.nonNull(node.getPrevious())) {
            node.getPrevious().setNext(node.getNext());
        }
        if (Objects.nonNull(node.getNext())) {
            node.getNext().setPrevious(node.getPrevious());
        }
        if (Objects.isNull(this.tail)) {
            this.tail = node.getPrevious();
        }
        if (Objects.isNull(this.head)) {
            this.head = node.getNext();
        }
    }

    private void insertAtFront(final CCache.CCacheNode<K, V> node) {
        if (Objects.isNull(this.head)) {
            this.head = this.tail = node;
        } else {
            this.head.setPrevious(node);
            node.setNext(this.head);
            this.head = node;
        }
    }

    public boolean removeKey(final K key) {
        final CCache.CCacheNode<K, V> node = this.map.get(key);
        this.removeFromList(node);
        this.map.remove(key);
        return true;
    }

    public void setKeyValue(final K key, final V value) {
        this.removeKey(key);
        if (this.map.size() >= this.maxCacheSize && Objects.nonNull(this.tail)) {
            this.removeKey(this.tail.getData().getKey());
        }
        final CCache.CCacheNode<K, V> node = new CCache.CCacheNode<>(key, value);
        this.insertAtFront(node);
        this.map.put(key, node);
    }

    protected CCache.CCacheNode<K, V> remove(CCache.CCacheNode<K, V> root) {
        CCache.CCacheNode<K, V> backup = null;
        while (Objects.nonNull(root) && root != this.tail) {
            backup = root.getNext();
            root.setNext(null);
            root = backup;
        }
        return root;
    }

    protected int length(final CCache.CCacheNode<K, V> node) {
        int size = 0;
        CCache.CCacheNode<K, V> current = node;
        while (Objects.nonNull(current)) {
            size++;
            current = current.getNext();
        }
        return size;
    }

    @Override
    public void clear() {
        this.remove(this.head);
        this.head = this.tail = null;
        this.map.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.head == null;//return (0 == this.size());
    }

    @Override
    public int size() {
        return this.length(this.head);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
