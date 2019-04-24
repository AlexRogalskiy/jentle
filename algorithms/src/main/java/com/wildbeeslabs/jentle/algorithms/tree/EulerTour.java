package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;

public abstract class EulerTour<T, V, R extends TreePosition<T, R>> {

    /**
     * Default {@link IPositionalBinaryTree} instance
     */
    protected IPositionalBinaryTree<T, R> tree;

    /**
     * Returns euler tour result {@code T} by input {@link IPositionalBinaryTree} argument
     *
     * @param tree - initial input {@link IPositionalBinaryTree} argument
     * @return euler tour result {@code T}
     */
    public T execute(final IPositionalBinaryTree<T, R> tree) {
        return null;
    }

    /**
     * Returns traversal result {@code V} representation by input {@code R} position
     *
     * @param position - initial input {@code R} position
     * @return traversal result {@code V} representation
     */
    protected V eulerTour(final R position) {
        final TraversalResult<V> result = this.initResult();
        if (this.tree.isExternal(position)) {
            this.visitExternal(position, result);
        } else {
            this.visitLeft(position, result);
            result.setLeftResult(this.eulerTour(this.tree.getLeft(position)));
            this.visitBelow(position, result);
            result.setRightResult(this.eulerTour(this.tree.getRight(position)));
            this.visitRight(position, result);
        }
        return this.buildResult(result);
    }

    /**
     * Visits external {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input external {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitExternal(final R position, final TraversalResult<V> result) {
    }

    /**
     * Visits left {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input left {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitLeft(final R position, final TraversalResult<V> result) {
    }

    /**
     * Visits below {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input below {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitBelow(final R position, final TraversalResult<V> result) {
    }

    /**
     * Visits right {@link Position} by evaluating {@link TraversalResult}
     *
     * @param position - initial input right {@link Position} to start visiting
     * @param result   - initial input {@link TraversalResult} as result set
     */
    protected void visitRight(final R position, final TraversalResult<V> result) {
    }

    /**
     * Initializes {@link TraversalResult} instance
     *
     * @return new {@link TraversalResult} instance
     */
    protected TraversalResult<V> initResult() {
        return new TraversalResult<>();
    }

    /**
     * Returns traversal result {@code V} representation by input {@link TraversalResult}
     *
     * @param result - initial input {@code TraversalResult} to evaluate by
     * @return traversal result {@code V} representation
     */
    protected V buildResult(final TraversalResult<V> result) {
        return result.getFinalResult();
    }
}
