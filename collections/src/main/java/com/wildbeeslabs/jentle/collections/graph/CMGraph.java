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

import com.wildbeeslabs.jentle.collections.interfaces.IGraph;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom matrix-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CMGraph<T> implements IGraph<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    protected T[][] graph;

    public CMGraph(final Class<? extends T> clazz, int numOfVertex) {
        this.graph = this.newArray(clazz, numOfVertex);
    }

    private T[][] newArray(Class<? extends T> type, int size) {
        return (T[][]) Array.newInstance(type, size, size);
    }

    public boolean has(int from, int to) {
        return (null != this.get(from, to));
    }

    public void set(int from, int to, final T data) {
        this.add(from, to, data);
    }

    private T getDataByDefault() {
        try {
            return (T) this.graph.getClass().getComponentType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public void add(int from, int to) {
        this.add(from, to, this.getDataByDefault());
    }

    @Override
    public void add(int from, int to, final T data) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1][to - 1] = data;
    }

    public void remove(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1][to - 1] = null;
    }

    public T get(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1][to - 1];
    }

    public int cardIn(int to) {
        this.checkRange(to);
        int s = 0;
        for (int i = 0; i < this.graph.length; i++) {
            if (this.has(i, to)) {
                s++;
            }
        }
        return s;
    }

    public int cardOut(int from) {
        this.checkRange(from);
        int s = 0;
        for (int i = 0; i < this.graph[from - 1].length; i++) {
            if (this.has(from, i)) {
                s++;
            }
        }
        return s;
    }

    public int size() {
        return this.graph.length;
    }

    public IGraph<? extends T> toCLGraph() {
        IGraph<T> lGraph = new CLGraph<>(this.size());
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.graph[i].length; j++) {
                T temp = this.get(i, j);
                if (Objects.nonNull(temp)) {
                    lGraph.add(i, j, temp);
                }
            }
        }
        return lGraph;
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CMGraph (vertex=%i is out of bounds [1, %i])", index, this.size()));
        }
    }

    @Override
    public String toString() {
        return String.format("CMGraph {graph: %s}", Arrays.toString(this.graph));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CMGraph<T> other = (CMGraph<T>) obj;
        if (!Arrays.deepEquals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Arrays.deepHashCode(this.graph);
        return hash;
    }
}
