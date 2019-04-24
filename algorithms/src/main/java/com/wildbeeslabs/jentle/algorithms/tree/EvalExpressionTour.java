package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EvalExpressionTour extends EulerTour<Integer, Integer, EvalExpressionTour.TreeNode<Integer>> {

    public Integer execute(final IPositionalBinaryTree<Integer, EvalExpressionTour.TreeNode<Integer>> tree) {
        super.execute(tree);
        log.info("Result: {}", this.eulerTour(tree.root()));
        return null;
    }

    protected void visitExternal(final Position<EvalExpressionTour.TreeNode<Integer>> position, final TraversalResult<Integer> result) {
        result.setFinalResult(position.element().getValue());
    }

    protected void visitRight(final Position<EvalExpressionTour.TreeNode<Integer>> position, final TraversalResult<Integer> result) {
        final OperatorInfo<Integer> op = position.element();
        result.setFinalResult(op.apply(result.getLeftResult(), result.getRightResult()));
    }

    @FunctionalInterface
    public interface OperatorInfo<T> {

        T apply(final T first, final T last);
    }

    @Data
    public static class TreeNode<T> extends ACTreeNodeExtended<Integer, TreeNode<T>> implements OperatorInfo<T>, TreePosition<T, TreeNode<T>> {

        private T value;

        @Override
        public T apply(final T first, final T last) {
            return null;
        }

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
