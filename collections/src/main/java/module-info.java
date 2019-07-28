/**
 * Jentle collections module configuration file
 */
module jentle.collections {
    exports com.wildbeeslabs.jentle.collections.utils;
    exports com.wildbeeslabs.jentle.collections.stack;

    exports com.wildbeeslabs.jentle.collections.array.iface;
    exports com.wildbeeslabs.jentle.collections.array.impl;

    exports com.wildbeeslabs.jentle.collections.graph.iface;
    exports com.wildbeeslabs.jentle.collections.graph.node;
    exports com.wildbeeslabs.jentle.collections.graph.impl;

    exports com.wildbeeslabs.jentle.collections.list.iface;
    exports com.wildbeeslabs.jentle.collections.list.impl;
    exports com.wildbeeslabs.jentle.collections.list.node;

    exports com.wildbeeslabs.jentle.collections.map.iface;
    exports com.wildbeeslabs.jentle.collections.map.impl;

    exports com.wildbeeslabs.jentle.collections.queue.iface;
    exports com.wildbeeslabs.jentle.collections.queue.impl;

    exports com.wildbeeslabs.jentle.collections.set.iface;
    exports com.wildbeeslabs.jentle.collections.set.impl;

    exports com.wildbeeslabs.jentle.collections.tree.iface.heap;
    exports com.wildbeeslabs.jentle.collections.tree.iface.trie;
    exports com.wildbeeslabs.jentle.collections.tree.iface.tree;
    exports com.wildbeeslabs.jentle.collections.tree.iface.tree.binary;
    exports com.wildbeeslabs.jentle.collections.tree.iface.tree.binary.position;
    exports com.wildbeeslabs.jentle.collections.tree.iface.tree.position;
    exports com.wildbeeslabs.jentle.collections.tree.node;
    exports com.wildbeeslabs.jentle.collections.tree.impl;

    exports com.wildbeeslabs.jentle.collections.iface.position;
    exports com.wildbeeslabs.jentle.collections.iface.visitor;
    exports com.wildbeeslabs.jentle.collections.iface.collection;
    exports com.wildbeeslabs.jentle.collections.iface.iterator;

    exports com.wildbeeslabs.jentle.collections.exception;
    exports com.wildbeeslabs.jentle.collections.iface.node;
    exports com.wildbeeslabs.jentle.collections.model;
    exports com.wildbeeslabs.jentle.collections.iterator;

    requires static lombok;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires commons.collections4;
    requires stdlib;
    requires org.jsoup;
}
