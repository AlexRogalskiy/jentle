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
package com.wildbeeslabs.jentle.collections.graph.node;

import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom abstract graph node implementation
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACGraphNode<T, E extends ACGraphNode<T, E>> extends ACBaseGraphNode<T, E> {

    protected final Collection<E> adjacents = new ArrayList<>();

    public ACGraphNode() {
        this(null);
    }

    public ACGraphNode(final T data) {
        this(data, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACGraphNode(final T data, final Comparator<? super T> cmp) {
        super(data, cmp);
    }

    public Collection<E> getAdjacents() {
        return this.adjacents;
    }

    public void setAdjacents(final Collection<E> adjacents) {
        this.adjacents.clear();
        Optional.ofNullable(adjacents).orElseGet(Collections::emptyList).forEach(this::addAdjacent);
    }

    public void addAdjacent(final E adjacent) {
        if (Objects.nonNull(adjacent)) {
            this.adjacents.add(adjacent);
        }
    }

    public void removeAdjacent(final E adjacent) {
        if (Objects.nonNull(adjacent)) {
            this.adjacents.remove(adjacent);
        }
    }

    public int getNumOfAdjacents() {
        return this.adjacents.size();
    }
}
