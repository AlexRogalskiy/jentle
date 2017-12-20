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
package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 *
 * Custom tree algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CTree {

    private CTree() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, U extends ACTreeNode<T, U>> List<LinkedList<ACTreeNode<T, U>>> createLevelNodeList(final ACTreeNode<T, U> root) {
        final List<LinkedList<ACTreeNode<T, U>>> levelList = new ArrayList<>();
        createLevelNodeList(root, levelList, 0);
        return levelList;
    }

    private static <T, U extends ACTreeNode<T, U>> void createLevelNodeList(final ACTreeNode<T, U> root, final List<LinkedList<ACTreeNode<T, U>>> levelList, int level) {
        if (Objects.isNull(root)) {
            return;
        }
        LinkedList<ACTreeNode<T, U>> list = new LinkedList<>();
        if (levelList.size() == level) {
            levelList.add(list);
        } else {
            list = levelList.get(level);
        }
        list.add(root);
        createLevelNodeList(root.getLeft(), levelList, level + 1);
        createLevelNodeList(root.getRight(), levelList, level + 1);
    }

    public static <T, U extends ACTreeNode<T, U>> List<LinkedList<ACTreeNode<T, U>>> createLevelNodeList2(final ACTreeNode<T, U> root) {
        final List<LinkedList<ACTreeNode<T, U>>> levelList = new ArrayList<>();
        LinkedList<ACTreeNode<T, U>> current = new LinkedList<>();
        if (Objects.nonNull(root)) {
            current.add(root);
        }
        while (current.size() > 0) {
            levelList.add(current);
            LinkedList<ACTreeNode<T, U>> parents = current;
            current = new LinkedList<>();
            for (final ACTreeNode<T, U> parent : parents) {
                if (Objects.nonNull(parent.getLeft())) {
                    current.add(parent.getLeft());
                }
                if (Objects.nonNull(parent.getRight())) {
                    current.add(parent.getRight());
                }
            }
        }
        return levelList;
    }

    //breadth first search recursive algorithm
    public static <T, U extends ACTreeNode<T, U>> boolean isLeavesAtSameLevel(final U root) {
        if (Objects.isNull(root)) {
            return false;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int level = 0;
        boolean bLeafFound = false;
        int leafLevel = -1;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                level++;
            } else {
                if (Objects.nonNull(node.getLeft()) && Objects.nonNull(node.getRight())) {
                    if (!bLeafFound) {
                        bLeafFound = true;
                        leafLevel = level;
                    } else {
                        if (leafLevel != level) {
                            return false;
                        }
                    }
                }
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        return true;
    }

    public static <T, U extends ACTreeNode<T, U>> U ceilInBST(final U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return root;
        }
        if (Objects.compare(root.getData(), data, cmp) > 0) {
            U left = ceilInBST(root.getLeft(), data, cmp);
            if (Objects.isNull(left)) {
                return root;
            } else {
                return left;
            }
        }
        return ceilInBST(root.getRight(), data, cmp);
    }
}
