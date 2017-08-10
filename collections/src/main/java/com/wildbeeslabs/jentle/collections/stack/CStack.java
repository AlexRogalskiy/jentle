package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.interfaces.IStack;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;

import java.util.Collection;
import java.util.Objects;
import java.util.Queue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom stack implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CStack<T> implements IStack<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected static class CStackNode<T> {

        private final T data;
        private CStackNode<T> next;

        public CStackNode() {
            this(null);
        }

        public CStackNode(final T data) {
            this(data, null);
        }

        public CStackNode(final T data, final CStackNode<T> next) {
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString() {
            return String.format("CStackNode {data: %s, next: %s}", this.data, this.next);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null == obj || obj.getClass() != this.getClass()) {
                return false;
            }
            final CStackNode<T> other = (CStackNode<T>) obj;
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
            hash = 29 * hash + Objects.hashCode(this.data);
            hash = 29 * hash + Objects.hashCode(this.next);
            return hash;
        }
    }

    protected CStackNode<T> top;
    protected int size;

    public CStack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public T pop() throws EmptyStackException {
        if (this.isEmpty()) {
            throw new EmptyStackException(String.format("ERROR: CStack (empty size=%i)", this.size));
        }
        T removed = this.top.data;
        this.top = this.top.next;
        this.size--;
        return removed;
    }

    @Override
    public void push(T item) {
        CStackNode<T> temp = new CStackNode<>(item, top);
        this.size++;
        this.top = temp;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (this.isEmpty()) {
            throw new EmptyStackException(String.format("ERROR: CStack (empty size=%i)", this.size));
        }
        return top.data;
    }

    @Override
    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(T value) {
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
    public Queue<T> toLifoQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return String.format("CStack {top: %s, size: %i}", this.top, this.size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CStack<T> other = (CStack<T>) obj;
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.top, other.top)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.top);
        hash = 23 * hash + this.size;
        return hash;
    }
}
