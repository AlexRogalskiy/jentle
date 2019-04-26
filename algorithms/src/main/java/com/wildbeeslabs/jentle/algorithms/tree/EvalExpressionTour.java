package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EvalExpressionTour extends EulerTour<Integer, Integer, EvalExpressionTour.TreeNode<Integer, Integer>> {

    public Integer execute(final IPositionalBinaryTree<Integer, EvalExpressionTour.TreeNode<Integer, Integer>> tree) {
        super.execute(tree);
        log.info("Result: {}", this.eulerTour(tree.root()));
        return null;
    }

    /**
     * Visits external {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input external {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitExternal(final Position<EvalExpressionTour.TreeNode<Integer, Integer>> position, final TraversalResult<Integer> result) {
        result.setFinalResult(position.element().getValue());
    }

    /**
     * Visits right {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input right {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitRight(final Position<EvalExpressionTour.TreeNode<Integer, Integer>> position, final TraversalResult<Integer> result) {
        final OperatorInfo<Integer, Integer> op = position.element();
        result.setFinalResult(op.apply(result.getLeftResult(), result.getRightResult()));
    }

    @FunctionalInterface
    public interface OperatorInfo<T, R> {

        /**
         * Returns operation result {@code R} by input parameters {@code T}
         *
         * @param first - initial input first argument {@code T} to calculate by
         * @param last  - initial input second argument {@code T} to calculate with
         * @return operation result {@code R}
         */
        R apply(final T first, final T last);
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TreeNode<T, R> extends ACTreeNodeExtended<Integer, TreeNode<T, R>> implements OperatorInfo<T, R>, TreePosition<T, TreeNode<T, R>> {

        private T value;

        @Override
        public R apply(final T first, final T last) {
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
