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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import static com.wildbeeslabs.jentle.collections.utils.CUtils.newArray;

/**
 * Custom dual stack implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CDualStack<T> extends ACStack<T> {

    protected T[] array;
    protected int size;
    protected int index1;
    protected int index2;

    public CDualStack(int maxSize, final Class<? extends T[]> clazz) {
        assert (size > 0);
        this.index1 = -1;
        this.index2 = maxSize;
        this.size = maxSize;
        this.array = newArray(clazz, maxSize);
    }

    public void push1(final T value) throws OverflowStackException {
        if (this.isFull()) {
            throw new OverflowStackException(String.format("ERROR: %s (full size=%d)", this.getClass().getName(), this.size1()));
        }
        this.array[++this.index1] = value;
    }

    public void push2(final T value) throws OverflowStackException {
        if (this.isFull()) {
            throw new OverflowStackException(String.format("ERROR: %s (full size=%d)", this.getClass().getName(), this.size2()));
        }
        this.array[--this.index2] = value;
    }

    public T pop1() throws EmptyStackException {
        if (this.isEmpty1()) {
            throw new EmptyStackException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size1()));
        }
        final T removed = this.array[this.index1];
        this.array[this.index1] = null;
        this.index1--;
        return removed;
    }

    public T pop2() throws EmptyStackException {
        if (this.isEmpty2()) {
            throw new EmptyStackException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size2()));
        }
        final T removed = this.array[this.index2];
        this.array[this.index2] = null;
        this.index2--;
        return removed;
    }

    public boolean isEmpty1() {
        if (this.index1 == -1) {
            return true;
        }
        return false;
    }

    public boolean isEmpty2() {
        if (this.index2 == this.size) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (this.index1 == this.index2) {
            return true;
        }
        return false;
    }

    public int size() {
        return this.size1() + this.size2();
    }

    public int size1() {
        return this.index1 + 1;
    }

    public int size2() {
        return this.size - this.index2;
    }

    @Override
    public void push(final T value) throws OverflowStackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T pop() throws EmptyStackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T peek() throws EmptyStackException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.array[i] = null;
        }
        this.index1 = -1;
        this.index2 = this.size;
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Queue<? extends T> toLifoQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
