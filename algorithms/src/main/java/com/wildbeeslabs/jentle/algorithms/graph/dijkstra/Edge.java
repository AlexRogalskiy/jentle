package com.wildbeeslabs.jentle.algorithms.graph.dijkstra;

/**
 * Represents an edge (or direct route between two points) for the {@link DijkstraAlgorithm}.
 * Implement this class to hold the start and end vertex for an edge and implement the
 * <code>getPenalty()</code> method.
 */
public interface Edge {

    /**
     * Returns the start vertex of the edge.
     * @return the start vertex
     */
    Vertex getStart();

    /**
     * Returns the end vertex of the edge.
     * @return the end vertex
     */
    Vertex getEnd();

    /**
     * Returns the penalty (or distance) for this edge.
     * @return the penalty value (must be non-negative)
     */
    int getPenalty();

}
