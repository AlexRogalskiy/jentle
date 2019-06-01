package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.slf4j.event.EventConstants.*;

/**
 * Level type {@link Enum}
 */
@Getter
@RequiredArgsConstructor
public enum LevelType {
    ERROR(ERROR_INT, "ERROR"),
    WARN(WARN_INT, "WARN"),
    INFO(INFO_INT, "INFO"),
    DEBUG(DEBUG_INT, "DEBUG"),
    TRACE(TRACE_INT, "TRACE");

    private final int levelInt;
    private final String levelStr;
}
