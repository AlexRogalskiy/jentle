/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.collections.list.node;

import lombok.Getter;

import java.util.Objects;

@Getter
public final class LinkedNode<T> {

    /**
     * Default node {@code T} value
     */
    private final T value;
    /**
     * Default {@link LinkedNode} next instance
     */
    private LinkedNode<T> next;

    public LinkedNode(final T value, final LinkedNode<T> next) {
        this.value = value;
        this.next = next;
    }

    public void linkNext(final LinkedNode<T> next) {
        if (Objects.nonNull(this.next)) {
            throw new IllegalStateException();
        }
        this.next = next;
    }

    /**
     * Convenience method that can be used to check if a linked list
     * with given head node (which may be null to indicate empty list)
     * contains given value
     *
     * @param <ST>  Type argument that defines contents of the linked list parameter
     * @param node  Head node of the linked list
     * @param value Value to look for
     * @return True if linked list contains the value, false otherwise
     */
    public static <ST> boolean contains(final LinkedNode<ST> node, final ST value) {
        LinkedNode<ST> current = node;
        while (Objects.nonNull(current)) {
            if (current.getValue() == value) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
}
