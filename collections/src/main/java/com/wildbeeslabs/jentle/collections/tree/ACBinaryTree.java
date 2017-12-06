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

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.IBinaryTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
import java.util.Arrays;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract binary tree implementation
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
public abstract class ACBinaryTree<T, U extends ACTreeNode<T, U>> extends ACBaseTree<T, U> implements IBinaryTree<T, U> {

    public ACBinaryTree(final U root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public void insert(final T value) {
        final U node = this.createTreeNode(Optional.ofNullable(value));
        if (this.isEmpty()) {
            this.root = node;
            this.size = 1;
        } else {
            this.insert(this.getRoot(), node);
        }
    }

    protected void insert(final U node, final U newNode) {
        Objects.requireNonNull(node);
        if (Objects.compare(newNode.getData(), node.getData(), this.cmp) <= 0) {
            if (Objects.isNull(node.getLeft())) {
                node.setLeft(newNode);
                this.size++;
            } else {
                this.insert(node.getLeft(), newNode);
            }
        } else {
            if (Objects.isNull(node.getRight())) {
                node.setRight(newNode);
                this.size++;
            } else {
                this.insert(node.getRight(), newNode);
            }
        }
    }

    @Override
    public U find(final T value) {
        return this.find(this.getRoot(), value);
    }

    protected U find(final U node, final T value) {
        if (Objects.isNull(node)) {
            return null;
        }
        if (Objects.compare(node.getData(), value, this.cmp) == 0) {
            return node;
        } else if (Objects.compare(node.getData(), value, cmp) <= 0) {
            return this.find(node.getLeft(), value);
        } else if (Objects.compare(node.getData(), value, cmp) > 0) {
            return this.find(node.getRight(), value);
        }
        return null;
    }
}
