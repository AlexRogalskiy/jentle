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
package com.wildbeeslabs.jentle.collections.graph.impl;

import com.wildbeeslabs.jentle.collections.graph.node.ACGraphNodeExtended;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom set graph 2 implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CSGraph2<T> extends ACGraph<T, CSGraph2.CGraphNodeExtended<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CGraphNodeExtended<T> extends ACGraphNodeExtended<T, CGraphNodeExtended<T>> {

        public CGraphNodeExtended() {
            this(null);
        }

        public CGraphNodeExtended(final T data) {
            this(data, CUtils.DEFAULT_SORT_COMPARATOR);
        }

        public CGraphNodeExtended(final T data, final Comparator<? super T> cmp) {
            super(data, cmp);
        }
    }

    protected final Set<CSGraph2.CGraphNodeExtended<T>> nodes = new HashSet<>();

    public Collection<CSGraph2.CGraphNodeExtended<T>> getNodes() {
        return this.nodes;
    }

    public void setAdjacents(final Collection<CSGraph2.CGraphNodeExtended<T>> nodes) {
        this.nodes.clear();
        if (Objects.nonNull(nodes)) {
            this.nodes.addAll(nodes);
        }
    }

    public void addNode(final CSGraph2.CGraphNodeExtended<T> node) {
        if (Objects.nonNull(node)) {
            this.nodes.add(node);
        }
    }

    public void removeNode(final CSGraph2.CGraphNodeExtended<T> node) {
        if (Objects.nonNull(node)) {
            this.nodes.remove(node);
        }
    }

    @Override
    public int size() {
        return this.nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public Iterator<T> iterator() {
        return new CSGraph2.CSGraph2Iterator<>(this);
    }

    protected static class CSGraph2Iterator<T> implements Iterator<T> {

        private Iterator<? extends T> cursor = null;

        public CSGraph2Iterator(final CSGraph2<? extends T> source) {
            this.cursor = source.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.cursor.hasNext();
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            return this.cursor.next();
        }

        @Override
        public void remove() {
            this.cursor.remove();
        }
    }
}
