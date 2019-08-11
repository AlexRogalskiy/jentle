package com.wildbeeslabs.jentle.collections.tree.utils;

import com.wildbeeslabs.jentle.collections.iface.node.TreeNode;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@UtilityClass
public class TreeUtils {

    public static <T, R extends TreeNode<T, R>> List<T> inorderTraversal(final TreeNode<T, R> root) {
        TreeNode<T, R> node = root;
        final List<T> list = new ArrayList<>();
        final Stack<TreeNode<T, R>> stack = new Stack<>();

        while (node != null || !stack.empty()) {
            while (root != null) {
                stack.add(node);
                node = node.getLeft();
            }
            node = stack.pop();
            list.add(node.getData());
            node = node.getRight();
        }
        return list;
    }
}
