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

import com.wildbeeslabs.jentle.collections.graph.iface.IGraph;
import com.wildbeeslabs.jentle.collections.graph.impl.CGraph.CGraphNode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Custom set graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSGraph extends ACGraph<Integer, CGraphNode<Integer>> {

    protected Set<CGraphNode<Integer>>[] graph;

    public CSGraph(int numOfVertex) {
        this.graph = new HashSet[numOfVertex];
        for (int i = 0; i < numOfVertex; i++) {
            this.graph[i] = new HashSet<>();
        }
    }

    public boolean has(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1].contains(new CGraphNode<>(to));
    }

    public void add(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].add(new CGraphNode<>(to));
    }

    public void remove(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].remove(new CGraphNode<>(to));
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
        return this.graph[from - 1].size();
    }

    @Override
    public int size() {
        return this.graph.length;
    }

    public IGraph<Integer> toCMGraph() {
        final CMGraph<Integer> mGraph = new CMGraph<>((Class<? extends CGraphNode<Integer>>) this.graph.getClass().getComponentType(), this.size());
        for (int i = 0; i < this.size(); i++) {
            for (Iterator<? extends CGraphNode<Integer>> it = this.graph[i].iterator(); it.hasNext(); ) {
                mGraph.add(i, it.next().getData(), 1);
            }
        }
        return mGraph;
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (vertex=%i is out of bounds [1, %i])", this.getClass().getName(), index, this.size()));
        }
    }

    @Override
    public Iterator<Integer> iterator() {
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
        if (!(obj instanceof CSGraph)) {
            return false;
        }
        final CSGraph other = (CSGraph) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(Arrays.deepEquals(this.graph, other.graph), true)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(51, 31)
            .appendSuper(super.hashCode())
            .append(Arrays.deepHashCode(this.graph))
            .toHashCode();
    }
}
