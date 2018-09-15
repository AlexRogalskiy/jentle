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
package com.wildbeeslabs.jentle.collections.collection;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom double collection implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CDualCollection<T> extends AbstractCollection<T> implements Serializable {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected final Collection<T> first;
    protected final Collection<T> second;

    public CDualCollection(final Collection first, final Collection second) {
        this.first = Objects.nonNull(first) ? first : Collections.EMPTY_LIST;
        this.second = Objects.nonNull(second) ? second : Collections.EMPTY_LIST;
    }

    @Override
    public Iterator<T> iterator() {
        return new CDualCollectionIterator<>(this);
    }

    @Override
    public int size() {
        return first.size() + second.size();
    }

    public static Collection combine(Collection first, Collection second) {
        if (first == second) {
            return first;
        }
        if (Objects.nonNull(first)) {
            if (Objects.nonNull(second)) {
                return new CDualCollection(first, second);
            } else {
                return first;
            }
        }
        return second;
    }

    protected static class CDualCollectionIterator<T> implements Iterator<T> {

        private final CDualCollection<? extends T> source;
        private Iterator iterator;
        private boolean bSecond;

        public CDualCollectionIterator(final CDualCollection<? extends T> source) {
            this.source = source;
            this.iterator = source.first.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext() || (!this.bSecond && !this.source.second.isEmpty());
        }

        @Override
        public T next() {
            if (!this.bSecond && !this.iterator.hasNext()) {
                this.iterator = this.source.second.iterator();
                this.bSecond = true;
            }
            return (T) this.iterator.next();
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }
}
