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
package com.wildbeeslabs.jentle.algorithms.graph;

import com.wildbeeslabs.jentle.collections.graph.CLGraph;
import com.wildbeeslabs.jentle.collections.graph.node.CGraphNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * Custom graph algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CGraph {

    private CGraph() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> boolean search(final CLGraph<T> graph, final CGraphNode<T> start, final CGraphNode<T> end) {//IGraph<T>
        if (start == end) {
            return true;
        }
        final Deque<CGraphNode<T>> listNode = new LinkedList<>();
        for (final CGraphNode<T> node : graph.getNodes()) {
            node.setState(CGraphNode.State.UNVISITED);
        }
        start.setState(CGraphNode.State.VISITING);
        listNode.add(start);
        while (!listNode.isEmpty()) {
            CGraphNode<T> temp = listNode.removeFirst();
            if (Objects.nonNull(temp)) {
                for (final CGraphNode<T> v : temp.getAdjacents()) {
                    if (Objects.equals(v.getState(), CGraphNode.State.UNVISITED)) {
                        if (v == end) {
                            return true;
                        } else {
                            v.setState(CGraphNode.State.VISITING);
                            listNode.add(v);
                        }
                    }
                }
                temp.setState(CGraphNode.State.VISITED);
            }
        }
        return false;
    }
}