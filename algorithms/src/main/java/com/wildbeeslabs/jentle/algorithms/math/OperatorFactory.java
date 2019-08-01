package com.wildbeeslabs.jentle.algorithms.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperatorFactory {
    static Map<String, Operation> operationMap = new HashMap<>();

    static {
        operationMap.put("add", new Addition());
        //operationMap.put("divide", new Division());
    }

    public static Optional<Operation> getOperation(final String operator) {
        return Optional.ofNullable(operationMap.get(operator));
    }

    public static int calculateUsingFactory(int a, int b, final String operator) {
        Operation targetOperation = OperatorFactory.getOperation(operator).orElseThrow(() -> new IllegalArgumentException("Invalid Operator"));
        return targetOperation.apply(a, b);
    }
}

/*
public int calculate(int a, int b, Operator operator) {
    return operator.apply(a, b);
}
 */

/*
ADD {
    @Override
    public int apply(int a, int b) {
        return a + b;
    }
},
// other operators

public abstract int apply(int a, int b);
 */
