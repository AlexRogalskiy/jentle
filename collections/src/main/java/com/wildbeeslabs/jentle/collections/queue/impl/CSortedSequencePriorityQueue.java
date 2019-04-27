/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNodeExtended;
import com.wildbeeslabs.jentle.collections.list.node.CPositionalListNodeExtended;
import com.wildbeeslabs.jentle.collections.model.impl.CKeyValueNode;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom sorted sequence {@link IPriorityQueue} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CSortedSequencePriorityQueue<K, V, S extends CKeyValueNode<K, V>, T extends ACPositionalListNodeExtended<K, V, S>> implements IPriorityQueue<T> {

    private CPositionalListNodeExtended<K, V> root = new CPositionalListNodeExtended<>();

    private final Comparator<? super K> comparator;

    protected T element(final Position<T> position) {
        return position.element();
    }

    protected K key(final Position<T> position) {
        return Optional.ofNullable(element(position))
            .map(elem -> element().getData())
            .filter(Objects::nonNull)
            .map(elem -> elem.getKey())
            .orElseGet(null);
    }

    protected V value(final Position<T> position) {
        return Optional.ofNullable(element(position))
            .map(elem -> element().getData())
            .filter(Objects::nonNull)
            .map(elem -> elem.getValue())
            .orElseGet(null);
    }

    public CSortedSequencePriorityQueue(final Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void insertItem(final T value) {
    }

    @Override
    public T minElement() {
        return null;
    }

    @Override
    public T removeMin() {
        return null;
    }

    @Override
    public boolean contains(final Object o) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(final T1[] a) {
        return null;
    }

    @Override
    public boolean add(final T kvcListNodeExtended) {
        return false;
    }

    @Override
    public boolean remove(final Object o) {
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean offer(final T node) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
