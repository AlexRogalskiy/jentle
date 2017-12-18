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

import com.wildbeeslabs.jentle.collections.interfaces.IArray;
import com.wildbeeslabs.jentle.collections.utils.CConverterUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.SerializationUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom abstract array implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class ACArray<T extends Serializable> implements IArray<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected T[] array;
    protected int size;

    public int indexOf(final T item) {
        if (Objects.isNull(item)) {
            for (int i = 0; i < this.size(); i++) {
                if (Objects.isNull(this.array[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < this.size(); i++) {
                if (Objects.equals(item, this.array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void fill(final IntFunction<? extends T> generator) {
        assert (Objects.nonNull(generator));
        Arrays.setAll(this.array, generator);
    }

    public void fill(final T value) {
        this.fill(value, 0, this.size() - 1);
    }

    public void fill(final T value, int startIndex, int endIndex) {
        this.checkRange(startIndex);
        this.checkRange(endIndex);
        assert (startIndex <= endIndex);
        Arrays.fill(this.array, startIndex, endIndex, SerializationUtils.clone(value));
    }

    public boolean contains(final T item) {
        return (this.indexOf(item) >= 0);
    }

    public T[] toArray() {
        return Arrays.copyOf(this.array, this.size());
        //SerializationUtils.clone(this.array);
    }

    public Set<? extends T> toSet() {
        return CConverterUtils.convertArrayToSet(this.array);
    }

    public List<? extends T> toList() {
        return CConverterUtils.convertArrayToList(this.array);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (index=%d is out of bounds [0, %d])", this.getClass().getName(), index, this.size - 1));
        }
    }
}
