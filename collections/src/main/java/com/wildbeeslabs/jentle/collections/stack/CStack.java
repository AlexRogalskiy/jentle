package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.interfaces.IStack;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;

import java.util.Collection;
import java.util.Queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CStack<T> implements IStack<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class CStackNode<T> {

        private final T data;
        private CStackNode<T> next;

        public CStackNode() {
            this(null);
        }

        public CStackNode(final T data) {
            this(data, null);
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
            throw new EmptyStackException(String.format("ERROR: CStack (empty size=%d)", this.size));
        }
        T removed = this.top.data;
        this.top = this.top.next;
        this.size--;
        return removed;
    }

    @Override
    public void push(final T item) {
        CStackNode<T> temp = new CStackNode<>(item, this.top);
        this.size++;
        this.top = temp;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (this.isEmpty()) {
            throw new EmptyStackException(String.format("ERROR: CStack (empty size=%d)", this.size));
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
    public boolean remove(final T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(final T value) {
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
}
