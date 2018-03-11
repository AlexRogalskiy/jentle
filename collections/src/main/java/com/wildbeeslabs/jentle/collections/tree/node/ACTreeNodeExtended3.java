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
package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract binary tree extended node 3 implementation
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
public abstract class ACTreeNodeExtended3<T, U extends ACTreeNodeExtended3<T, U>> extends ACBaseTreeNode<T, U> {

    protected ACTreeNodeExtended3.Color color;
    protected U node;

    public static enum Color {

        RED, BLACK;
    }

    public ACTreeNodeExtended3() {
        this(null);
    }

    public ACTreeNodeExtended3(final T data) {
        this(data, null, null, null);
    }

    public ACTreeNodeExtended3(final T data, final U node, final U left, final U right) {
        this(data, node, left, right, ACTreeNodeExtended3.Color.BLACK);
    }

    public ACTreeNodeExtended3(final T data, final U node, final U left, final U right, final ACTreeNodeExtended3.Color color) {
        super(data, left, right);
        this.node = node;
        this.color = color;
    }
}
