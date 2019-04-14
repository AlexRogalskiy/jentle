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
package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.collections.interfaces.tree.ITreeLike;
import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Optional;

/**
 * Custom abstract tree-like implementation
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public abstract class ACTreeLike<T, U extends ACNode<T>> implements ITreeLike<T, U> {

    protected U root;
    protected final Comparator<? super T> cmp;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ACTreeLike(final U root, final Comparator<? super T> cmp) {
        this.root = root;
        this.cmp = cmp;
    }

    @Override
    public void setRoot(final Optional<? extends T> value) {
        this.setRoot(this.createTreeNode(value));
    }

    protected void setRoot(final U node) {
        this.root = node;
    }

    @Override
    public U getRoot() {
//        if (this.isEmpty()) {
//            return null;
//        }
        return this.root;
    }

    protected abstract U createTreeNode(final Optional<? extends T> value);
}
