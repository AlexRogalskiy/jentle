package com.wildbeeslabs.jentle.collections.stack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SetOfStacks<T> {
    private int maxSize;
    private List<Deque<T>> setOfStacks = new ArrayList<>();

    public SetOfStacks(final int maxSize) {
        this.maxSize = maxSize;
    }

    public T pop() {
        Deque<T> currentStack = setOfStacks.get(setOfStacks.size() - 1);
        T item = currentStack.pop();
        if (currentStack.isEmpty()) {
            setOfStacks.remove(setOfStacks.size() - 1);
        }
        return item;
    }

    public void push(final T item) {
        Deque<T> currentStack = setOfStacks.get(setOfStacks.size() - 1);
        if (currentStack.size() == maxSize) {
            final Deque<T> newStack = new ArrayDeque<>();
            newStack.push(item);
            setOfStacks.add(newStack);
        } else {
            currentStack.push(item);
        }
    }
}
