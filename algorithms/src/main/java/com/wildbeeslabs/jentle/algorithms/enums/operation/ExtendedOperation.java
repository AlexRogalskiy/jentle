package com.wildbeeslabs.jentle.algorithms.enums.operation;

import com.wildbeeslabs.jentle.algorithms.toolset.iface.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExtendedOperation implements Operation {
    EXP("^") {
        @Override
        public double apply(double x, double y) {
            return Math.pow(x, y);
        }
    },
    REMAINDER("%") {
        @Override
        public double apply(double x, double y) {
            return x % y;
        }
    };

    private final String symbol;

    @Override
    public String toString() {
        return this.symbol;
    }

//    public static void main(final String[] args) {
//        double x = Double.parseDouble(args[0]);
//        double y = Double.parseDouble(args[1]);
//        test(ExtendedOperation.class, x, y);
//        test2(Arrays.asList(ExtendedOperation.values()), x, y);
//    }

//    private static <T extends Enum<T> & Operation> void test(final Class<T> opSet, double x, double y) {
//        for (final Operation op : opSet.getEnumConstants())
//            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
//    }

//    private static void test2(final Collection<? extends Operation> opSet, double x, double y) {
//        for (final Operation op : opSet)
//            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
//    }
}
