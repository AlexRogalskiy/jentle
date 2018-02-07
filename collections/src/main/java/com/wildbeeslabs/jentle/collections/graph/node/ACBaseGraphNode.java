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
package com.wildbeeslabs.jentle.collections.graph.node;

import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract base graph node implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class ACBaseGraphNode<T, E extends ACBaseGraphNode<T, E>> extends ACNode<T> {

    protected ACBaseGraphNode.State state;
    protected final Comparator<? super T> comparator;

    public static enum State {

        UNVISITED, VISITED, VISITING;
    }

    public ACBaseGraphNode() {
        this(null);
    }

    public ACBaseGraphNode(final T data) {
        this(data, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACBaseGraphNode(final T data, final Comparator<? super T> comparator) {
        super(data);
        this.state = ACBaseGraphNode.State.UNVISITED;
        this.comparator = comparator;
    }
}
