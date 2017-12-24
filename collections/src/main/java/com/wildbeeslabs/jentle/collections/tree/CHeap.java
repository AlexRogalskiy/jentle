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
package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.collections.exception.NoSpaceAvailableException;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom heap implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CHeap<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    protected int size;
    protected int capacity;
    protected final Comparator<? super T> cmp;
    protected T[] array;

    public CHeap(final Class<? extends T[]> clazz, int capacity) {
        this(clazz, capacity, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CHeap(final Class<? extends T[]> clazz, int capacity, final Comparator<? super T> cmp) {
        this.size = 0;
        this.capacity = capacity;
        this.cmp = cmp;
        this.array = CUtils.newArray(clazz, this.capacity);
    }

    public void insert(final T item) throws NoSpaceAvailableException {
        if (this.size >= this.capacity) {
            throw new NoSpaceAvailableException(String.format("ERROR: CHeap (no more space available size=%d, capacity=%d)", this.size(), this.capacity()));
        }
        int currentIndex = this.size++, parentIndex;
        while (currentIndex > 0 && Objects.compare(item, (this.array[parentIndex = currentIndex / 2]), this.cmp) < 0) {
            this.array[currentIndex] = this.array[parentIndex];
            currentIndex = parentIndex;
        }
        this.array[currentIndex] = item;
    }

    //@Override
    public void clear() {
        for (int i = 0; i < this.capacity(); i++) {
            this.array = null;
        }
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    //@Override
    public int capacity() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public String toString() {
        return String.format("CHeap {data: %s, size: %d, capacity: %d}", this.array, this.size, this.capacity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CHeap<T> other = (CHeap<T>) obj;
        if (this.size != other.size) {
            return false;
        }
        if (this.capacity != other.capacity) {
            return false;
        }
        if (!Arrays.deepEquals(this.array, other.array)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.size;
        hash = 79 * hash + this.capacity;
        hash = 79 * hash + Arrays.deepHashCode(this.array);
        return hash;
    }
}
