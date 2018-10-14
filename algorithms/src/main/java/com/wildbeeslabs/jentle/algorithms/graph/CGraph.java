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

import com.wildbeeslabs.jentle.collections.graph.CSGraph2;
import com.wildbeeslabs.jentle.collections.graph.node.ACGraphNodeExtended;
import com.wildbeeslabs.jentle.collections.graph.node.CGraphNode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.cycle.HierholzerEulerianCycle;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedSubgraph;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 *
 * Custom graph algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CGraph {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CGraph.class);

    private CGraph() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> boolean search(final CGraphNode<T> start, final CGraphNode<T> end) {
        if (start == end) {
            return true;
        }
        final Deque<CGraphNode<T>> listNode = new LinkedList<>();
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

    public static <T, E extends ACGraphNodeExtended<T, E>> CSGraph2<T> calculateShortestPathFromSource(final CSGraph2<T> graph, final E source) {
        source.setDistance(BigDecimal.ZERO);
        final Set<E> settledNodes = new HashSet<>();
        final Set<E> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);
        while (!unsettledNodes.isEmpty()) {
            final E currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (final Entry<E, BigDecimal> adjacencyPair : currentNode.getAdjacents().entrySet()) {
                final E adjacentNode = adjacencyPair.getKey();
                final BigDecimal edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static <T, E extends ACGraphNodeExtended<T, E>> void calculateMinimumDistance(final E evaluationNode, final BigDecimal edgeWeight, final E sourceNode) {
        final BigDecimal sourceDistance = sourceNode.getDistance();
        if (sourceDistance.add(edgeWeight).compareTo(evaluationNode.getDistance()) < 0) {
            evaluationNode.setDistance(sourceDistance.add(edgeWeight));
            final LinkedList<E> shortestPath = new LinkedList<>(sourceNode.getPathNodes());
            shortestPath.add(sourceNode);
            evaluationNode.setPathNodes(shortestPath);
        }
    }

    private static <T, E extends ACGraphNodeExtended<T, E>> E getLowestDistanceNode(final Set<E> unsettledNodes) {
        E lowestDistanceNode = null;
        BigDecimal lowestDistance = BigDecimal.valueOf(Long.MAX_VALUE);
        for (final E node : unsettledNodes) {
            final BigDecimal nodeDistance = node.getDistance();
            if (nodeDistance.compareTo(lowestDistance) < 0) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    public static <V, E> GraphPath<V, E> getDijkstraShortestPath(final Graph<V, E> graph, final V source, final V target) {
        return DijkstraShortestPath.findPathBetween(graph, source, target);
    }

    public static <V, E> GraphPath<V, E> getBelmanFordShortestPath(final Graph<V, E> graph, final V source, final V target) {
        return BellmanFordShortestPath.findPathBetween(graph, source, target);
    }

    public static <V, E> List<DirectedSubgraph<V, E>> getStronglyConnectedSubgraph(final DirectedGraph<V, E> directedGraph) {
        final StrongConnectivityAlgorithm<V, E> scAlg = new KosarajuStrongConnectivityInspector<>(directedGraph);
        final List<DirectedSubgraph<V, E>> stronglyConnectedSubgraphs = scAlg.stronglyConnectedSubgraphs();
        return stronglyConnectedSubgraphs;
    }

    public static <V, E> GraphPath<V, E> getEluerianCycle(final Graph<V, E> graph) {
        final HierholzerEulerianCycle eulerianCycle = new HierholzerEulerianCycle<>();
        if (eulerianCycle.isEulerian(graph)) {
            return eulerianCycle.getEulerianCycle(graph);
        }
        return null;
    }

    public static <V, E> List<V> getHamiltonianCycle(final SimpleWeightedGraph<V, E> graph) {
        return HamiltonianCycle.getApproximateOptimalForCompleteGraph(graph);
    }

    public static <V, E> Set<V> getCycles(final DirectedGraph<V, E> directedGraph) {
        final CycleDetector<V, E> cycleDetector = new CycleDetector<>(directedGraph);
        if (cycleDetector.detectCycles()) {
            return cycleDetector.findCycles();
        }
        return Collections.EMPTY_SET;
    }
}
