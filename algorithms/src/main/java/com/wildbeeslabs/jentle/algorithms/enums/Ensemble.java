package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Helper enumeration class to process Ensembles
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Getter
@RequiredArgsConstructor
public enum Ensemble {
    SOLO(1),
    DUET(2),
    TRIO(3),
    QUARTET(4),
    QUINTET(5),
    SEXTET(6),
    SEPTET(7),
    OCTET(8),
    DOUBLE_QUARTET(8),
    NONET(9),
    DECTET(10),
    TRIPLE_QUARTET(12);

    private final int numberOfMusicians;

    public static Ensemble fromNumber(final int number) {
        return Arrays.stream(values())
            .filter(type -> type.getNumberOfMusicians() == number)
            .findFirst()
            .orElse(null);
    }
}
