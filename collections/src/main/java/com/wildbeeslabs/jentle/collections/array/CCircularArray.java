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
package com.wildbeeslabs.jentle.collections.array;

import com.wildbeeslabs.jentle.collections.exception.InvalidDimensionException;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static com.wildbeeslabs.jentle.collections.utils.CUtils.newArray;

/**
 * Custom circular array implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CCircularArray<T> extends ACArray<T> {

    @Setter(AccessLevel.NONE)
    private int head;

    public CCircularArray(final Class<? extends T[]> clazz) throws InvalidDimensionException {
        this(clazz, 0);
    }

    public CCircularArray(final Class<? extends T[]> clazz, int size) throws InvalidDimensionException {
        this(clazz, size, null);
    }

    public CCircularArray(final Class<? extends T[]> clazz, int size, final T[] array) throws InvalidDimensionException {
        if (size < 0) {
            throw new InvalidDimensionException(String.format("ERROR: %s (invalid initial size=%d)", this.getClass().getName(), size));
        }
        this.size = size;
        this.array = newArray(clazz, this.size);
        if (Objects.nonNull(array)) {
            this.array = Arrays.copyOfRange(array, 0, Math.min(this.size, array.length));
        }
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        return this.array[this.convert(index)];
    }

    @Override
    public T set(final T item, int index) throws IndexOutOfBoundsException {
        index = this.convert(index);
        final T removed = this.array[index];
        this.array[index] = item;
        return removed;
    }

    @Override
    public void fill(final T value, int startIndex, int endIndex) {
        startIndex = this.convert(startIndex);
        endIndex = this.convert(endIndex);
        assert (startIndex <= endIndex);
        Arrays.fill(this.array, startIndex, endIndex, ObjectUtils.clone(value));
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size(); i++) {
            this.array[i] = null;
        }
        this.head = 0;
    }

    public void rotate(int shift) {
        this.head = this.convert(shift);
    }

    private int convert(int index) {
        if (index < 0) {
            index += this.size();
        }
        return (this.head + index) % this.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new CCircularArrayIterator<>(this);
    }

    protected static class CCircularArrayIterator<T> implements Iterator<T> {

        private final CCircularArray<? extends T> source;
        private int cursor;

        public CCircularArrayIterator(final CCircularArray<? extends T> source) {
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
            final T value = this.source.array[this.source.convert(this.cursor)];
            this.cursor++;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .appendSuper(super.toString())
            .append("head", this.head)
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CCircularArray)) {
            return false;
        }
        final CCircularArray<T> other = (CCircularArray<T>) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(this.head, other.head)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 79)
            .appendSuper(super.hashCode())
            .append(this.head)
            .toHashCode();
    }
}
