package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.interfaces.IDeque;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Custom abstract deque implementation {@link AbstractQueue}
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACDeque<T> extends AbstractQueue<T> implements IDeque<T> {

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    @Override
    public boolean offerFirst(T t) {
        return false;
    }

    @Override
    public boolean offerLast(T t) {
        return false;
    }

    @Override
    public T pollFirst() {
        return null;
    }

    @Override
    public T pollLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public void push(T t) {

    }

    @Override
    public T pop() {
        return null;
    }
}
