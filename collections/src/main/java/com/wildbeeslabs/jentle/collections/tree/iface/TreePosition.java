package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.node.TreeNode;
import com.wildbeeslabs.jentle.collections.iface.position.Position;

/**
 * Custom tree {@link Position} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface TreePosition<T, R> extends Position<T>, TreeNode<T, R> {
}
