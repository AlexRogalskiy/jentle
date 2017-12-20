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
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Function;

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
            }
            return left;
        }
        return ceilInBST(root.getRight(), data, cmp);
    }

    //depth first search recursive algorithm
    public static <T, U extends ACTreeNode<T, U>> U floor(final U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return root;
        }
        if (Objects.compare(root.getData(), data, cmp) > 0) {
            return floor(root.getLeft(), data, cmp);
        }
        U right = floor(root.getRight(), data, cmp);
        if (Objects.isNull(right)) {
            return root;
        }
        return right;
    }

    public static <T, U extends ACTreeNodeExtended2<T, U>> void nextSiblingPointer(final U node) {
        if (Objects.isNull(node)) {
            return;
        }
        if (Objects.nonNull(node.getLeft())) {
            node.getLeft().setNextSibling(node.getRight());
        }
        if (Objects.nonNull(node.getRight())) {
            if (Objects.nonNull(node.getNextSibling())) {
                node.getRight().setNextSibling(node.getNextSibling().getLeft());
            } else {
                node.getRight().setNextSibling(null);
            }
        }
        nextSiblingPointer(node.getLeft());
        nextSiblingPointer(node.getRight());
    }

    public static <T, U extends ACTreeNodeExtended2<T, U>> List<T> nextSiblingTraversal(U root) {
        final List<T> result = new ArrayList<>();
        U node = null;
        int level = 0;
        while (Objects.nonNull(root)) {
            node = root;
            level++;
            while (Objects.nonNull(node)) {
                result.add(node.getData());
                node = node.getNextSibling();
            }
            root = root.getLeft();
        }
        return result;
    }

    public static <T, U extends ACTreeNode<T, U>> U kSmallestElement(final U root, int k) {
        return kSmallestElement(root, k, new int[]{0});
    }

    private static <T, U extends ACTreeNode<T, U>> U kSmallestElement(final U root, int k, int[] iElement) {
        if (Objects.isNull(root)) {
            return null;
        }
        U left = kSmallestElement(root.getLeft(), k, iElement);
        if (Objects.nonNull(left)) {
            return left;
        }
        iElement[0]++;
        if (iElement[0] == k) {
            return root;
        }
        return kSmallestElement(root.getRight(), k, iElement);
    }

    public static <T, U extends ACTreeNode<T, U>> boolean quasiIsomorphicBinaryTree(final U node1, final U node2) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (quasiIsomorphicBinaryTree(node1.getLeft(), node2.getLeft()) && quasiIsomorphicBinaryTree(node1.getRight(), node2.getRight())) {
            return true;
        }
        if (quasiIsomorphicBinaryTree(node1.getLeft(), node2.getRight()) && quasiIsomorphicBinaryTree(node1.getRight(), node2.getLeft())) {
            return true;
        }
        return false;
    }

    public static <T, U extends ACTreeNode<T, U>> boolean isIsomorphicBinaryTree(final U node1, final U node2) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (!isIsomorphicBinaryTree(node1.getLeft(), node2.getLeft())) {
            return false;
        }

        if (!isIsomorphicBinaryTree(node1.getRight(), node2.getRight())) {
            return false;
        }
        return true;
    }

    public static <T, U extends ACTreeNode<T, U>> Integer levelOfNodeInBTree(final U root, final U inputNode, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int level = 0;
        boolean found = false;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                level++;
            } else {
                if (Objects.compare(inputNode.getData(), node.getData(), cmp) == 0) {
                    found = true;
                    break;
                }
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        if (found) {
            return level;
        }
        return null;
    }

    public static <T, U extends ACTreeNode<T, U>> int[] maxWidthOfBTree(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int maxWidth = 0;
        int level = 0;
        int localLevel = 0;
        int localWidth = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                if (localWidth > maxWidth) {
                    maxWidth = localWidth;
                    level = localLevel;
                }
                localWidth = 0;
                localLevel++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
                localWidth++;
            }
        }
        return new int[]{maxWidth, level};
    }

    private static <T, U extends ACTreeNode<T, U>> void getNodesKDistFromLeaf(final U root, int distance, int index, final List<T> data, final List<Boolean> isVisitedBefore) {
        if (Objects.isNull(root)) {
            return;
        }
        data.set(index, root.getData());
        isVisitedBefore.set(index, Boolean.FALSE);
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight()) && (index - distance >= 0) && !isVisitedBefore.get(index - distance)) {
            isVisitedBefore.set(index - distance, Boolean.TRUE);
        } else if (distance > 0) {
            getNodesKDistFromLeaf(root.getLeft(), distance, index + 1, data, isVisitedBefore);
            getNodesKDistFromLeaf(root.getRight(), distance, index + 1, data, isVisitedBefore);
        }
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> getNodesKDistFromLeaf(final U root, int distance) {
        final List<T> result = new ArrayList<>();
        final List<Boolean> visit = new ArrayList<>();
        getNodesKDistFromLeaf(root, distance, 0, result, visit);
        return result;
    }

    private static <T, U extends ACTreeNode<T, U>> void getNodesKDistFromRoot(final U root, int distance, final List<T> data) {
        if (Objects.isNull(root)) {
            return;
        }
        if (distance == 0) {
            data.add(root.getData());
        } else if (distance > 0) {
            getNodesKDistFromRoot(root.getLeft(), distance - 1, data);
            getNodesKDistFromRoot(root.getRight(), distance - 1, data);
        }
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> getNodesKDistFromRoot(final U root, int distance) {
        final List<T> result = new ArrayList<>();
        getNodesKDistFromRoot(root, distance, result);
        return result;
    }

    private static <T, U extends ACTreeNode<T, U>> void getNodesByRange(final U root, final T data1, final T data2, final List<T> result, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return;
        }
        if (Objects.compare(root.getData(), data1, cmp) >= 0 && Objects.compare(root.getData(), data2, cmp) <= 0) {
            result.add(root.getData());
        }
        if (Objects.compare(root.getData(), data1, cmp) > 0) {
            getNodesByRange(root.getLeft(), data1, data2, result, cmp);
        }
        if (Objects.compare(root.getData(), data2, cmp) < 0) {
            getNodesByRange(root.getRight(), data1, data2, result, cmp);
        }
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> getNodesByRange(final U root, final T data1, final T data2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        getNodesByRange(root, data1, data2, result, cmp);
        return result;
    }

    private static <T, U extends ACTreeNode<T, U>> U min(final U node) {
        if (Objects.isNull(node.getLeft())) {
            return node;
        }
        return min(node.getLeft());
    }

    private static <T, U extends ACTreeNode<T, U>> U max(final U root) {
        if (Objects.isNull(root.getRight())) {
            return root;
        }
        return max(root.getRight());
    }

    public static <T, U extends ACTreeNode<T, U>> U deleteNodeInBST(U root, final T data, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(data, root.getData(), cmp) < 0) {
            root.setLeft(deleteNodeInBST(root.getLeft(), data, cmp));
        } else if (Objects.compare(data, root.getData(), cmp) > 0) {
            root.setRight(deleteNodeInBST(root.getRight(), data, cmp));
        } else {
            if (Objects.nonNull(root.getLeft()) && Objects.nonNull(root.getRight())) {
                T minInRightSubTree = min(root.getRight()).getData();
                root.setData(minInRightSubTree);
                root.setRight(deleteNodeInBST(root.getRight(), minInRightSubTree, cmp));
            } else {
                if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
                    root = null;
                } else {
                    U deleteNode = root;
                    root = (Objects.nonNull(root.getLeft())) ? root.getLeft() : root.getRight();
                    deleteNode = null;
                }
            }
        }
        return root;
    }

    public static <T, U extends ACTreeNode<T, U>> U predecessor(U root, final U node, final Comparator<? super T> cmp) {
        if (Objects.nonNull(node.getLeft())) {
            return max(node.getLeft());
        }
        U predecessor = null;
        while (Objects.nonNull(root)) {
            if (Objects.compare(node.getData(), root.getData(), cmp) == 0) {
                break;
            } else if (Objects.compare(node.getData(), root.getData(), cmp) < 0) {
                root = root.getLeft();
            } else if (Objects.compare(node.getData(), root.getData(), cmp) > 0) {
                predecessor = root;
                root = root.getRight();
            }
        }
        return predecessor;
    }

    public static <T, U extends ACTreeNode<T, U>> U successor(U root, final U node, final Comparator<? super T> cmp) {
        if (Objects.nonNull(node.getRight())) {
            return min(node.getRight());
        }
        U successor = null;
        while (Objects.nonNull(root)) {
            if (Objects.compare(node.getData(), root.getData(), cmp) == 0) {
                break;
            } else if (Objects.compare(node.getData(), root.getData(), cmp) < 0) {
                successor = root;
                root = root.getLeft();
            } else if (Objects.compare(node.getData(), root.getData(), cmp) > 0) {
                root = root.getRight();
            }
        }
        return successor;
    }

    public static <T, U extends ACTreeNode<T, U>> U leastCommonAncestor(final U root, final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.compare(root.getData(), node1.getData(), cmp) > 0 && Objects.compare(root.getData(), node2.getData(), cmp) > 0) {
            return leastCommonAncestor(root.getLeft(), node1, node2, cmp);
        }
        if (Objects.compare(root.getData(), node1.getData(), cmp) < 0 && Objects.compare(root.getData(), node2.getData(), cmp) < 0) {
            return leastCommonAncestor(root.getRight(), node1, node2, cmp);
        }
        return root;
    }

    public static <T, U extends ACTreeNode<T, U>> boolean isBST(final U node, final T min, final T max, final Comparator<? super T> cmp) {
        if (Objects.isNull(node)) {
            return true;
        }
        if (Objects.compare(node.getData(), min, cmp) < 0 || Objects.compare(node.getData(), max, cmp) > 0) {
            return false;
        }
        boolean isLeft = isBST(node.getLeft(), min, node.getData(), cmp);
        if (!isLeft) {
            return isLeft;
        }
        boolean isRight = isBST(node.getRight(), node.getData(), max, cmp);
        if (!isRight) {
            return isRight;
        }
        return true;
    }

    public static <T, U extends ACTreeNode<T, U>> U findNodeInBST(final U node, final T value, final Comparator<? super T> cmp) {
        if (Objects.isNull(node)) {
            return null;
        }
        if (Objects.compare(node.getData(), value, cmp) == 0) {
            return node;
        } else if (Objects.compare(value, node.getData(), cmp) < 0) {
            return findNodeInBST(node.getLeft(), value, cmp);
        }
        return findNodeInBST(node.getRight(), value, cmp);
    }

    public static <T, U extends ACTreeNode<T, U>> boolean isIdentical(final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (Objects.compare(node1.getData(), node2.getData(), cmp) != 0) {
            return false;
        }
        if (!isIdentical(node1.getLeft(), node2.getLeft(), cmp)) {
            return false;
        }
        if (!isIdentical(node1.getRight(), node2.getRight(), cmp)) {
            return false;
        }
        return true;
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> List<T> sumInRoot2LeafPath(final U root, int sum) {
        final List<T> path = new ArrayList<>();
        boolean sumExist = sumInRoot2LeafPath(root, path, sum);
        if (sumExist) {
            return path;
        }
        return null;
    }

    private static <T extends Number, U extends ACTreeNode<T, U>> boolean sumInRoot2LeafPath(final U root, final List<T> path, int sum) {
        if (Objects.isNull(root)) {
            return false;
        }
        path.add(root.getData());
        if (Objects.nonNull(root.getData())) {
            sum -= root.getData().intValue();
        }
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            if (sum == 0) {
                return true;
            }
            return false;
        }
        return sumInRoot2LeafPath(root.getLeft(), path, sum) || sumInRoot2LeafPath(root.getRight(), path, sum);
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> List<T> maxSumPath(final U root) {
        final List<T> path = new ArrayList<>();
        maxSumPathRoot2Leaf(root, path, 0, Integer.MIN_VALUE);
        return path;
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> int maxSumPathRoot2Leaf(final U root, final List<T> path, int sum, int maxSum) {
        if (Objects.isNull(root)) {
            return Integer.MIN_VALUE;
        }
        path.add(root.getData());
        if (Objects.nonNull(root.getData())) {
            sum += root.getData().intValue();
        }
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            if (sum > maxSum) {
                maxSum = sum;
            }
            return maxSum;
        }
        return Math.max(maxSumPathRoot2Leaf(root.getLeft(), path, sum, maxSum), maxSumPathRoot2Leaf(root.getRight(), path, sum, maxSum));
    }

    private static <T extends Number, U extends ACTreeNode<T, U>> void verticalOrder(final U root, int distance, final Map<Integer, Integer> map) {
        if (Objects.isNull(root)) {
            return;
        }
        int existingValue = 0;
        if (map.containsKey(distance)) {
            existingValue = map.get(distance);
        }
        map.put(distance, root.getData().intValue() + existingValue);
        verticalOrder(root.getLeft(), distance - 1, map);
        verticalOrder(root.getRight(), distance + 1, map);
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> Map<Integer, Integer> verticalOrderSumOfBTree(final U root) {
        final Map<Integer, Integer> map = new HashMap<>();
        verticalOrder(root, 0, map);
        return map;
    }

    private static <T, U extends ACTreeNode<T, U>> void verticalOrder2(final U root, int distance, final Map<Integer, List<T>> map) {
        if (Objects.isNull(root)) {
            return;
        }
        List<T> list = null;
        if (map.containsKey(distance)) {
            list = map.get(distance);
        } else {
            list = new ArrayList<>();
        }
        list.add(root.getData());
        map.put(distance, list);
        verticalOrder2(root.getLeft(), distance - 1, map);
        verticalOrder2(root.getRight(), distance + 1, map);
    }

    public static <T, U extends ACTreeNode<T, U>> Map<Integer, List<T>> verticalOrderOfBTree(final U root) {
        final Map<Integer, List<T>> map = new HashMap<>();
        verticalOrder2(root, 0, map);
        return map;
    }

    private <T, U extends ACTreeNode<T, U>> int diameterOfBTree(final U root, int[] diameter) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int left = diameterOfBTree(root.getLeft(), diameter);
        int right = diameterOfBTree(root.getRight(), diameter);
        diameter[0] = Math.max(diameter[0], left + right + 1);
        return Math.max(left, right) + 1;
    }

    public <T, U extends ACTreeNode<T, U>> int getDiameter(final U root) {
        int[] diameter = new int[]{0};
        diameterOfBTree(root, diameter);
        return diameter[0];
    }

    public static <T, U extends ACTreeNode<T, U>> int nodesInBTree(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int nLeftSubtree = nodesInBTree(root.getLeft());
        int nRightSubtree = nodesInBTree(root.getRight());
        return nLeftSubtree + nRightSubtree + 1;
    }

    public static <T, U extends ACTreeNode<T, U>> T minMaxElement(final U root, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        T data = root.getData();
        T left = minMaxElement(root.getLeft(), cmp);
        T right = minMaxElement(root.getRight(), cmp);
        return Collections.max(Arrays.asList(left, right, data), cmp);
    }

    public static <T, U extends ACTreeNode<T, U>> int heightOfTree(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int left = heightOfTree(root.getLeft());
        int right = heightOfTree(root.getRight());
        return Math.max(left, right) + 1;
    }

    public static <T, U extends ACTreeNode<T, U>> boolean isMirrorTree(final U node1, final U node2, final Comparator<? super T> cmp) {
        if (Objects.isNull(node1) && Objects.isNull(node2)) {
            return true;
        }
        if (Objects.isNull(node1) || Objects.isNull(node2)) {
            return false;
        }
        if (Objects.compare(node1.getData(), node2.getData(), cmp) != 0) {
            return false;
        }
        if (!isMirrorTree(node1.getLeft(), node2.getRight(), cmp)) {
            return false;
        }

        if (!isMirrorTree(node1.getRight(), node2.getLeft(), cmp)) {
            return false;
        }
        return true;
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> spiralTraversal(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Stack<U> stackOne = new Stack<>();
        final Stack<U> stackTwo = new Stack<>();
        stackOne.push(root);
        while (!stackOne.isEmpty() || !stackTwo.isEmpty()) {
            while (!stackOne.isEmpty()) {
                U node = stackOne.pop();
                result.add(node.getData());
                if (Objects.nonNull(node.getRight())) {
                    stackTwo.push(node.getRight());
                }
                if (Objects.nonNull(node.getLeft())) {
                    stackTwo.push(node.getLeft());
                }
            }
            while (!stackTwo.isEmpty()) {
                U node = stackTwo.pop();
                result.add(node.getData());
                if (Objects.nonNull(node.getLeft())) {
                    stackOne.push(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    stackOne.push(node.getRight());
                }
            }
        }
        return result;
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> reverseLevelOrder(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        final Stack<U> stack = new Stack<>();
        while (!queue.isEmpty()) {
            U node = queue.poll();
            stack.push(node);
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop().getData());
        }
        return result;
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> getAncestorsOfNode(final U root, final T data, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        getAncestorsOfNode(root, data, result, cmp);
        return result;
    }

    private static <T, U extends ACTreeNode<T, U>> boolean getAncestorsOfNode(final U root, final T data, final List<T> result, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return false;
        }
        if (Objects.compare(root.getData(), data, cmp) == 0) {
            return true;
        }
        boolean bFoundOnLeft = getAncestorsOfNode(root.getLeft(), data, result, cmp);
        if (bFoundOnLeft) {
            result.add(root.getData());
            return bFoundOnLeft;
        }
        boolean bFoundOnRight = getAncestorsOfNode(root.getRight(), data, result, cmp);
        if (bFoundOnRight) {
            result.add(root.getData());
            return bFoundOnRight;
        }
        return false;
    }

    public static <T, U extends ACTreeNode<T, U>> List<T> root2LeafPath(final U root, int[] path) {
        final List<T> result = new ArrayList<>();
        root2LeafPath(root, result);
        return result;
    }

    private static <T, U extends ACTreeNode<T, U>> void root2LeafPath(final U root, final List<T> path) {
        if (Objects.isNull(root)) {
            return;
        }
        path.add(root.getData());
        if (Objects.isNull(root.getLeft()) && Objects.isNull(root.getRight())) {
            return;
        }
        root2LeafPath(root.getLeft(), path);
        root2LeafPath(root.getRight(), path);
    }

    public static <T, U extends ACTreeNode<T, U>> void mirrorTree(final U root) {
        if (Objects.isNull(root)) {
            return;
        }
        mirrorTree(root.getLeft());
        mirrorTree(root.getRight());
        U swapNode = root.getLeft();
        root.setLeft(root.getRight());
        root.setRight(swapNode);
    }

    public static <T, U extends ACTreeNode<T, U>> int countNodes(final U root) {
        return countNodes(root, (node) -> Boolean.TRUE);
    }

    public static <T, U extends ACTreeNode<T, U>> int countFullNodes(final U root) {
        return countNodes(root, (node) -> Objects.nonNull(node.getLeft()) && Objects.nonNull(node.getRight()));
    }

    public static <T, U extends ACTreeNode<T, U>> int countNonLeafNodes(final U root) {
        return countNodes(root, (node) -> Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    public static <T, U extends ACTreeNode<T, U>> int countNonLeafOneChild(final U root) {
        final Function<U, Boolean> function = (node) -> (Objects.nonNull(node.getLeft()) && Objects.isNull(node.getRight())) || (Objects.isNull(node.getLeft()) && Objects.nonNull(node.getRight()));
        return countNodes(root, function);
    }

    public static <T, U extends ACTreeNode<T, U>> int countLeaves(final U root) {
        return countNodes(root, (node) -> Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    private static <T, U extends ACTreeNode<T, U>> int countNodes(final U root, final Function<U, Boolean> function) {
        if (Objects.isNull(root)) {
            return 0;
        }
        int nNodes = 0;
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (function.apply(node)) {
                nNodes++;
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return nNodes;
    }

    public static <T, U extends ACTreeNode<T, U>> T minMaxElement2(final U root, final Comparator<? super T> cmp) {
        if (Objects.isNull(root)) {
            return null;
        }
        T max = root.getData();
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.compare(max, node.getData(), cmp) < 0) {
                max = node.getData();
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return max;
    }

    public static <T, U extends ACTreeNode<T, U>> U lowestCommonAncestor(final U root, final U node1, final U node2) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (root == node1 || root == node2) {
            return root;
        }
        U left = lowestCommonAncestor(root.getLeft(), node1, node2);
        U right = lowestCommonAncestor(root.getRight(), node1, node2);
        if (Objects.nonNull(left) && Objects.nonNull(right)) {
            return root;
        }
        if (Objects.nonNull(left)) {
            return left;
        }
        return right;
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> int[] maxSumLevel(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int maxSum = 0;
        int level = 0;
        int localLevel = 0;
        int localSum = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                if (localSum > maxSum) {
                    maxSum = localSum;
                    level = localLevel;
                }
                localSum = 0;
                localLevel++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
                if (Objects.nonNull(node.getData())) {
                    localSum += node.getData().intValue();
                }
            }
        }
        return new int[]{maxSum, level};
    }

    public static <T extends Number, U extends ACTreeNode<T, U>> Integer sumOfNodes(final U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        int totalSum = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.nonNull(node.getData())) {
                totalSum += node.getData().intValue();
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return totalSum;
    }

    public static <T, U extends ACTreeNode<T, U>> int heightOfTree2(final U root) {
        if (Objects.isNull(root)) {
            return 0;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        int height = 0;
        while (!queue.isEmpty()) {
            U node = queue.poll();
            if (Objects.isNull(node)) {
                if (!queue.isEmpty()) {
                    queue.offer(null);
                }
                height++;
            } else {
                if (Objects.nonNull(node.getLeft())) {
                    queue.offer(node.getLeft());
                }
                if (Objects.nonNull(node.getRight())) {
                    queue.offer(node.getRight());
                }
            }
        }
        return height;
    }

    //level order traversal
    public static <T, U extends ACTreeNode<T, U>> List<T> traverseBinaryTree(final U root) {
        final List<T> result = new ArrayList<>();
        if (Objects.isNull(root)) {
            return result;
        }
        final Queue<U> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            U node = queue.poll();
            result.add(node.getData());
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return result;
    }

    public static <T, U extends ACTreeNode<T, U>> U deleteTree(U root) {
        if (Objects.isNull(root)) {
            return null;
        }
        root.setLeft(deleteTree(root.getLeft()));
        root.setRight(deleteTree(root.getRight()));
        root = null;
        return root;
    }
}
