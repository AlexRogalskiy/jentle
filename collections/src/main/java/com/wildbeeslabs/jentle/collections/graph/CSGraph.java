package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.ISet;
import com.wildbeeslabs.jentle.collections.set.CBitSet;

import java.util.Arrays;

/**
 *
 * Custom set-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSGraph implements IGraph<Integer> {

    protected ISet<Integer>[] graph;

    public CSGraph(int size) {
        this.graph = new CBitSet[size];
        for (int i = 0; i < size; i++) {
            this.graph[i] = new CBitSet(0, size - 1);
        }
    }

    public boolean has(int a, int b) {
        return this.graph[a].has(b);
    }

    public void add(int a, int b) {
        this.graph[a].disjunct(b);
    }

    public void remove(int a, int b) {
        this.graph[a].remove(b);
    }

    public int cardIn(int a) {
        int s = 0;
        for (int i = 0; i < this.graph.length; i++) {
            if (this.has(i, a)) {
                s++;
            }
        }
        return s;
    }

    public int cardOut(int a) {
        return this.graph[a].size();
    }

    @Override
    public String toString() {
        return String.format("CSGraph {graph: %s}", this.graph.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CSGraph other = (CSGraph) obj;
        if (!Arrays.deepEquals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Arrays.deepHashCode(this.graph);
        return hash;
    }
}
