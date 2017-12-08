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
package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.collections.graph.CGraph;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * Custom graph utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CGraphUtils {

    private CGraphUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> CGraph.CGraphNode<T>[] findTasksOrder(final T[] tasks, final T[][] dependencies) {
        final CGraph<T> graph = buildGraph(tasks, dependencies);
        return orderTasks(graph.getNodes());
    }

    private static <T> CGraph<T> buildGraph(final T[] tasks, final T[][] dependencies) {
        final CGraph<T> graph = new CGraph<>();
        for (final T task : tasks) {
            graph.createNode(task);
        }
        for (final T[] dependency : dependencies) {
            final T first = dependency[0];
            final T second = dependency[1];
            graph.addEdge(first, second);
        }
        return graph;
    }

    private static <T> CGraph.CGraphNode<T>[] orderTasks(final List<CGraph.CGraphNode<T>> tasks) {
        final CGraph.CGraphNode<T>[] order = new CGraph.CGraphNode[tasks.size()];
        int endOfList = addNonDependent(order, tasks, 0);
        int toBeProcessed = 0;
        while (toBeProcessed < order.length) {
            final CGraph.CGraphNode<T> current = order[toBeProcessed];
            if (Objects.isNull(current)) {
                return null;
            }
            final Iterable<CGraph.CGraphNode<T>> children = current.getAdjacents();
            for (final CGraph.CGraphNode<T> child : children) {
                child.decDependencies();
            }
            endOfList = addNonDependent(order, children, endOfList);
            toBeProcessed++;
        }
        return order;
    }

    private static <T> int addNonDependent(final CGraph.CGraphNode<T>[] order, final Iterable<CGraph.CGraphNode<T>> tasks, int offset) {
        for (final CGraph.CGraphNode<T> task : tasks) {
            if (task.getDependencies() == 0) {
                order[offset++] = task;
            }
        }
        return offset;
    }

    public static <T> Stack<CGraph.CGraphNode<T>> findTasksOrder2(final T[] tasks, final T[][] dependencies) {
        final CGraph<T> graph = buildGraph(tasks, dependencies);
        return orderTasks2(graph.getNodes());
    }

    private static <T> Stack<CGraph.CGraphNode<T>> orderTasks2(final List<CGraph.CGraphNode<T>> tasks) {
        final Stack<CGraph.CGraphNode<T>> stack = new Stack();
        for (final CGraph.CGraphNode<T> task : tasks) {
            if (Objects.equals(task.getState(), CGraph.CGraphNode.State.UNVISITED)) {
                if (!doDFS(task, stack)) {
                    return null;
                }
            }
        }
        return stack;
    }

    private static <T> boolean doDFS(final CGraph.CGraphNode<T> task, final Stack<CGraph.CGraphNode<T>> stack) {
        if (Objects.equals(task.getState(), CGraph.CGraphNode.State.VISITING)) {
            return false;
        }
        if (Objects.equals(task.getState(), CGraph.CGraphNode.State.UNVISITED)) {
            task.setState(CGraph.CGraphNode.State.VISITING);
            final List<CGraph.CGraphNode<T>> children = task.getAdjacents();
            if (!children.stream().noneMatch((child) -> (!doDFS(child, stack)))) {
                return false;
            }
            task.setState(CGraph.CGraphNode.State.VISITED);
            stack.push(task);
        }
        return true;
    }
}
