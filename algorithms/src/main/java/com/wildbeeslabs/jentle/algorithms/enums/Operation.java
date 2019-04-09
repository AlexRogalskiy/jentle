package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;

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

    Operation(final String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    abstract double apply(double x, double y);

    private static final Map<String, Operation> stringToEnum = new HashMap<>();

    static {
        for (final Operation op : values())
            stringToEnum.put(op.toString(), op);
    }

    public static Operation fromString(final String symbol) {
        return stringToEnum.get(symbol);
    }
}
