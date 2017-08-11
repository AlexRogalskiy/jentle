package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.ISet;
import com.wildbeeslabs.jentle.collections.set.CBitSet;

import java.util.Arrays;
import java.util.Iterator;

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
    protected static final Logger LOGGER = LogManager.getLogger(CSGraph.class);

    protected ISet<Integer>[] graph;

    public CSGraph(int numOfVertex) {
        this.graph = new CBitSet[numOfVertex];
        for (int i = 0; i < numOfVertex; i++) {
            this.graph[i] = new CBitSet(0, numOfVertex - 1);
        }
    }

    public boolean has(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1].has(new Integer(to));
    }

    @Override
    public void add(int from, int to, final Integer data) {
        this.add(from, to);
    }

    public void add(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].disjunct(new Integer(to));
    }

    public void remove(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].remove(new Integer(to));
    }

    public int cardIn(int to) {
        this.checkRange(to);
        int s = 0;
        for (int i = 0; i < this.graph.length; i++) {
            if (this.has(i, to)) {
                s++;
            }
        }
        return s;
    }

    public int cardOut(int from) {
        this.checkRange(from);
        return this.graph[from - 1].size();
    }

    public int size() {
        return this.graph.length;
    }

    public IGraph<Integer> toCMGraph() {
        IGraph<Integer> mGraph = new CMGraph<>((Class<? extends Integer>) this.graph.getClass().getComponentType(), this.size());
        for (int i = 0; i < this.size(); i++) {
            for (Iterator<Integer> it = this.graph[i].iterator(); it.hasNext();) {
                mGraph.add(i, it.next().intValue(), 1);
            }
        }
        return mGraph;
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
