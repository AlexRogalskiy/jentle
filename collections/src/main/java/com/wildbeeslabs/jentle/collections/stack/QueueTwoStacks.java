package com.wildbeeslabs.jentle.collections.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class QueueTwoStacks<T> {
    private Deque<T> enqueuStack = new ArrayDeque<>();
    private Deque<T> dequeueStack = new ArrayDeque<>();

    public void enqueue(final T value) {
        enqueuStack.push(value);
    }

    public T dequeue() {
        if (dequeueStack.isEmpty()) {
            while (!enqueuStack.isEmpty()) {
                dequeueStack.push(enqueuStack.pop());
            }
        }
        return dequeueStack.pop();
    }
}
