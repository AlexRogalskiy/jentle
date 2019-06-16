package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TagsSorter {
    ALPHA("alpha");

    private final String value;

    public static TagsSorter of(final String name) {
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
