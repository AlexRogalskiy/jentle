/**
 * Jentle collections module configuration file
 */
module jentle.collections {
    exports com.wildbeeslabs.jentle.collections.utils;
    exports com.wildbeeslabs.jentle.collections.stack;
    exports com.wildbeeslabs.jentle.collections.graph.node;
    exports com.wildbeeslabs.jentle.collections.tree;
    exports com.wildbeeslabs.jentle.collections.tree.node;
    exports com.wildbeeslabs.jentle.collections.list.node;
    exports com.wildbeeslabs.jentle.collections.interfaces;
    exports com.wildbeeslabs.jentle.collections.exception;
    exports com.wildbeeslabs.jentle.collections.list;
    exports com.wildbeeslabs.jentle.collections.map;
    exports com.wildbeeslabs.jentle.collections.graph;
    exports com.wildbeeslabs.jentle.collections.set;

    requires static lombok;
    requires slf4j.api;
    requires edu.princeton.cs.introcs;
    requires org.apache.commons.lang3;
    requires commons.collections4;
}
