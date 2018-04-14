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

import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract graph extended node implementation
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
public abstract class ACGraphNodeExtended<T, E extends ACGraphNodeExtended<T, E>> extends ACBaseGraphNode<T, E> {

    protected BigDecimal distance;
    protected final LinkedList<E> pathNodes = new LinkedList<>();
    protected final ConcurrentMap<E, BigDecimal> adjacents = new ConcurrentHashMap<>();

    public ACGraphNodeExtended() {
        this(null);
    }

    public ACGraphNodeExtended(final T data) {
        this(data, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACGraphNodeExtended(final T data, final Comparator<? super T> cmp) {
        this(data, BigDecimal.valueOf(Long.MAX_VALUE), CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACGraphNodeExtended(final T data, final BigDecimal distance, final Comparator<? super T> cmp) {
        super(data, cmp);
        this.distance = distance;
    }

    public Map<E, BigDecimal> getAdjacents() {
        return this.adjacents;
    }

    public void setAdjacents(final Map<E, BigDecimal> adjacents) {
        this.adjacents.clear();
        if (Objects.nonNull(adjacents)) {
            this.adjacents.putAll(adjacents);
        }
    }

    public void addAjacent(final E adjacent, final BigDecimal distance) {
        if (Objects.nonNull(adjacent)) {
            this.adjacents.put(adjacent, distance);
        }
    }

    public void removeAdjacent(final E adjacent) {
        if (Objects.nonNull(adjacent)) {
            this.adjacents.remove(adjacent);
        }
    }

    public Collection<E> getPathNodes() {
        return this.pathNodes;
    }

    public void setPathNodes(final Collection<E> pathNodes) {
        this.pathNodes.clear();
        if (Objects.nonNull(pathNodes)) {
            this.pathNodes.addAll(pathNodes);
        }
    }

    public void addPathNode(final E pathNode) {
        if (Objects.nonNull(pathNode)) {
            this.pathNodes.add(pathNode);
        }
    }

    public void removePathNode(final E pathNode) {
        if (Objects.nonNull(pathNode)) {
            this.pathNodes.remove(pathNode);
        }
    }
}
