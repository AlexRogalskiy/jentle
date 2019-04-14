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
package com.wildbeeslabs.jentle.collections.interfaces.tree;

import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;

/**
 *
 * Custom binary tree interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
public interface IBinaryTree<T, U extends ACBaseTreeNode<T, U>> extends IBaseTree<T, U> {

    /**
     * Inserts new node with the current value based on tree order sequence
     *
     * @param value - node value
     */
    void insert(final T value);

    /**
     * Returns node with the current value
     *
     * @param value - node value
     * @return node with the current value
     */
    U find(final T value);
}
