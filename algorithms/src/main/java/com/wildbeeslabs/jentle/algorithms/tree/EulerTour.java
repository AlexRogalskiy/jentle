package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;

public abstract class EulerTour<T, V> {

    protected IPositionalBinaryTree<T> tree;

    public T execute(final IPositionalBinaryTree<T> tree) {
        return null;
    }

    protected V eulerTour(final Position<T> position) {
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

    protected void visitExternal(final Position<T> position, final TraversalResult<V> result) {

    }

    protected void visitLeft(final Position<T> position, final TraversalResult<V> result) {

    }

    protected void visitBelow(final Position<T> position, final TraversalResult<V> result) {

    }

    protected void visitRight(final Position<T> position, final TraversalResult<V> result) {

    }

    protected TraversalResult<V> initResult() {
        return new TraversalResult<>();
    }

    protected V buildResult(final TraversalResult<V> result) {
        return result.getFinalResult();
    }
}
