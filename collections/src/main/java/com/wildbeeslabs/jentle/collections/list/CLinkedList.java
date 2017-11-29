package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.list.CLinkedList.CLinkedListNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom linked list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CLinkedList<T> extends ACList<T, CLinkedListNode<T>> implements IList<T> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    protected static class CLinkedListNode<T> extends ACList.ACListNode<T, CLinkedListNode<T>> {

        protected CLinkedListNode<T> previous;

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
            super(data, next, cmp);
            this.previous = previous;
        }
    }

    protected CLinkedListNode<T> first;
    protected CLinkedListNode<T> last;
    protected int size;

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
        super(cmp);
        this.first = this.last = null;
        this.size = 0;
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

    public void deleteDuplicates() {
        this.deleteDuplicates(this.first);
    }

    public T getKthToLast2(int k) {
        return this.getKthToLast2(this.first, k);
    }

    public CLinkedListNode<T> partition(final T value) {
        return (CLinkedListNode<T>) this.partition(this.first, value);
    }

    public boolean isPalindrome() {
        return this.isPalindrome(this.first);
    }
}
