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
package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.graph.node.CGraphNode;
import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom list-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CLGraph<T> implements IGraph<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    protected static class CLGraphArc<T> extends ACNode<T> {

        private int end;

        public CLGraphArc(int end) {
            this(null, end);
        }

        public CLGraphArc(final T data, int end) {
            super(data);
            this.end = end;
        }
    }

    protected IList<CLGraphArc<T>>[] graph;
    protected final Comparator<? super T> cmp;

    public CLGraph(int size) {
        this(size, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CLGraph(int size, final Comparator<? super T> cmp) {
        this.graph = this.newArray((Class<? extends IList<CLGraphArc<T>>[]>) this.graph.getClass(), size);
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

    public void remove(int from, int to) throws EmptyListException {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].remove(new CLGraphArc<>(to));
    }

    private CLGraphArc<T> getItem(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1].get(to);
    }

    public T get(int from, int to) {
        CLGraphArc<T> temp = this.getItem(from, to);
        if (Objects.nonNull(temp)) {
            return temp.getData();
        }
        return null;
    }

    public boolean set(int from, int to, final T data) {
        CLGraphArc<T> temp = this.getItem(from, to);
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

    private IList<CLGraphArc<T>>[] newArray(Class<? extends IList<CLGraphArc<T>>[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    public int size() {
        return this.graph.length;
    }

    public void clear() {
        this.graph = this.newArray((Class<? extends IList<CLGraphArc<T>>[]>) this.graph.getClass(), this.size());
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (vertex=%d is out of bounds [0, %d])", this.getClass().getName(), index, this.size()));
        }
    }

    public IGraph<Integer> toCSGraph() {
        final CSGraph sGraph = new CSGraph(this.size());
        for (int i = 0; i < this.size(); i++) {
            for (Iterator<? extends CLGraphArc<T>> it = this.graph[i].iterator(); it.hasNext();) {
                CLGraphArc<T> node = it.next();
                sGraph.add(i, node.end, null);
            }
        }
        return sGraph;
    }

    public Iterable<CGraphNode<T>> getNodes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return String.format("CLGraph {graph: %s}", Arrays.toString(this.graph));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CLGraph<T> other = (CLGraph<T>) obj;
        if (!Arrays.deepEquals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Arrays.deepHashCode(this.graph);
        return hash;
    }
}
