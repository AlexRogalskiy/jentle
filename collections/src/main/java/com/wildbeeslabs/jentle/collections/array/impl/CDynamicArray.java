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
package com.wildbeeslabs.jentle.collections.array.impl;

import com.wildbeeslabs.jentle.collections.exception.InvalidDimensionException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static com.wildbeeslabs.jentle.collections.utils.CUtils.newArray;

/**
 * Custom dynamic array implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CDynamicArray<T> extends ACArray<T> {

    /**
     * Default array enlarge capacity coefficient
     */
    private static final double DEFAULT_ENLARGE_CAPACITY_FACTOR = 1.45;
    /**
     * Default array shrink capacity coefficient
     */
    private static final double DEFAULT_SHRINK_CAPACITY_FACTOR = 1.75;

    private int capacity;

    public CDynamicArray(final Class<? extends T[]> clazz) throws InvalidDimensionException {
        this(clazz, 0);
    }

    public CDynamicArray(final Class<? extends T[]> clazz, int size) throws InvalidDimensionException {
        this(clazz, size, size);
    }

    public CDynamicArray(final Class<? extends T[]> clazz, int size, int capacity) throws InvalidDimensionException {
        this(clazz, size, capacity, null);
    }

    public CDynamicArray(final Class<? extends T[]> clazz, int size, int capacity, final T[] array) throws InvalidDimensionException {
        if (size < 0) {
            throw new InvalidDimensionException(String.format("ERROR: %s (invalid initial size=%d)", this.getClass().getName(), size));
        }
        this.size = size;
        this.capacity = (capacity < size ? size : capacity);
        this.array = newArray(clazz, this.capacity);
        if (Objects.nonNull(array)) {
            this.array = Arrays.copyOfRange(array, 0, Math.min(this.size, array.length));
        }
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        return this.array[index];
    }

    @Override
    public T set(final T item, int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        final T removed = this.array[index];
        this.array[index] = item;
        return removed;
    }

    public void add(final T[] items) {
        if (Objects.nonNull(items)) {
            this.resize(items.length);
            for (final T item : items) {
                this.add(item);
            }
        }
    }

    @Override
    public boolean add(final T item) {
        return this.addAt(item, this.size);
    }

    public boolean addAt(final T item, int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        this.resize(1);
        System.arraycopy(this.array, index, this.array, index + 1, this.size - index);
        this.array[index] = item;
        this.size++;
        return true;
    }

    @Override
    public boolean remove(final Object item) {
        int index = this.indexOf(item);
        if (index < 0) {
            return false;
        }
        this.removeAt(index);
        return true;
    }

    public T removeAt(int index) throws IndexOutOfBoundsException {
        this.checkRange(index);
        this.resize(-1);
        final T removed = this.array[index];
        int itemsToShift = this.size - index - 1;
        if (itemsToShift > 0) {
            System.arraycopy(this.array, index + 1, this.array, index, itemsToShift);
        }
        this.array[this.size] = null;
        return removed;
    }

    private void resize(int delta) {
        if (delta > 0) {
            this.enlargeCapacity(delta);
        } else if (delta < 0) {
            this.shrinkCapacity(-delta);
        }
    }

    private void enlargeCapacity(int delta) {
        this.size += delta;
        if (this.size > this.capacity) {
            this.capacity = (int) Math.floor(this.size * DEFAULT_ENLARGE_CAPACITY_FACTOR);
            log.info(String.format("CDynamicArray (enlarged capacity=%d)", this.capacity));
            this.changeCapacity();
        }
    }

    private void shrinkCapacity(int delta) {
        this.size = (delta > this.size ? 0 : this.size - delta);
        if ((int) Math.floor(this.size * DEFAULT_SHRINK_CAPACITY_FACTOR) < this.capacity) {
            this.capacity = (int) Math.floor(this.size * DEFAULT_SHRINK_CAPACITY_FACTOR);
            log.info(String.format("CDynamicArray (shrinked capacity=%d)", this.capacity));
            this.changeCapacity();
        }
    }

    private void changeCapacity() {
        //T[] temp = newArray((Class<? extends T[]>) this.array.getClass(), this.capacity);
        final T[] temp = Arrays.copyOfRange(this.array, 0, this.size);
        this.array = temp;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size(); i++) {
            this.array[i] = null;
        }
    }

    @Override
    public boolean contains(final Object item) {
        return (this.indexOf(item) >= 0);
    }

    public void trimToSize() {
        if (this.size < this.capacity) {
            this.array = Arrays.copyOf(this.array, this.size);
            this.capacity = this.size;
        }
    }

    public int capacity() {
        return this.capacity;
    }

    @Override
    public Iterator<T> iterator() {
        return new CDynamicArrayIterator<>(this);
    }

    protected static class CDynamicArrayIterator<T> implements Iterator<T> {

        private final CDynamicArray<? extends T> source;
        private int cursor;

        public CDynamicArrayIterator(final CDynamicArray<? extends T> source) {
            this.source = source;
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return (this.cursor < this.source.size());
        }

        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }
            final T value = this.source.array[this.cursor];
            this.cursor++;
            return value;
        }

        @Override
        public void remove() {
            this.source.checkRange(--this.cursor);
            this.source.removeAt(this.cursor);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .appendSuper(super.toString())
            .append("capacity", this.capacity)
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CDynamicArray)) {
            return false;
        }
        final CDynamicArray<T> other = (CDynamicArray<T>) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(this.capacity, other.capacity)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 51)
            .appendSuper(super.hashCode())
            .append(this.capacity)
            .toHashCode();
    }
}
