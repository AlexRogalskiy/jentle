package com.wildbeeslabs.jentle.algorithms.enums;

public enum Cardinality {
    NONE,
    ONE_TO_ONE,
    MANY_TO_ONE,
    ONE_TO_MANY,
    MANY_TO_MANY;

    public boolean isMany() {
        return this.equals(ONE_TO_MANY) || this.equals(MANY_TO_MANY);
    }
}
