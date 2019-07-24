package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FtsChangeType {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");

    private final String id;

    public static FtsChangeType fromId(final String id) {
        if ("I".equals(id))
            return INSERT;
        else if ("U".equals(id))
            return UPDATE;
        else if ("D".equals(id))
            return DELETE;
        return null;
    }
}
