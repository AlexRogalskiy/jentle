package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.Tree;
import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;

public class Empty implements Tree {
    @Override
    public Object accept(final TreeVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        final TreeStructureVisitor v = new TreeStructureVisitor();
        accept(v);
        return v.toString();
    }
}
