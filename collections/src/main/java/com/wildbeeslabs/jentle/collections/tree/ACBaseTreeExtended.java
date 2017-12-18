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

import com.wildbeeslabs.jentle.collections.interfaces.IBaseTreeExtended;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;

import java.util.Comparator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract base extended tree implementation
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
public abstract class ACBaseTreeExtended<T, U extends ACTreeNodeExtended<T, U>> extends ACBaseTree<T, U> implements IBaseTreeExtended<T, U> {

    public ACBaseTreeExtended(final U root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    public U inOrderSuccessor(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.nonNull(root.getRight())) {
            return this.leftMostChild(root.getRight());
        } else {
            U current = root;
            U parent = current.getParent();
            while (Objects.nonNull(parent) && parent.getLeft() != current) {
                current = parent;
                parent = parent.getParent();
            }
            return parent;
        }
    }
}
