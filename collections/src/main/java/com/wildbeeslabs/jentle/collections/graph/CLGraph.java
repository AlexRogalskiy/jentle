package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.IList;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom list-graph implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CLGraph<T> implements IGraph<T> {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CLGraph.class);

    protected static class CLGraphArc<T> {

        private int end;
        private T data;

        public CLGraphArc(int end) {
            this(end, null);
        }

        public CLGraphArc(int end, final T data) {
            this.end = end;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("CLGraphArc {data: %s, end: %s}", this.data, this.end);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CLGraphArc<T> other = (CLGraphArc<T>) obj;
            if (this.end != other.end) {
                return false;
            }
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + this.end;
            hash = 67 * hash + Objects.hashCode(this.data);
            return hash;
        }
    }

    protected IList<CLGraphArc<T>>[] graph;
    protected final Comparator<? super T> cmp;

    public CLGraph(int size) {
        this(size, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CLGraph(int size, final Comparator<? super T> cmp) {
        this.graph = this.newArray((Class<? extends IList<CLGraphArc<T>>[]>) this.graph.getClass(), size);
        this.cmp = cmp;
    }

    @Override
    public void add(int from, int to, final T data) {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].addLast(new CLGraphArc<>(to, data));
    }

    public void add(int from, int to) {
        this.add(from, to, null);
    }

    public void remove(int from, int to) throws EmptyListException {
        this.checkRange(from);
        this.checkRange(to);
        this.graph[from - 1].remove(new CLGraphArc<>(to));
    }

    private CLGraphArc<T> getItem(int from, int to) {
        this.checkRange(from);
        this.checkRange(to);
        return this.graph[from - 1].getAt(to);
    }

    public T get(int from, int to) {
        CLGraphArc<T> temp = this.getItem(from, to);
        if (null != temp) {
            return temp.data;
        }
        return null;
    }

    public boolean set(int from, int to, final T data) {
        CLGraphArc<T> temp = this.getItem(from, to);
        if (null != temp) {
            temp.data = data;
            return true;
        }
        return false;
    }

    public int cardOut(int from) {
        this.checkRange(from);
        return this.graph[from - 1].size();
    }

    private IList<CLGraphArc<T>>[] newArray(Class<? extends IList<CLGraphArc<T>>[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    public int size() {
        return this.graph.length;
    }

    public void clear() {
        this.graph = this.newArray((Class<? extends IList<CLGraphArc<T>>[]>) this.graph.getClass(), this.size());
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CLGraph (vertex=%i is out of bounds [1, %i])", index, this.size()));
        }
    }

    public IGraph<Integer> toCSGraph() {
        IGraph<Integer> sGraph = new CSGraph(this.size());
        for (int i = 0; i < this.size(); i++) {
            for (Iterator<CLGraphArc<T>> it = this.graph[i].iterator(); it.hasNext();) {
                CLGraphArc<T> node = it.next();
                sGraph.add(i, node.end, null);
            }
        }
        return sGraph;
    }

    @Override
    public String toString() {
        return String.format("CLGraph {graph: %s}", Arrays.toString(this.graph));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CLGraph<T> other = (CLGraph<T>) obj;
        if (!Arrays.deepEquals(this.graph, other.graph)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Arrays.deepHashCode(this.graph);
        return hash;
    }
}
