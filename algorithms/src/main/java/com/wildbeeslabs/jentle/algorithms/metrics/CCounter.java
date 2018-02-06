/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.metrics;

import java.io.Serializable;
import java.util.Iterator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom counter watch implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CCounter implements Iterator, Serializable {

    private boolean wrap = Boolean.FALSE;
    private long first = 1;
    private long last = -1;
    private long interval = 1;
    private long current = first;

    public CCounter(long first, long last) {
        this(first, last, 0);
    }

    public CCounter(long first, long last, long interval) {
        this.setFirst(first);
        this.last = last;
        this.interval = interval;
    }

    public void incrementBy(long addition) {
        this.current += addition;
    }

    public void decrementBy(long addition) {
        this.current -= addition;
    }

    public void setFirst(long first) {
        this.first = first;
        this.current = first;
    }

    public long getNext() {
        long next = this.current;
        this.current += this.interval;
        if (this.wrap && (this.current > this.last)) {
            this.current -= ((1 + this.last) - this.first);
        }
        return next;
    }

    public long getPrevious() {
        this.current -= this.interval;
        if (this.wrap && (this.current < this.first)) {
            this.current += (this.last - this.first + 1);
        }
        return this.current;
    }

    @Override
    public boolean hasNext() {
        return ((this.last == -1) || this.wrap) ? Boolean.TRUE : (this.current <= this.last);
    }

    @Override
    public Object next() {
        return new Long(this.getNext());
    }

    @Override
    public void remove() {
        // Do nothing
    }
}
