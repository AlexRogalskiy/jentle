package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Modifier;

@Getter
@RequiredArgsConstructor
public enum Visibility {
    PUBLIC(Modifier.PUBLIC),
    PROTECTED(Modifier.PROTECTED),
    PRIVATE(Modifier.PRIVATE),
    DEFAULT(0);

    private final int correspondingModifier;
}
