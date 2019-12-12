package com.wildbeeslabs.jentle.collections.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class StackUtils {

    public static void sortStack(final Deque<Integer> stack) {
        final Deque<Integer> tempStack = new ArrayDeque<>();
        while (!stack.isEmpty()) {
            final Integer item = stack.pop();
            if (tempStack.isEmpty() || tempStack.peek() < item) {
                tempStack.push(item);
            } else {
                while (!tempStack.isEmpty() && tempStack.peek() > item) {
                    stack.push(tempStack.pop());
                }
                tempStack.push(item);
            }
        }
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
    }
}
