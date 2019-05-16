package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    SET((short) -3, false),
    UNKNOWN((short) -2, false),
    RECOGNIZED((short) -1, false),
    NOT_SUPPORTED((short) 0, true),
    NOT_RECOGNIZED((short) 1, true),
    NOT_ALLOWED((short) 2, true);

    private final short type;

    private final boolean isExceptional;
}
