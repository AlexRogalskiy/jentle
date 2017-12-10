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

import com.wildbeeslabs.jentle.algorithms.utils.CArrayUtils;
import com.wildbeeslabs.jentle.collections.exception.InvalidDimensionException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * Custom circular array implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CCircularArray<T extends Serializable> extends ACArray<T> {

    private int head;

    public CCircularArray(final Class<? extends T[]> clazz) throws InvalidDimensionException {
        this(clazz, 0);
    }

    public CCircularArray(final Class<? extends T[]> clazz, int size) throws InvalidDimensionException {
        this(clazz, size, null);
    }

    public CCircularArray(final Class<? extends T[]> clazz, int size, final T[] array) throws InvalidDimensionException {
        if (size < 0) {
            throw new InvalidDimensionException(String.format("ERROR: CCircularArray (invalid initial size=%d)", size));
        }
        this.size = size;
        this.array = CArrayUtils.newArray(clazz, this.size);
        if (Objects.nonNull(array)) {
            System.arraycopy(array, 0, this.array, 0, Math.min(this.size, array.length));
        }
    }

    public T elementAt(int index) throws IndexOutOfBoundsException {
        return this.array[this.convert(index)];
    }

    public T setElementAt(final T item, int index) throws IndexOutOfBoundsException {
        index = this.convert(index);
        final T removed = this.array[index];
        this.array[index] = item;
        return removed;
    }

    public void fill(final T value, int startIndex, int endIndex) {
        startIndex = this.convert(startIndex);
        endIndex = this.convert(endIndex);
        assert (startIndex <= endIndex);
        Arrays.fill(this.array, startIndex, endIndex, SerializationUtils.clone(value));
    }

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
        return String.format("CCircularArray {data: %s, head: %d}", this.array, this.head);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CCircularArray<T> other = (CCircularArray<T>) obj;
        if (this.head != other.head) {
            return false;
        }
        if (this.size() != other.size()) {
            return false;
        }
        if (!Arrays.deepEquals(this.array, other.array)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.head;
        hash = 79 * hash + Arrays.deepHashCode(this.array);
        return hash;
    }
}
