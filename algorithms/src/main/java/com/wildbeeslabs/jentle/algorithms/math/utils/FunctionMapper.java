package com.wildbeeslabs.jentle.algorithms.math.utils;

import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

/**
 *
 * @author Michael Brzustowicz
 */
public class FunctionMapper implements RealMatrixChangingVisitor {

    private final double power;
    
    public FunctionMapper(double power) {
        this.power = power;
    }
    
    @Override
    public void start(int rows, int columns, int startRow, int endRow,
            int startColumn, int endColumn) {
        // do nothing
    }

    @Override
    public double visit(int row, int column, double value) {
        return Math.pow(value, power);
    }

    @Override
    public double end() {
        return 0;
    }
    
}
