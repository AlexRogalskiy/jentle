package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.Visitor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CList<T> implements IList<T> {

    /**
     * Default Logger instance
     */
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected static class CListNode<T> {

        private T data;
        private final Comparator<? super T> cmp;
        private CListNode<T> next;

        public CListNode(final T data) {
            this(data, null);
        }

        public CListNode(final T data, final CListNode<T> next) {
            this(data, next, CSort.DEFAULT_SORT_COMPARATOR);
        }

        public CListNode(final T data, final CListNode<T> next, final Comparator<? super T> cmp) {
            this.data = data;
            this.next = next;
            this.cmp = cmp;
        }

        @Override
        public String toString() {
            return String.format("CListNode {data: %s, next: %s}", this.data, this.next);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CListNode<T> other = (CListNode<T>) obj;
            if (!Objects.equals(this.data, other.data)) {
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
            hash = 11 * hash + Objects.hashCode(this.data);
            hash = 11 * hash + Objects.hashCode(this.next);
            return hash;
        }
    }

    private CListNode<T> first;
    private CListNode<T> last;
    private int size;

    public CList() {
        this.first = this.last = null;
        this.size = 0;
    }

    public CList(final CList<T> source) {
        this.addList(source);
    }

    public final void addList(final CList<T> source) {
        for (CListNode<T> current = source.first; current != null; current = current.next) {
            this.addLast(current.data);
        }
    }

    public void addFirst(T item) {
        @SuppressWarnings("Convert2Diamond")
        CListNode<T> temp = new CListNode<T>(item, this.first);
        if (null == this.first) {
            this.last = temp;
        }
        this.first = temp;
        this.size++;
    }

    public void addLast(T item) {
        @SuppressWarnings("Convert2Diamond")
        CListNode<T> temp = new CListNode<T>(item, null);
        if (null == this.last) {
            this.first = temp;
        } else {
            this.last.next = temp;
        }
        this.last = temp;
        this.size++;
    }

    public T removeFirst() throws EmptyListException {
        if (this.isEmpty()) {
            //throw new EmptyListException(String.format("ERROR: CList (empty size=%i)", this.size()));
            return null;
        }
        T removed = this.first.data;
        this.first = this.first.next;
        this.size--;
        return removed;
    }

    public T removeLast() throws EmptyListException {
        if (this.isEmpty()) {
            //throw new EmptyListException(String.format("ERROR: CList (empty size=%i)", this.size()));
            return null;
        }
        CListNode<T> previous = null, next = this.first;
        while (null != next.next) {
            previous = next;
            next = next.next;
        }
        if (null == previous) {
            this.first = null;
        } else {
            previous.next = null;
        }
        this.last = previous;
        this.size--;
        return next.data;
    }

    @Override
    public boolean remove(T item) throws EmptyListException {
        if (this.isEmpty()) {
            //hrow new EmptyListException(String.format("ERROR: CList (empty size=%i)", this.size()));
            return false;
        }
        CListNode<T> previous = null, next = this.first;
        boolean removed = false;
        while (null != next) {
            if (Objects.equals(item, next.data)) {
                removed = true;
                this.size--;
                if (null != previous) {
                    previous.next = next.next;
                }
            } else {
                previous = next;
            }
            next = next.next;
        }
        this.last = previous;
        return removed;
    }

    public void insertAt(T item, int index) {
        this.checkRange(index);
        CListNode<T> previous = null, next = this.first;
        while (null != next && index-- > 0) {
            previous = next;
            next = next.next;
        }
        @SuppressWarnings("Convert2Diamond")
        CListNode<T> temp = new CListNode<T>(item, next);
        if (null == next) {
            this.last = temp;
        }
        if (null == previous) {
            this.first = temp;
        } else {
            previous.next = temp;
        }
        this.size++;
    }

    public T getFirst() {
        if (this.isEmpty()) {
            return null;
        }
        return this.first.data;
    }

    @Override
    public boolean contains(T item) {
        for (Iterator<T> i = this.iterator(); i.hasNext();) {
            if (Objects.equals(i.next(), item)) {
                return true;
            }
        }
        return false;
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CList (index=%i is out of bounds [0, %i])", index, this.size - 1));
        }
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    public boolean isDistinct() {
        for (Iterator<T> i = this.iterator(); i.hasNext();) {
            T item = i.next();
            for (Iterator<T> j = this.iterator(); j.hasNext();) {
                if (Objects.equals(j.next(), item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void each(final Visitor visitor) {
        for (Iterator<T> current = this.iterator(); current.hasNext();) {
            visitor.visit(current.next());
        }
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
    public void clear() {
        this.first = this.last = null;
    }

    @Override
    public int size() {
        return this.size;
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

    //@Override
    @SuppressWarnings("Convert2Diamond")
    public Iterator<T> iterator() {
        return new CListIterator<T>(this);
    }

    protected static class CListIterator<T> implements Iterator<T> {

        private CListNode<T> cursor = null;

        public CListIterator(final CList<T> source) {
            this.cursor = source.first;
        }

        @Override
        public boolean hasNext() {
            return (null != this.cursor);
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            T current = this.cursor.data;
            this.cursor = this.cursor.next;
            return current;
        }

        @Override
        public void remove() {
            //
        }
    }

    @Override
    public String toString() {
        return String.format("CList {first: %s, last: %s, size: %i}", this.first, this.last, this.size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        CList<T> another = (CList<T>) obj;
        boolean equal = true;
        Iterator<T> list1 = this.iterator();
        Iterator<T> list2 = another.iterator();
        for (; equal && list1.hasNext() && list2.hasNext();) {
            equal = list1.next().equals(list2.next());
        }
        return equal && !list1.hasNext() && !list2.hasNext();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.last);
        hash = 97 * hash + this.size;
        return hash;
    }
}
