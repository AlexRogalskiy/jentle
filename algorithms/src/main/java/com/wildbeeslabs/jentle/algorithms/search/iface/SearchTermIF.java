package com.wildbeeslabs.jentle.algorithms.search.iface;

import java.io.Serializable;

/**
 * Search term functional interface
 *
 * @param <T> type of search term value
 */
@FunctionalInterface
public interface SearchTermIF<T> extends Serializable {

    /**
     * This method applies a specific match criterion to the given
     * message and returns the result.
     *
     * @param value The match criterion is applied on this message
     * @return true, it the match succeeds, false if the match fails
     */
    boolean match(final T value);
}
