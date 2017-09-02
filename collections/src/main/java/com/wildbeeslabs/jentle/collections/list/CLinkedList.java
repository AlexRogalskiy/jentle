package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.IList;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom linked list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CLinkedList<T> implements IList<T> {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CLinkedList.class);

    protected static class CLinkedListNode<T> {

        private T data;
        private CLinkedListNode<T> previous;
        private CLinkedListNode<T> next;
        private final Comparator<? super T> cmp;

        public CLinkedListNode() {
            this(null);
        }

        public CLinkedListNode(final T data) {
            this(data, null, null);
        }

        public CLinkedListNode(final T data, final CLinkedListNode<T> previous, final CLinkedListNode<T> next) {
            this(data, previous, next, CSort.DEFAULT_SORT_COMPARATOR);
        }

        public CLinkedListNode(final T data, final CLinkedListNode<T> previous, final CLinkedListNode<T> next, final Comparator<? super T> cmp) {
            this.data = data;
            this.previous = previous;
            this.next = next;
            this.cmp = cmp;
        }

        @Override
        public String toString() {
            return String.format("CLinkedListNode {data: %s, previous: %s, next: %s}", this.data, this.previous, this.next);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CLinkedListNode<T> other = (CLinkedListNode<T>) obj;
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            if (!Objects.equals(this.previous, other.previous)) {
                return false;
            }
            if (!Objects.equals(this.next, other.next)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.data);
            hash = 37 * hash + Objects.hashCode(this.previous);
            hash = 37 * hash + Objects.hashCode(this.next);
            return hash;
        }
    }

    protected CLinkedListNode<T> first;
    protected CLinkedListNode<T> last;
    protected int size;
    protected final Comparator<? super T> cmp;

    public CLinkedList() {
        this(null, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CLinkedList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CLinkedList(final CLinkedList<? extends T> source) {
        this(source, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CLinkedList(final CLinkedList<? extends T> source, final Comparator<? super T> cmp) {
        this.first = this.last = null;
        this.size = 0;
        this.cmp = cmp;
        this.addList(source);
    }

    public void addList(final CLinkedList<? extends T> source) {
        if (Objects.nonNull(source)) {
            for (CLinkedListNode<? extends T> current = source.first; current != null; current = current.next) {
                this.addLast(current.data);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public void clear() {
        this.first = this.last = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean offer(T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T poll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(T value) throws EmptyListException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Queue<T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addLast(final T item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getAt(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteDuplicates(final CLinkedListNode<T> node) {
        Set<T> set = new HashSet<>();
        CLinkedListNode<T> previous = node, current = node;
        while (Objects.nonNull(current)) {
            if (set.contains(current.data)) {
                previous.next = current.next;
            } else {
                set.add(current.data);
                previous = current;
            }
            current = current.next;
        }
    }

    public void deleteDuplicates2(final CLinkedListNode<T> node) {
        CLinkedListNode<T> current = node;
        while (Objects.nonNull(current)) {
            CLinkedListNode<T> runner = current;
            while (Objects.nonNull(runner.next)) {
                if (Objects.compare(runner.next.data, current.data, this.cmp) == 0) {
                    runner.next = runner.next.next;
                } else {
                    runner = runner.next;
                }
            }
            current = current.next;
        }
    }

    public T getKthToLast(final CLinkedListNode<T> node, int k) {
        Integer idx = new Integer(0);
        CLinkedListNode<T> current = this.getKthToLast(node, k, idx);
        return Objects.nonNull(current) ? current.data : null;
    }

    private CLinkedListNode<T> getKthToLast(final CLinkedListNode<T> node, int k, Integer idx) {
        if (this.isEmpty()) {
            return null;
        }
        CLinkedListNode<T> current = getKthToLast(node.next, k, idx);
        idx = idx + 1;
        if (idx.intValue() == k) {
            return node;
        }
        return current;
    }

    public T getKthToLast2(final CLinkedListNode<T> node, int k) {
        CLinkedListNode<T> p1 = node, p2 = node;
        for (int i = 0; i < k; i++) {
            if (Objects.isNull(p1)) {
                return null;
            }
            p1 = p1.next;
        }
        while (Objects.nonNull(p1)) {
            p1 = p1.next;
            p2 = p2.next;
        }
        return p2.data;
    }

    @Override
    public String toString() {
        return String.format("CLinkedList {first: %s, last: %s, size: %i}", this.first, this.last, this.size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CLinkedList<T> other = (CLinkedList<T>) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.last, other.last)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.first);
        hash = 41 * hash + Objects.hashCode(this.last);
        hash = 41 * hash + this.size;
        return hash;
    }
}
