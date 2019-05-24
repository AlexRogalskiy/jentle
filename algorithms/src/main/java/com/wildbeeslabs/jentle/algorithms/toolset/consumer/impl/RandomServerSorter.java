package com.wildbeeslabs.jentle.algorithms.toolset.consumer.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Strategy class for work with {@link Consumer}.
 * <p>
 * Sorts the list of servers randomly.
 */
public class RandomServerSorter implements Consumer<List<String>> {

    @Override
    public void accept(final List<String> strings) {
        Collections.shuffle(strings);
    }
}
