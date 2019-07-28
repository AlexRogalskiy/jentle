package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;

public class TreeDepthVisitor implements TreeVisitor {

    @Override
    public Integer visit(final Empty empty) {
        return 0;
    }

    @Override
    public Integer visit(final Leaf leaf) {
        return 0;
    }

    @Override
    public Integer visit(final Node node) {
        final Integer leftDepth = (Integer) node.left().accept(this);
        final Integer rightDepth = (Integer) node.right().accept(this);
        return (1 + Math.max(leftDepth, rightDepth));
    }
}
