package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>Helper class to hold multi-part Map keys</p>
 */
@Data
@EqualsAndHashCode
@ToString
public class MultipartKey {
    private final Object[] keys;
    private int hashCode;

    /**
     * Constructs an instance of <code>MultipartKey</code> to hold the specified objects.
     *
     * @param keys the set of objects that make up the key.  Each key may be null.
     */
    public MultipartKey(final Object... keys) {
        this.keys = keys;
    }
}
