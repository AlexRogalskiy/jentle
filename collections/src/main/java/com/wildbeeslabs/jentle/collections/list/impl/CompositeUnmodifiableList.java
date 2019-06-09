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
package com.wildbeeslabs.jentle.collections.list.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.AbstractList;
import java.util.List;

/**
 * Custom list implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompositeUnmodifiableList<E> extends AbstractList<E> {

    private final List<E> list1;
    private final List<E> list2;

    public CompositeUnmodifiableList(final List<E> list1, final List<E> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }

    @Override
    public E get(int index) {
        assert index >= 0 && index < this.size() : "Index is out of bounds";
        if (index < this.list1.size()) {
            return this.list1.get(index);
        }
        return this.list2.get(index - this.list1.size());
    }

    @Override
    public int size() {
        return this.list1.size() + this.list2.size();
    }

//    @Override
//    public E get(final int index) {
//        return getList(index).get(getListIndex(index));
//    }

    @Override
    public E set(final int index, final E element) {
        return getList(index).set(getListIndex(index), element);
    }

    @Override
    public void add(final int index, final E element) {
        getList(index).add(getListIndex(index), element);
    }

    @Override
    public E remove(final int index) {
        return getList(index).remove(getListIndex(index));
    }

    @Override
    public boolean contains(final Object o) {
        return list1.contains(o) || list2.contains(o);
    }

    @Override
    public void clear() {
        list1.clear();
        list2.clear();
    }

    /**
     * Returns the index within the corresponding list related to the given index.
     *
     * @param index the index in this list
     * @return the index of the underlying list
     */
    private int getListIndex(final int index) {
        final int size1 = list1.size();
        return index >= size1 ? index - size1 : index;
    }

    /**
     * Returns the list that corresponds to the given index.
     *
     * @param index the index in this list
     * @return the underlying list that corresponds to that index
     */
    private List<E> getList(final int index) {
        return index >= list1.size() ? list2 : list1;
    }
}
