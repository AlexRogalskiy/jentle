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
package com.wildbeeslabs.jentle.collections.graph.impl;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.graph.iface.IGraph;
import com.wildbeeslabs.jentle.collections.graph.node.ACBaseGraphNode;
import com.wildbeeslabs.jentle.collections.list.iface.IList;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * Custom list graph implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CLGraph<T> extends ACGraph<T, CLGraph.CLGraphArc<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    protected static class CLGraphArc<T> extends ACBaseGraphNode<T, CLGraphArc<T>> {

        private int end;

        public CLGraphArc(int end) {
            this(null, end);
        }

        public CLGraphArc(final T data, int end) {
            super(data);
            this.end = end;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    protected static class CLGraphArcNode<T, E extends CLGraphArc<T>> extends ACListNode<E, CLGraph.CLGraphArcNode<T, E>> {

        public CLGraphArcNode() {
            this(null);
        }

        public CLGraphArcNode(final E data) {
            this(data, null);
        }

        public CLGraphArcNode(final E data, CLGraph.CLGraphArcNode<T, E> next) {
            super(data, next);
        }
    }

    protected IList<CLGraph.CLGraphArc<T>, CLGraph.CLGraphArcNode<T, CLGraph.CLGraphArc<T>>>[] graph;
    protected final Comparator<? super T> cmp;

    public CLGraph(int size) {
        this(size, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CLGraph(int size, final Comparator<? super T> cmp) {
        this.graph = this.newArray((Class<? extends IList<CLGraph.CLGraphArc<T>, CLGraph.CLGraphArcNode<T, CLGraph.CLGraphArc<T>>>[]>) this.graph.getClass(), size);
        this.cmp = cmp;
    }

    //@Override
    public void add(int from, int to, final T data) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].addLast(new CLGraphArc<>(data, to));
    }

    public void add(int from, int to) {
        this.add(from, to, null);
    }

    @SuppressWarnings("element-type-mismatch")
    public void remove(int from, int to) throws EmptyListException {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].remove(new CLGraphArc<>(to));
    }

    private CLGraph.CLGraphArc<T> getItem(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1].get(to);
    }

    public T get(int from, int to) {
        final CLGraph.CLGraphArc<T> temp = this.getItem(from, to);
        if (Objects.nonNull(temp)) {
            return temp.getData();
        }
        return null;
    }

    public boolean set(int from, int to, final T data) {
        final CLGraph.CLGraphArc<T> temp = this.getItem(from, to);
        if (Objects.nonNull(temp)) {
            temp.setData(data);
            return true;
        }
        return false;
    }

    public int cardOut(int from) {
        this.checkRange(from);
        return this.graph[from - 1].size();
    }

    private IList<CLGraph.CLGraphArc<T>, CLGraph.CLGraphArcNode<T, CLGraph.CLGraphArc<T>>>[] newArray(Class<? extends IList<CLGraph.CLGraphArc<T>, CLGraph.CLGraphArcNode<T, CLGraph.CLGraphArc<T>>>[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    @Override
    public int size() {
        return this.graph.length;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    public void clear() {
        this.graph = this.newArray((Class<? extends IList<CLGraph.CLGraphArc<T>, CLGraph.CLGraphArcNode<T, CLGraph.CLGraphArc<T>>>[]>) this.graph.getClass(), this.size());
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (vertex=%d is out of bounds [0, %d])", this.getClass().getName(), index, this.size()));
        }
    }

    public IGraph<Integer> toCSGraph() {
        final CSGraph sGraph = new CSGraph(this.size());
        for (int i = 0; i < this.size(); i++) {
            for (final CLGraph.CLGraphArc<T> node : this.graph[i]) {
                sGraph.add(i, node.end);
            }
        }
        return sGraph;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .appendSuper(super.toString())
            .append("class", this.getClass().getName())
            .append("data", Arrays.deepToString(this.graph))
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CLGraph)) {
            return false;
        }
        final CLGraph<T> other = (CLGraph<T>) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(Arrays.deepEquals(this.graph, other.graph), true)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 61)
            .appendSuper(super.hashCode())
            .append(Arrays.deepHashCode(this.graph))
            .toHashCode();
    }
}
