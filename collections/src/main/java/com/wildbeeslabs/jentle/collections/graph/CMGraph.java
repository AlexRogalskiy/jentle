package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.collections.interfaces.IGraph;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom matrix-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CMGraph<T> implements IGraph<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected T[][] graph;

    public CMGraph(final Class<? extends T> clazz, int numOfVertex) {
        this.graph = this.newArray(clazz, numOfVertex);
    }

    private T[][] newArray(Class<? extends T> type, int size) {
        return (T[][]) Array.newInstance(type, size, size);
    }

    public boolean has(int a, int b) {
        return (null != this.get(a, b));
    }

    public void set(int a, int b, final T data) {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a - 1][b - 1] = data;
    }

    public void remove(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a - 1][b - 1] = null;
    }

    public T get(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        return this.graph[a - 1][b - 1];
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
        int s = 0;
        for (int i = 0; i < this.graph[a].length; i++) {
            if (this.has(a, i)) {
                s++;
            }
        }
        return s;
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
        return String.format("CMGraph {graph: %s}", Arrays.toString(this.graph));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CMGraph other = (CMGraph) obj;
        if (!Arrays.deepEquals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Arrays.deepHashCode(this.graph);
        return hash;
    }
}
