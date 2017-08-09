package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.ISet;
import com.wildbeeslabs.jentle.collections.set.CBitSet;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom set-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSGraph implements IGraph<Integer> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected ISet<Integer>[] graph;

    public CSGraph(int numOfVertex) {
        this.graph = new CBitSet[numOfVertex];
        for (int i = 0; i < numOfVertex; i++) {
            this.graph[i] = new CBitSet(0, numOfVertex - 1);
        }
    }

    public boolean has(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        return this.graph[a-1].has(b);
    }

    public void set(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a-1].disjunct(b);
    }

    public void remove(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a-1].remove(b);
    }

    public int cardIn(int a) {
        this.checkRange(a);
        int s = 0;
        for (int i = 0; i < this.graph.length; i++) {
            if (this.has(i, a)) {
                s++;
            }
        }
        return s;
    }

    public int cardOut(int a) {
        this.checkRange(a);
        return this.graph[a-1].size();
    }

    public int size() {
        return this.graph.length;
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CMGraph (vertex=%i is out of bounds [1, %i])", index, this.size()));
        }
    }

    @Override
    public String toString() {
        return String.format("CSGraph {graph: %s}", Arrays.toString(this.graph));
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
