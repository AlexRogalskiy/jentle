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
package com.wildbeeslabs.jentle.collections.interfaces;

import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;
import java.util.Optional;

/**
 *
 * Custom tree interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
public interface ITree<T, U extends ACBaseTreeNode<T, U>> extends IBaseTree<T, U> {

    /**
     * Creates new node as a left child of the current node
     *
     * @param node - current node
     * @param value - node value (optional)
     */
    void insertLeft(final U node, final Optional<? extends T> value);

    /**
     * Creates new node as a right child of the current node
     *
     * @param node - current node
     * @param value - node value (optional)
     */
    void insertRight(final U node, final Optional<? extends T> value);

    /**
     * Replaces current node with a new value
     *
     * @param node - current node
     * @param newValue - new node value
     * @return previous value of current node
     */
    T replaceElement(final U node, final T newValue);

    /**
     * Swaps elements of first and last nodes
     *
     * @param first - first node to replace
     * @param last - last node to be replaced with
     */
    void swapElements(final U first, final U last);
}
