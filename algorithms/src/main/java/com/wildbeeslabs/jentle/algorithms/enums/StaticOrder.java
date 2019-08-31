package com.wildbeeslabs.jentle.algorithms.enums;

import com.google.common.collect.ImmutableList;

/**
 * The different ways to order static imports.
 */
enum StaticOrder {
    /**
     * Sorts import statements so that all static imports come before all non-static imports and
     * otherwise sorted alphabetically.
     */
    STATIC_FIRST(ImmutableList.of(Boolean.TRUE, Boolean.FALSE)),

    /**
     * Sorts import statements so that all static imports come after all non-static imports and
     * otherwise sorted alphabetically.
     */
    STATIC_LAST(ImmutableList.of(Boolean.FALSE, Boolean.TRUE));

    private final Iterable<Boolean> groupOrder;

    StaticOrder(Iterable<Boolean> groupOrder) {
        this.groupOrder = groupOrder;
    }

    public Iterable<Boolean> groupOrder() {
        return groupOrder;
    }
}
