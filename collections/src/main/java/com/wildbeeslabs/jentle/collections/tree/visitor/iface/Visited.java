package com.wildbeeslabs.jentle.collections.tree.visitor.iface;

public interface Visited<V> {

    Object accept(final V visitor);
}
