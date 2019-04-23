package com.wildbeeslabs.jentle.algorithms.tree;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.IPositionalBinaryTree;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintExpressionTour extends EulerTour<String, String> {

    public String execute(final IPositionalBinaryTree<String> tree) {
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
}
