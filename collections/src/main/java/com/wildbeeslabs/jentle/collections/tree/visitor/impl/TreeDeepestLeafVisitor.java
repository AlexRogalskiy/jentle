package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;

import java.util.AbstractMap;

public class TreeDeepestLeafVisitor implements TreeVisitor {

    @Override
    public AbstractMap.SimpleImmutableEntry<String, Integer> visit(final Empty empty) {
        return new AbstractMap.SimpleImmutableEntry<>("", 0);
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<String, Integer> visit(Leaf leaf) {
        return new AbstractMap.SimpleImmutableEntry<>(leaf.value(), 0);
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<String, Integer> visit(final Node node) {
        @SuppressWarnings("unchecked")
        AbstractMap.SimpleImmutableEntry<String, Integer> leftResult = (AbstractMap.SimpleImmutableEntry<String, Integer>) node.left().accept(this);

        @SuppressWarnings("unchecked")
        AbstractMap.SimpleImmutableEntry<String, Integer> rightResult = (AbstractMap.SimpleImmutableEntry<String, Integer>) node.right().accept(this);

        int leftDepth = leftResult.getValue();
        int rightDepth = rightResult.getValue();
        return leftDepth >= rightDepth
            ? new AbstractMap.SimpleImmutableEntry<>(leftResult.getKey(), 1 + leftDepth)
            : new AbstractMap.SimpleImmutableEntry<>(rightResult.getKey(), 1 + rightDepth);
    }
}
