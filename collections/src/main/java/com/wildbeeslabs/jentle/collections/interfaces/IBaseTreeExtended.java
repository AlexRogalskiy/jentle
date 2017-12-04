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

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;

/**
 *
 * Custom base tree extended interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
public interface IBaseTreeExtended<T, U extends ACTreeNodeExtended<T, U>> extends IBaseTree<T, U> {

    /**
     * Checks if current node is left child
     *
     * @param node - current node
     * @return true - if current node is left child node, false - otherwise
     */
    boolean isLeftChild(final U node);

    /**
     * Checks if current node is right child
     *
     * @param node - current node
     * @return true - if current node is right child node, false - otherwise
     */
    boolean isRightChild(final U node);

    /**
     * Checks if current node has parent node
     *
     * @param node - current node
     * @return true - if current node has parent node, false - otherwise
     */
    boolean hasParent(final U node);

    /**
     * Returns parent node of the current node
     *
     * @param node - current node
     * @return parent node
     */
    U getParent(final U node);

    /**
     * Returns depth of the current node
     *
     * @param node - current node
     * @return number of nodes in parent hierarchy
     */
    int depth(final U node);
}