package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.Tree;
import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Node implements Tree {
    private final Tree left;
    private final Tree right;

    @Override public Object accept(final TreeVisitor visitor) {
        return visitor.visit(this);
    }

    public Tree left() {
        return left;
    }

    public Tree right() {
        return right;
    }

    @Override public String toString() {
        TreeStructureVisitor v = new TreeStructureVisitor();
        accept(v);
        return v.toString();
    }
}
