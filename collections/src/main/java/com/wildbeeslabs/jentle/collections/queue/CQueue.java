package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.interfaces.IQueue;
import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;

import java.util.Collection;
import java.util.Objects;
import java.util.Queue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom Queue implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
public class CQueue<T> implements IQueue<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class CQueueNode<T> {

        private final T data;
        private CQueueNode<T> next;

        public CQueueNode() {
            this(null);
        }

        public CQueueNode(final T data) {
            this(data, null);
        }
    }

    protected CQueueNode<T> first;
    protected CQueueNode<T> last;
    protected int size;

    public CQueue() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public void enqueue(final T item) {
        CQueueNode<T> temp = new CQueueNode<>(item);
        if (Objects.nonNull(last)) {
            this.last.next = temp;
        }
        this.last = temp;
        if (Objects.isNull(first)) {
            this.first = this.last;
        }
        this.size++;
    }

    public T dequeue() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: CQueue (empty size=%i)", this.size));
        }
        T data = this.first.data;
        this.first = this.first.next;
        if (Objects.isNull(first)) {
            this.last = null;
        }
        this.size--;
        return data;
    }

    @Override
    public T peek() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: CQueue (empty size=%i)", this.size));
        }
        return this.first.data;
    }

    @Override
    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public boolean offer(final T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T poll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(final T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
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
    public Queue<T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
