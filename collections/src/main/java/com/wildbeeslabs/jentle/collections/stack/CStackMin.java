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
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Objects;

/**
 * Custom stack with minimum implementation {@link CStack}
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
public class CStackMin<T> extends CStack<T> {

    /**
     * Default {@link CStack} instance
     */
    protected CStack<T> minStack;
    /**
     * Default {@link Comparator} instance
     */
    protected final Comparator<? super T> cmp;

    /**
     * Default min stack constructor
     */
    public CStackMin() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    /**
     * Default min stack constructor by input {@link Comparator} instance
     *
     * @param cmp - initial input {@link Comparator} instance
     */
    public CStackMin(final Comparator<? super T> cmp) {
        this.cmp = cmp;
        this.minStack = new CStack<>();
    }

    @Override
    public void push(final T value) throws OverflowStackException {
        if (Objects.compare(value, this.peek(), this.cmp) <= 0) {
            this.minStack.push(value);
        }
        super.push(value);
    }

    @Override
    public T pop() throws EmptyStackException {
        final T value = super.pop();
        if (Objects.compare(value, this.peek(), this.cmp) == 0) {
            this.minStack.pop();
        }
        return value;
    }

    @Override
    public T peek() throws EmptyStackException {
        return this.minStack.peek();
    }
}
