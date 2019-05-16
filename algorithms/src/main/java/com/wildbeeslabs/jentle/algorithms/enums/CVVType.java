package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Helper enumeration class to process credit cards cvv
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Getter
@RequiredArgsConstructor
public enum CVVType {
    CVV3(3),
    CVV4(4);

    private final Integer length;

    public static CVVType fromLength(final int length) {
        return Arrays.stream(values())
            .filter(type -> type.getLength().equals(length))
            .findFirst()
            .orElse(null);
    }
}
