package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.Tree;
import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Leaf implements Tree {
    private final String value;

    @Override
    public Object accept(TreeVisitor visitor) {
        return visitor.visit(this);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        final TreeStructureVisitor v = new TreeStructureVisitor();
        accept(v);
        return v.toString();
    }
}
