package com.wildbeeslabs.jentle.collections.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class QueueSingleStack<T> {

    private Deque<T> stack = new ArrayDeque<>();

    public void enqueue(T item) {
        stack.push(item);
    }

    public T dequeue() {
        T item = stack.pop();
        if (stack.isEmpty()) {
            return item;
        }
        T returnValue = dequeue();
        stack.push(item);
        return returnValue;
    }
}
