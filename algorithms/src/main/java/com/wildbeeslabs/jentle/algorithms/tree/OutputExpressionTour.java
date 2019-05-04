package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutputExpressionTour extends EulerTour<String, String, OutputExpressionTour.TreeNode<String>> {

    public String execute(final IPositionalBinaryTree<String, OutputExpressionTour.TreeNode<String>> tree) {
        super.execute(tree);
        this.eulerTour(tree.root());
        return null;
    }

    protected void visitExternal(final Position<String> position, final TraversalResult<String> result) {
        log.info("Element: {}", position.element());
    }

    protected void visitLeft(final Position<String> position, final TraversalResult<String> result) {
        log.info("(");
    }

    protected void visitBelow(final Position<String> position, final TraversalResult<String> result) {
        log.info("Element: {}", position.element());
    }

    protected void visitRight(final Position<String> position, final TraversalResult<String> result) {
        log.info(")");
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TreeNode<T> extends ACTreeNodeExtended<Integer, OutputExpressionTour.TreeNode<T>> implements TreePosition<T, OutputExpressionTour.TreeNode<T>> {

        private T value;

        @Override
        public T element() throws InvalidPositionException {
            return this.getValue();
        }

        @Override
        public void setElement(final T value) {
            this.setValue(value);
        }
    }
}
