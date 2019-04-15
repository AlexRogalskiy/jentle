package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.exception.EmptyDequeException;
import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.queue.impl.CDeque;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * Custom stack implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CDequeStack<T> extends Stack<T> {

    private CDeque<T> deque;

    public CDequeStack() {
        this.deque = new CDeque<>();
    }

    @Override
    public int size() {
        return this.deque.size();
    }

    @Override
    public boolean isEmpty() {
        return this.deque.isEmpty();
    }

    @Override
    public T push(final T value) {
        this.deque.addLast(value);
        return value;
    }

    @Override
    public T peek() throws EmptyStackException {
        try {
            return this.deque.getLast();
        } catch (EmptyDequeException ex) {
            throw new EmptyStackException("ERROR: stack is empty");
        }
    }

    @Override
    public T pop() throws EmptyStackException {
        try {
            return this.deque.removeLast();
        } catch (EmptyDequeException ex) {
            throw new EmptyStackException("ERROR: stack is empty");
        }
    }
}
