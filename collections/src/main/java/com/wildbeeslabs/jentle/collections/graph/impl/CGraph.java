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

import com.wildbeeslabs.jentle.collections.graph.node.ACGraphNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom list based graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CGraph<T> extends ACGraph<T, CGraph.CGraphNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CGraphNode<T> extends ACGraphNode<T, CGraphNode<T>> {

        @Setter(AccessLevel.NONE)
        protected int dependencies;

        public CGraphNode() {
            this(null);
        }

        public CGraphNode(final T data) {
            this(data, CUtils.DEFAULT_SORT_COMPARATOR);
        }

        public CGraphNode(final T data, final Comparator<? super T> cmp) {
            super(data, cmp);
            this.dependencies = 0;
        }

        @Override
        public void addAdjacent(final CGraphNode<T> node) {
            if (!this.adjacents.contains(node)) {
                this.addAdjacent(node);
                node.incDependencies();
            }
        }

        @Override
        public void removeAdjacent(final CGraphNode<T> node) {
            if (this.adjacents.contains(node)) {
                this.removeAdjacent(node);
                node.decDependencies();
            }
        }

        public void incDependencies() {
            this.dependencies++;
        }

        public void decDependencies() {
            this.dependencies--;
        }
    }

    @Setter(AccessLevel.NONE)
    protected List<CGraph.CGraphNode<T>> nodes = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    protected Map<T, CGraph.CGraphNode<T>> map = new HashMap<>();
    protected final Comparator<? super T> cmp;

    public CGraph() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CGraph(final Comparator<? super T> cmp) {
        this.cmp = cmp;
    }

    public void createNode(final T data) {
        if (!this.map.containsKey(data)) {
            final CGraph.CGraphNode<T> node = new CGraph.CGraphNode<>(data);
            this.nodes.add(node);
            this.map.put(data, node);
        }
    }

    public CGraph.CGraphNode<T> getNode(final T data) {
        return this.map.get(data);
    }

    public CGraph.CGraphNode<T> getOrCreateNode(final T data) {
        this.createNode(data);
        return this.getNode(data);
    }

    public void addEdge(final T start, final T end) {
        final CGraph.CGraphNode<T> startNode = this.getOrCreateNode(start);
        final CGraph.CGraphNode<T> endNode = this.getOrCreateNode(end);
        startNode.addAdjacent(endNode);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
