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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    public int capacity() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("className", this.getClass().getName())
                .append("size", this.size)
                .append("capacity", this.capacity)
                .append("data", Arrays.deepToString(this.array))
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CHeap)) {
            return false;
        }
        final CHeap<T> other = (CHeap<T>) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.size, other.size)
                .append(this.capacity, other.capacity)
                .appendSuper(Arrays.deepEquals(this.array, other.array))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 51)
                .appendSuper(super.hashCode())
                .append(this.size)
                .append(this.capacity)
                .append(Arrays.deepHashCode(this.array))
                .toHashCode();
    }
}
