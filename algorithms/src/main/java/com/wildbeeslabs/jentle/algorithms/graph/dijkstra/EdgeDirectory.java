package com.wildbeeslabs.jentle.algorithms.graph.dijkstra;

import java.util.Iterator;

/**
 * Represents a directory of edges for use by the {@link DijkstraAlgorithm}.
 */
public interface EdgeDirectory {

    /**
     * Returns the penalty between two vertices.
     * @param start the start vertex
     * @param end the end vertex
     * @return the penalty between two vertices, or 0 if no single edge between the two vertices
     *                  exists.
     */
    int getPenalty(Vertex start, Vertex end);

    /**
     * Returns an iterator over all valid destinations for a given vertex.
     * @param origin the origin from which to search for destinations
     * @return the iterator over all valid destinations for a given vertex
     */
    Iterator getDestinations(Vertex origin);

}
