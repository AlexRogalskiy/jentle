package com.wildbeeslabs.jentle.algorithms.graph.dijkstra;

/**
 * Represents a vertex to be used by {@link DijkstraAlgorithm}. If you want to represent a city,
 * you can do "public class City implements Vertex". The purpose of this interface is to make
 * sure the Vertex implementation implements the Comparable interface so the sorting order is
 * well-defined even when two vertices have the same penalty/distance from an origin point.
 * Therefore, make sure you implement the <code>compareTo(Object)</code> and
 * <code>equals(Object)</code> methods.
 */
public interface Vertex extends Comparable {

}
