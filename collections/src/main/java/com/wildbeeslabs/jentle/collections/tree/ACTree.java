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
package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.collections.interfaces.ITree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class ACTree<T, U extends ACTreeNode<T, U>> extends ACBaseTree<T, U> implements ITree<T, U> {

    public ACTree(final U root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public void insertLeft(final U node, final Optional<? extends T> value) {
        Objects.requireNonNull(node);
        if (Objects.nonNull(node.getLeft())) {
            LOGGER.debug(String.format("Node (%s) has already left child", node));
            return;
        }
        final U newNode = this.createTreeNode(value);
        node.setLeft(newNode);
        this.size++;
    }

    @Override
    public void insertRight(final U node, final Optional<? extends T> value) {
        Objects.requireNonNull(node);
        if (Objects.nonNull(node.getRight())) {
            LOGGER.debug(String.format("Node (%s) has already right child", node));
            return;
        }
        final U newNode = this.createTreeNode(value);
        node.setRight(newNode);
        this.size++;
    }

    @Override
    public T replaceElement(final U node, final T newValue) {
        Objects.requireNonNull(node);
        final T value = node.getData();
        node.setData(newValue);
        return value;
    }

    @Override
    public void swapElements(final U first, final U last) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(last);
        final T value = this.replaceElement(first, last.getData());
        last.setData(value);
    }
}