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

    exports com.wildbeeslabs.jentle.collections.tree.iface;
    exports com.wildbeeslabs.jentle.collections.tree.node;
    exports com.wildbeeslabs.jentle.collections.tree.impl;

    exports com.wildbeeslabs.jentle.collections.iface;

    exports com.wildbeeslabs.jentle.collections.exception;

    requires static lombok;
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires commons.collections4;
    requires stdlib;
}
