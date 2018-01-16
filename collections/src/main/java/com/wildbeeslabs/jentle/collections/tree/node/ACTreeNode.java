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
package com.wildbeeslabs.jentle.collections.tree.node;

import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract tree node implementation
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
public abstract class ACTreeNode<T, U extends ACTreeNode<T, U>> extends ACNode<T> {

    protected U parent;
    protected ACTreeNode.State state;
    protected Comparator<? super T> comparator;
    protected final List<U> childs = new ArrayList<>();

    public static enum State {

        UNVISITED, VISITED, VISITING;
    }

    public ACTreeNode() {
        this(null);
    }

    public ACTreeNode(final T data) {
        this(data, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACTreeNode(final T data, final Comparator<? super T> comparator) {
        this(data, null, comparator);
    }

    public ACTreeNode(final T data, final U parent, final Comparator<? super T> comparator) {
        super(data);
        this.parent = parent;
        this.state = ACTreeNode.State.UNVISITED;
        this.comparator = comparator;
    }

    public void copy(final U node) {
        Objects.requireNonNull(node);
        this.comparator = node.getComparator();
        this.parent = node.getParent();
        this.state = node.getState();
        this.setChilds(node.getChilds());
    }

    public Collection<U> getChilds() {
        return this.childs;
    }

    public void setChilds(final Collection<U> childs) {
        this.childs.clear();
        if (Objects.nonNull(childs)) {
            this.childs.addAll(childs);
        }
    }

    public void addChild(final U child) {
        if (Objects.nonNull(child)) {
            this.childs.add(child);
        }
    }

    public void removeChild(final U child) {
        if (Objects.nonNull(child)) {
            this.childs.remove(child);
        }
    }

    public U getRandomChild() {
        int selectRandom = (int) (Math.random() * ((this.childs.size() - 1) + 1));
        return this.childs.get(selectRandom);
    }
}
