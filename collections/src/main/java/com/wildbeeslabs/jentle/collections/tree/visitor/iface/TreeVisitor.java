package com.wildbeeslabs.jentle.collections.tree.visitor.iface;

import com.wildbeeslabs.jentle.collections.tree.visitor.impl.Empty;
import com.wildbeeslabs.jentle.collections.tree.visitor.impl.Leaf;
import com.wildbeeslabs.jentle.collections.tree.visitor.impl.Node;

public interface TreeVisitor {

    Object visit(final Empty empty);

    Object visit(final Leaf leaf);

    Object visit(final Node node);
}
