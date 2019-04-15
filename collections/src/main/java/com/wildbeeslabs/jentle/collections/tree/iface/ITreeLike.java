/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.ICollection;
import com.wildbeeslabs.jentle.collections.list.node.ACNode;

import java.util.Objects;
import java.util.Optional;

/**
 * Custom tree {@link ICollection} interface declaration
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ITreeLike<T, U extends ACNode<T>> extends ICollection<T> {

    /**
     * Get the size of the tree.
     *
     * @return size of the tree.
     */
    int size();

    /**
     * Check if the list contains values.
     *
     * @return boolean (true - if the list is empty, false - otherwise)
     */
    default boolean isEmpty() {
        return (0 == this.size());
    }

    /**
     * Add new root node
     *
     * @param value - new root node
     */
    void setRoot(final Optional<? extends T> value);

    /**
     * Returns current root node
     *
     * @return - current root node
     */
    U getRoot();

    /**
     * Checks if current node is root
     *
     * @param node - current node
     * @return true - if current node is root, false - otherwise
     */
    default boolean isRoot(final U node) {
        Objects.requireNonNull(node);
        return (this.getRoot() == node);
    }
}
