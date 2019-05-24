package com.wildbeeslabs.jentle.algorithms.toolset.consumer.impl;

import java.util.List;
import java.util.function.Consumer;

/**
 * Strategy class for work with {@link Consumer}.
 * <p>
 * Does not sorts the list of servers - this is a legacy behavior for platform version &lt; 6.5.
 */
public class NoopServerSorter implements Consumer<List<String>> {

    @Override
    public void accept(final List<String> strings) {
    }
}
