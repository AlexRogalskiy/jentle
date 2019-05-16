package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper enumeration class to process Phases
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
public enum Phase {

    SOLID,
    LIQUID,
    GAS;

    @Getter
    @RequiredArgsConstructor
    public enum Transition {

        MELT(SOLID, LIQUID),
        FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS),
        CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS),
        DEPOSIT(GAS, SOLID);

        private final Phase from;
        private final Phase to;

        private static final Map<Phase, Map<Phase, Transition>> enumMap = Stream.of(values()).collect(Collectors.groupingBy(t -> t.from, () -> new EnumMap<>(Phase.class), Collectors.toMap(t -> t.to, t -> t, (x, y) -> y, () -> new EnumMap<>(Phase.class))));

        public static Transition from(final Phase from, final Phase to) {
            return enumMap.get(from).get(to);
        }
    }
}
