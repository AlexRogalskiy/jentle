package com.wildbeeslabs.jentle.algorithms.comparator.entry;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Default difference entry implementation
 */
@Builder
@Data
@EqualsAndHashCode
@ToString
public class DefaultDiffEntry implements DiffEntry<Object> {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    @Builder.Default
    private static final long serialVersionUID = -8477472621769483552L;

    /**
     * Default property name
     */
    private String propertyName;
    /**
     * Default property value of first argument
     */
    private Object first;
    /**
     * Default property value of last argument
     */
    private Object last;
}
