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

import com.wildbeeslabs.jentle.collections.graph.CGraph.CGraphNode;
import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static com.wildbeeslabs.jentle.collections.utils.CUtils.newMatrix;

/**
 * Custom matrix graph implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CMGraph<T> extends ACGraph<T, CGraphNode<T>> {

    protected CGraphNode<T>[][] graph;

    public CMGraph(final Class<? extends CGraphNode<T>> clazz, int numOfVertex) {
        this.graph = newMatrix(clazz, numOfVertex, numOfVertex);
    }

    public boolean has(int from, int to) {
        return Objects.nonNull(this.get(from, to));
    }

    public void set(int from, int to, final T data) {
        this.add(from, to, data);
    }

    private T getDataByDefault() {
        try {
            return (T) this.graph.getClass().getComponentType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (InvocationTargetException ex) {
            return null;
        }
    }

    public void add(int from, int to) {
        this.add(from, to, this.getDataByDefault());
    }

    public void add(int from, int to, final T data) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1][to - 1] = new CGraphNode<>(data);
    }

    public void remove(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1][to - 1] = null;
    }

    public T get(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        if (Objects.isNull(this.graph[from - 1][to - 1])) {
            return null;
        }
        return this.graph[from - 1][to - 1].getData();
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
        final CLGraph<T> lGraph = new CLGraph<>(this.size());
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.graph[i].length; j++) {
                final T temp = this.get(i, j);
                if (Objects.nonNull(temp)) {
                    lGraph.add(i, j, temp);
                }
            }
        }
        return lGraph;
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (vertex=%i is out of bounds [1, %i])", this.getClass().getName(), index, this.size()));
        }
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
        if (!(obj instanceof CMGraph)) {
            return false;
        }
        final CMGraph<T> other = (CMGraph<T>) obj;
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
