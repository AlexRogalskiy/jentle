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
package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom stack with minimum implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CStackMin<T> extends CStack<T> {

    protected CStack<T> minStack;
    protected final Comparator<? super T> cmp;

    public CStackMin() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CStackMin(final Comparator<? super T> cmp) {
        this.cmp = cmp;
        this.minStack = new CStack<>();
    }

    @Override
    public void push(final T value) {
        if (Objects.compare(value, this.min(), this.cmp) <= 0) {
            this.minStack.push(value);
        }
        super.push(value);
    }

    @Override
    public T pop() throws EmptyStackException {
        final T value = super.pop();
        if (Objects.compare(value, this.min(), this.cmp) == 0) {
            this.minStack.pop();
        }
        return value;
    }

    protected T min() {
        try {
            return this.minStack.peek();
        } catch (EmptyStackException ex) {
            LOGGER.error(String.format("ERROR: minimum stack is empty: message={%s}", ex.getMessage()));
            return null;
        }
    }
}
