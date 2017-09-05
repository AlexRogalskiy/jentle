package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.Visitor;
import com.wildbeeslabs.jentle.collections.list.CList.CListNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

/**
 *
 * Custom list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CList<T> extends ACList<T, CListNode<T>> implements IList<T> {

    protected static class CListNode<T> extends ACList.ACListNode<T, CListNode<T>> {

        public CListNode() {
            this(null);
        }

        public CListNode(final T data) {
            this(data, null);
        }

        public CListNode(final T data, final CListNode<T> next) {
            this(data, next, CSort.DEFAULT_SORT_COMPARATOR);
        }

        public CListNode(final T data, final CListNode<T> next, final Comparator<? super T> cmp) {
            super(data, next, cmp);
        }
    }

    protected CListNode<T> first;
    protected CListNode<T> last;
    protected int size;

    public CList() {
        this(null, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CList(final CList<? extends T> source) {
        this(source, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CList(final CList<? extends T> source, final Comparator<? super T> cmp) {
        super(cmp);
        this.first = this.last = null;
        this.size = 0;
        this.addList(source);
    }

    public void addList(final CList<? extends T> source) {
        if (Objects.nonNull(source)) {
            for (CListNode<? extends T> current = source.first; current != null; current = current.next) {
                this.addLast(current.data);
            }
        }
    }

    public void addFirst(final T item) {
        final CListNode<T> temp = new CListNode<>(item, this.first);
        if (Objects.isNull(this.first)) {
            this.last = temp;
        }
        this.first = temp;
        this.size++;
    }

    @Override
    public void addLast(final T item) {
        final CListNode<T> temp = new CListNode<>(item, null);
        if (Objects.isNull(this.last)) {
            this.first = temp;
        } else {
            this.last.next = temp;
        }
        this.last = temp;
        this.size++;
    }

    public T removeFirst() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: CList (empty size=%d)", this.size()));
        }
        T removed = this.first.data;
        this.first = this.first.next;
        this.size--;
        return removed;
    }

    public T removeLast() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: CList (empty size=%d)", this.size()));
        }
        CListNode<T> previous = this.first, next = this.first;
        while (Objects.nonNull(next.next)) {
            previous = next;
            next = next.next;
        }
        if (Objects.isNull(previous)) {
            this.first = null;
        } else {
            previous.next = null;
        }
        this.last = previous;
        this.size--;
        return next.data;
    }

    @Override
    public boolean remove(final T item) throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: CList (empty size=%d)", this.size()));
        }
        CListNode<T> previous = this.first, next = this.first;
        boolean removed = false;
        while (Objects.nonNull(next)) {
            if (Objects.compare(item, next.data, this.cmp) == 0) {
                removed = true;
                this.size--;
                if (Objects.nonNull(previous)) {
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

    public void insertAt(final T item, int index) {
        this.checkRange(index);
        CListNode<T> previous = this.first, next = this.first;
        while (Objects.nonNull(next) && --index > 0) {
            previous = next;
            next = next.next;
        }
        CListNode<T> temp = new CListNode<>(item, next);
        if (Objects.isNull(next)) {
            this.last = temp;
        }
        if (Objects.isNull(previous)) {
            this.first = temp;
        } else {
            previous.next = temp;
        }
        this.size++;
    }

    public T getAt(int index) {
        this.checkRange(index);
        CListNode<T> current = this.first;
        while (Objects.nonNull(current) && --index > 0) {
            current = current.next;
        }
        if (Objects.nonNull(current)) {
            return current.data;
        }
        return null;
    }

    public T getFirst() {
        if (this.isEmpty()) {
            return null;
        }
        return this.first.data;
    }

    @Override
    public boolean contains(final T item) {
        for (Iterator<T> i = this.iterator(); i.hasNext();) {
            if (Objects.compare(i.next(), item, this.cmp) == 0) {
                return true;
            }
        }
        return false;
    }

    private void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CList (index=%d is out of bounds [1, %d])", index, this.size));
        }
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    public boolean isDistinct() {
        for (Iterator<T> i = this.iterator(); i.hasNext();) {
            T item = i.next();
            for (Iterator<T> j = this.iterator(); j.hasNext();) {
                if (Objects.compare(j.next(), item, this.cmp) == 0) {
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
        this.size = 0;
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
    public Iterator<T> iterator() {
        return new CListIterator<>(this);
    }

    protected static class CListIterator<T> implements Iterator<T> {

        private CListNode<? extends T> cursor = null;

        public CListIterator(final CList<? extends T> source) {
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

    public void deleteDuplicates() {
        this.deleteDuplicates(this.first);
    }

    public T getKthToLast2(int k) {
        return this.getKthToLast2(this.first, k);
    }

    public CListNode<T> partition(final T value) {
        return (CListNode<T>) this.partition(this.first, value);
    }

    @Override
    public String toString() {
        return String.format("CList {first: %s, last: %s, size: %d}", this.first, this.last, this.size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CList<T> other = (CList<T>) obj;
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
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.last);
        hash = 97 * hash + this.size;
        return hash;
    }
}
