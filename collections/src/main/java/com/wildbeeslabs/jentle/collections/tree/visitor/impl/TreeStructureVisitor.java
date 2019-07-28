package com.wildbeeslabs.jentle.collections.tree.visitor.impl;

import com.wildbeeslabs.jentle.collections.tree.visitor.iface.TreeVisitor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.joining;

public class TreeStructureVisitor implements TreeVisitor {

    private final List<String> lines = new ArrayList<>();

    private int depth;

    @Override
    public Void visit(final Empty empty) {
        addLine("Empty");
        --depth;
        return null;
    }

    @Override
    public Void visit(final Leaf leaf) {
        addLine("Leaf");
        --depth;
        return null;
    }

    @Override
    public Void visit(final Node node) {
        addLine("Node");
        ++this.depth;
        node.left().accept(this);
        ++this.depth;
        node.right().accept(this);
        --this.depth;
        return null;
    }

    @Override
    public String toString() {
        return lines.stream().collect(joining(getProperty("line.separator")));
    }

    private void addLine(final String label) {
        this.lines.add(repeat(" ", this.depth * 2) + label + ':' + this.depth);
    }

    private static String repeat(final String s, final int times) {
        return nCopies(times, s).stream().collect(joining());
    }
}
