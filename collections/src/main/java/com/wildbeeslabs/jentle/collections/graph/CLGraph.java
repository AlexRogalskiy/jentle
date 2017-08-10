package com.wildbeeslabs.jentle.collections.graph;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.IGraph;
import com.wildbeeslabs.jentle.collections.interfaces.IList;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Comparator;
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
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

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

    public void add(int a, int b, final T data) {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a - 1].addLast(new CLGraphArc<>(b, data));
    }

    public void add(int a, int b) {
        this.add(a, b, null);
    }

    public void remove(int a, int b) throws EmptyListException {
        this.checkRange(a);
        this.checkRange(b);
        this.graph[a - 1].remove(new CLGraphArc<>(b));
    }

    private CLGraphArc<T> getItem(int a, int b) {
        this.checkRange(a);
        this.checkRange(b);
        return this.graph[a - 1].getAt(b);
    }

    public T get(int a, int b) {
        CLGraphArc<T> temp = this.getItem(a, b);
        if (null != temp) {
            return temp.data;
        }
        return null;
    }

    public boolean set(int a, int b, final T data) {
        CLGraphArc<T> temp = this.getItem(a, b);
        if (null != temp) {
            temp.data = data;
            return true;
        }
        return false;
    }

    public int cardOut(int a) {
        this.checkRange(a);
        return this.graph[a-1].size();
    }

    private IList<CLGraphArc<T>>[] newArray(Class<? extends IList<CLGraphArc<T>>[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    public int size() {
        return this.graph.length;
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CLGraph (vertex=%i is out of bounds [1, %i])", index, this.size()));
        }
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
