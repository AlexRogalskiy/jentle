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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Custom tree3 implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public class CTree3<T> {

    /**
     * Default indent of formatted output
     */
    public static final int DEFAULT_FORMAT_INDENT = 2;

    private T head;
    private CTree3<T> parent = null;

    private final List<CTree3<T>> leafs = new ArrayList<>();
    private final Map<T, CTree3<T>> pathNodes = new HashMap<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public CTree3(final T head) {
        this.head = head;
        this.addPathNode(this);
    }

    public void addLeaf(final T root, final T leaf) {
        if (this.pathNodes.containsKey(root)) {
            this.pathNodes.get(root).addLeaf(leaf);
        } else {
            this.addLeaf(root).addLeaf(leaf);
        }
    }

    public CTree3<T> addLeaf(final T leaf) {
        final CTree3<T> tree = new CTree3<>(leaf);
        this.leafs.add(tree);
        tree.parent = this;
        tree.setPathNodes(this.getPathNodes());
        this.addPathNode(tree);
        return tree;
    }

    public CTree3<T> setAsParent(final T parentRoot) {
        final CTree3<T> tree = new CTree3<>(parentRoot);
        tree.leafs.add(this);
        this.parent = tree;
        tree.setPathNodes(this.getPathNodes());
        tree.addPathNode(this);
        tree.addPathNode(tree);
        return tree;
    }

    public T getHead() {
        return this.head;
    }

    public CTree3<T> getTree(final T element) {
        return this.pathNodes.get(element);
    }

    public CTree3<T> getParent() {
        return this.parent;
    }

    public Collection<T> getSuccessors(final T root) {
        final Collection<T> successors = new ArrayList<>();
        final CTree3<T> tree = this.getTree(root);
        if (Objects.nonNull(tree)) {
            tree.leafs.stream().forEach((leaf) -> {
                successors.add(leaf.head);
            });
        }
        return successors;
    }

    public Collection<CTree3<T>> getSubTrees() {
        return this.leafs;
    }

    public static <T> Collection<T> getSuccessors(final T of, final Collection<CTree3<T>> in) {
        for (final CTree3<T> tree : in) {
            if (tree.pathNodes.containsKey(of)) {
                return tree.getSuccessors(of);
            }
        }
        return Collections.EMPTY_LIST;
    }

    public String toFormatString(int increment) {
        final StringBuffer sb = new StringBuffer();
        final StringBuffer inc = new StringBuffer();
        for (int i = 0; i < increment; ++i) {
            inc.append(" ");
        }
        sb.append(inc.append(this.head));
        this.leafs.stream().forEach((child) -> {
            sb.append(System.lineSeparator()).append(child.toFormatString(increment + CTree3.DEFAULT_FORMAT_INDENT));
        });
        return sb.toString();
    }

    public Collection<CTree3<T>> getPathNodes() {
        return this.pathNodes.values();
    }

    public void setPathNodes(final Collection<CTree3<T>> pathNodes) {
        this.pathNodes.clear();
        if (Objects.nonNull(pathNodes)) {
            pathNodes.stream().forEach((node) -> {
                this.pathNodes.put(node.getHead(), node);
            });
        }
    }

    public void addPathNode(final CTree3<T> pathNode) {
        if (Objects.nonNull(pathNode)) {
            this.pathNodes.put(pathNode.getHead(), pathNode);
        }
    }

    public void removePathNode(final CTree3<T> pathNode) {
        if (Objects.nonNull(pathNode)) {
            this.pathNodes.remove(pathNode.getHead());
        }
    }
}
