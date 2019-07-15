package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents possible screen orientations.
 */
@Getter
@RequiredArgsConstructor
public enum ScreenOrientation {
    LANDSCAPE("landscape"),
    PORTRAIT("portrait");

    private final String value;
}
