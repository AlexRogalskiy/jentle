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
import com.wildbeeslabs.jentle.collections.graph.node.ACGraphNode;
import com.wildbeeslabs.jentle.collections.list.node.ACListGraphNode;
import com.wildbeeslabs.jentle.collections.list.node.CListGraphNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    public static <T> boolean paintFill(final T[][] screen, int row, int column, final T value) {
        Objects.requireNonNull(screen);
        if (Objects.equals(value, screen[row][column])) {
            return false;
        }
        return paintFill(screen, row, column, screen[row][column], value);
    }

    private static <T> boolean paintFill(final T[][] screen, int row, int column, final T oldValue, final T newValue) {
        if (row < 0 || row >= screen.length || column < 0 || column >= screen[0].length) {
            return false;
        }
        if (Objects.equals(oldValue, screen[row][column])) {
            screen[row][column] = newValue;
            paintFill(screen, row - 1, column, oldValue, newValue);
            paintFill(screen, row + 1, column, oldValue, newValue);
            paintFill(screen, row, column - 1, oldValue, newValue);
            paintFill(screen, row, column + 1, oldValue, newValue);
        }
        return true;
    }

    public static <T extends Entry, E extends ACGraphNode<T, E>, U extends ACListGraphNode<T, E, U>> List<E> findPathBiBFS(final Map<Integer, E> map, int source, int destination) {
        final BFSData<T, E, U> sourceData = new BFSData(map.get(source));
        final BFSData<T, E, U> destData = new BFSData(map.get(destination));
        while (!sourceData.isFinished() && !destData.isFinished()) {
            T collision = searchLevel(map, sourceData, destData);
            if (Objects.nonNull(collision)) {
                return mergePaths(sourceData, destData, collision.getId());
            }
            collision = searchLevel(map, destData, sourceData);
            if (Objects.nonNull(collision)) {
                return mergePaths(sourceData, destData, collision.getId());
            }
        }
        return null;
    }

    private static <T extends Entry, E extends ACGraphNode<T, E>, U extends ACListGraphNode<T, E, U>> T searchLevel(final Map<Integer, E> map, final BFSData<T, E, U> primary, final BFSData<T, E, U> secondary) {
        int count = primary.toVisit.size();
        for (int i = 0; i < count; i++) {
            final U pathNode = primary.toVisit.poll();
            int personId = pathNode.getData().getData().getId();
            if (secondary.visited.containsKey(personId)) {
                return pathNode.getData().getData();
            }
            final E data = pathNode.getData();
            final List<E> adjacentList = data.getAdjacents();
            adjacentList.stream().filter((adjacent) -> (!primary.visited.containsKey(adjacent.getData().getId()))).map((adjacent) -> {
                final E current = map.get(adjacent.getData().getId());
                final U next = (U) new CListGraphNode<>(current, (CListGraphNode) pathNode, null);
                primary.visited.put(adjacent.getData().getId(), (U) next);
                return next;
            }).forEach((next) -> {
                primary.toVisit.add((U) next);
            });
        }
        return null;
    }

    private static <T extends Entry, E extends ACGraphNode<T, E>, U extends ACListGraphNode<T, E, U>> List<E> mergePaths(final BFSData<T, E, U> primary, final BFSData<T, E, U> secondary, int connection) {
        final U end1 = primary.visited.get(connection);
        final U end2 = secondary.visited.get(connection);
        final LinkedList<E> pathOne = end1.collapse(false);
        final LinkedList<E> pathTwo = end2.collapse(true);
        pathTwo.removeFirst();
        pathOne.addAll(pathTwo);
        return pathOne;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class BFSData<T extends Entry, E extends ACGraphNode<T, E>, U extends ACListGraphNode<T, E, U>> {

        public final Queue<U> toVisit = new LinkedList<>();
        public final Map<Integer, U> visited = new HashMap<>();

        public BFSData(final E root) {
            final U sourcePath = (U) new CListGraphNode<>(root);
            this.toVisit.add(sourcePath);
            this.visited.put(root.getData().getId(), sourcePath);
        }

        public boolean isFinished() {
            return this.toVisit.isEmpty();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class Entry {

        private final Integer id;
        private String info;

        public Entry(final Integer id) {
            this.id = id;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class Machine<E extends Entry> {

        private Map<Integer, E> entryMap = new HashMap<>();
        private final Integer id;
        private String info;

        public Machine(final Integer id) {
            this.id = id;
        }

        public Entry getEntryById(final Integer entryId) {
            return this.entryMap.get(entryId);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class Cluster<M extends Machine> {

        private Map<Integer, M> machineMap = new HashMap<>();
        private Map<Integer, Integer> entryToMachineMap = new HashMap();

        public M getMachineById(final Integer machineId) {
            return this.machineMap.get(machineId);
        }

        public Integer getMachineIdByEntryId(final Integer entryId) {
            return this.entryToMachineMap.get(entryId);
        }

        public Entry getEntryById(final Integer entryId) {
            final Integer machineId = this.entryToMachineMap.get(entryId);
            if (Objects.isNull(machineId)) {
                return null;
            }
            final M machine = getMachineById(machineId);
            if (Objects.isNull(machine)) {
                return null;
            }
            return machine.getEntryById(entryId);
        }
    }
}
