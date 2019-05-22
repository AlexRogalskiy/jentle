package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper enumeration class to process Operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Getter
@RequiredArgsConstructor
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };

    private final String symbol;

    @Override
    public String toString() {
        return this.symbol;
    }

    public abstract double apply(double x, double y);

    private static final Map<String, Operation> OPERATION_MAP = new HashMap<>();

    static {
        for (final Operation op : values())
            OPERATION_MAP.put(op.toString(), op);
    }

    public static Operation fromSymbol(final String symbol) {
        return OPERATION_MAP.get(symbol);
    }
}
