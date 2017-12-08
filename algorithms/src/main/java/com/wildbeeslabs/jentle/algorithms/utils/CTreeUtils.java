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
package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom tree utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CTreeUtils {

    private CTreeUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> U commonAncestor(final U firstNode, final U secondNode) {
        int delta = depth(firstNode) - depth(secondNode);
        U first = delta > 0 ? secondNode : firstNode;
        U second = delta > 0 ? firstNode : secondNode;
        second = goUpBy(second, Math.abs(delta));
        while (first != second && Objects.nonNull(first) && Objects.nonNull(second)) {
            first = first.getParent();
            second = second.getParent();
        }
        return Objects.isNull(first) || Objects.isNull(second) ? null : first;
    }

    private static <T, U extends ACTreeNodeExtended<T, U>> U goUpBy(U node, int delta) {
        while (delta > 0 && Objects.nonNull(node)) {
            node = node.getParent();
            delta--;
        }
        return node;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> int depth(U node) {
        int depth = 0;
        while (Objects.nonNull(node)) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    public static <T, U extends ACTreeNodeExtended<T, U>> U commonAncestor2(final U root, final U firstNode, final U secondNode) {
        if (!covers(root, firstNode) || !covers(root, secondNode)) {
            return null;
        } else if (covers(firstNode, secondNode)) {
            return firstNode;
        } else if (covers(secondNode, firstNode)) {
            return secondNode;
        }
        U sibling = getSibling(firstNode);
        U parent = firstNode.getParent();
        while (!covers(sibling, secondNode)) {
            sibling = getSibling(parent);
            parent = parent.getParent();
        }
        return parent;
    }

    private static <T, U extends ACTreeNode<T, U>> boolean covers(final U root, final U node) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (root == node) {
            return true;
        }
        return covers(root.getLeft(), node) || covers(root.getRight(), node);
    }

    private static <T, U extends ACTreeNodeExtended<T, U>> U getSibling(final U node) {
        if (Objects.isNull(node) || Objects.isNull(node.getParent())) {
            return null;
        }
        final U parent = node.getParent();
        return parent.getLeft() == node ? parent.getRight() : parent.getLeft();
    }

    public static <T, U extends ACTreeNode<T, U>> U commonAncestor3(final U root, final U firstNode, final U secondNode) {
        if (!covers(root, firstNode) || !covers(root, secondNode)) {
            return null;
        }
        return ancestorHelper(root, firstNode, secondNode);
    }

    private static <T, U extends ACTreeNode<T, U>> U ancestorHelper(final U root, final U firstNode, final U secondNode) {
        if (Objects.isNull(root) || root == firstNode || root == secondNode) {
            return root;
        }
        boolean firstIsOnLeft = covers(root.getLeft(), firstNode);
        boolean secondIsOnLeft = covers(root.getLeft(), secondNode);
        if (!Objects.equals(firstIsOnLeft, secondIsOnLeft)) {
            return root;
        }
        final U childSide = firstIsOnLeft ? root.getLeft() : root.getRight();
        return ancestorHelper(childSide, firstNode, secondNode);
    }

    public static <T, U extends ACTreeNode<T, U>> U commonAncestor4(final U root, final U firstNode, final U secondNode) {
        final Result<T, U> result = commonAncestorHelper(root, firstNode, secondNode);
        if (result.isAncestor) {
            return result.node;
        }
        return null;
    }

    private static <T, U extends ACTreeNode<T, U>> Result<T, U> commonAncestorHelper(final U root, final U firstNode, final U secondNode) {
        if (Objects.isNull(root)) {
            return new Result<>(null, false);
        }
        if (root == firstNode && root == secondNode) {
            return new Result(root, true);
        }
        final Result<T, U> temp = commonAncestorHelper(root.getLeft(), firstNode, secondNode);
        if (temp.isAncestor) {
            return temp;
        }
        final Result<T, U> temp2 = commonAncestorHelper(root.getRight(), firstNode, secondNode);
        if (temp2.isAncestor) {
            return temp2;
        }
        if (Objects.nonNull(temp.node) && Objects.nonNull(temp2.node)) {
            return new Result<>(root, true);
        } else if (root == firstNode || root == secondNode) {
            boolean isAncestor = Objects.nonNull(temp.node) || Objects.nonNull(temp2.node);
            return new Result<>(root, isAncestor);
        } else {
            return new Result<>(Objects.nonNull(temp.node) ? temp.node : temp2.node, false);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    private static class Result<T, U extends ACTreeNode<T, U>> {

        public U node;
        public boolean isAncestor;

        public Result(final U node, boolean isAncestor) {
            this.node = node;
            this.isAncestor = isAncestor;
        }
    }

    public static <T extends Serializable, U extends ACTreeNode<T, U>> List<LinkedList<T>> allSequences(final U node) {
        final List<LinkedList<T>> result = new ArrayList<>();
        if (Objects.isNull(node)) {
            result.add(new LinkedList<>());
            return result;
        }
        final LinkedList<T> prefix = new LinkedList<>();
        prefix.add(node.getData());
        final List<LinkedList<T>> leftSequence = allSequences(node.getLeft());
        final List<LinkedList<T>> rightSequence = allSequences(node.getRight());
        leftSequence.stream().forEach((left) -> {
            rightSequence.stream().map((right) -> {
                final List<LinkedList<T>> weaved = new ArrayList<>();
                weaveLists(left, right, weaved, prefix);
                return weaved;
            }).forEach((weaved) -> {
                result.addAll(weaved);
            });
        });
        return result;
    }

    private static <T extends Serializable> void weaveLists(final LinkedList<T> first, final LinkedList<T> second, final List<LinkedList<T>> results, final LinkedList<T> prefix) {
        if (first.isEmpty() || second.isEmpty()) {
            final List<T> result = CListUtils.cloneList(prefix);
            result.addAll(first);
            result.addAll(second);
            results.add((LinkedList<T>) result);
            return;
        }
        final T headFirst = first.removeFirst();
        prefix.addLast(headFirst);
        weaveLists(first, second, results, prefix);
        prefix.removeLast();
        first.addFirst(headFirst);

        final T headSecond = second.removeFirst();
        prefix.addLast(headSecond);
        weaveLists(first, second, results, prefix);
        prefix.removeLast();
        second.addFirst(headSecond);
    }

    public static <T, U extends ACTreeNode<T, U>> boolean containsTree(final U firstNode, final U secondNode) {
        final StringBuffer firstStr = new StringBuffer();
        final StringBuffer secondStr = new StringBuffer();
        getOrderString(firstNode, firstStr);
        getOrderString(secondNode, secondStr);
        return firstStr.indexOf(secondStr.toString()) != -1;
    }

    private static <T, U extends ACTreeNode<T, U>> void getOrderString(final U node, final StringBuffer sBuffer) {
        if (Objects.isNull(node)) {
            sBuffer.append("-");
            return;
        }
        sBuffer.append(node.getData()).append(" ");
        getOrderString(node.getLeft(), sBuffer);
        getOrderString(node.getRight(), sBuffer);

    }

    public static <T, U extends ACTreeNode<T, U>> boolean containsTree2(final U firstNode, final U secondNode) {
        if (Objects.isNull(secondNode)) {
            return true;
        }
        return subTree(firstNode, secondNode);
    }

    private static <T, U extends ACTreeNode<T, U>> boolean subTree(final U firstNode, final U secondNode) {
        if (Objects.isNull(firstNode)) {
            return false;
        } else if (Objects.equals(firstNode.getData(), secondNode.getData()) && matchTree(firstNode, secondNode)) {
            return true;
        }
        return subTree(firstNode.getLeft(), secondNode) || subTree(firstNode.getRight(), secondNode);
    }

    private static <T, U extends ACTreeNode<T, U>> boolean matchTree(final U firstNode, final U secondNode) {
        if (Objects.isNull(firstNode) && Objects.isNull(secondNode)) {
            return true;
        } else if (Objects.isNull(firstNode) || Objects.isNull(secondNode)) {
            return false;
        } else if (!Objects.equals(firstNode.getData(), secondNode.getData())) {
            return false;
        } else {
            return matchTree(firstNode.getLeft(), secondNode.getLeft()) && matchTree(firstNode.getRight(), secondNode.getRight());
        }

    }

    public static <T extends Double, U extends ACTreeNode<T, U>> Double countPathsWithSum(final U root, final T targetSum) {
        if (Objects.isNull(root)) {
            return (T) new Double(0);
        }
        final Double pathsFromRoot = countPathWithSumFromNode(root, targetSum, (T) new Double(0));
        Double pathsOnLeft = countPathsWithSum(root.getLeft(), targetSum);
        Double pathsOnRight = countPathsWithSum(root.getRight(), targetSum);
        return (pathsFromRoot + pathsOnLeft + pathsOnRight);
    }

    private static <T extends Double, U extends ACTreeNode<T, U>> Double countPathWithSumFromNode(final U node, final T targetSum, Double currentSum) {
        if (Objects.isNull(node)) {
            return (T) new Double(0);
        }
        currentSum += node.getData();
        double totalPaths = 0;
        if (Objects.equals(currentSum, targetSum)) {
            totalPaths++;
        }
        totalPaths += countPathWithSumFromNode(node.getLeft(), targetSum, currentSum);
        totalPaths += countPathWithSumFromNode(node.getRight(), targetSum, currentSum);
        return totalPaths;
    }
}
