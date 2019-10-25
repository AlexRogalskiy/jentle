package com.wildbeeslabs.jentle.algorithms.math;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

/**
 *
 * @author Michael Brzustowicz
 */
public class UnivariateFunctionMapper implements RealMatrixChangingVisitor {
    
    UnivariateFunction univariateFunction;

    public UnivariateFunctionMapper(UnivariateFunction univariateFunction) {
        this.univariateFunction = univariateFunction;
    }
    
    @Override
    public void start(int rows, int columns, int startRow, int endRow,
            int startColumn, int endColumn) {
        //NA
    }

    @Override
    public double visit(int row, int column, double value) {
        return univariateFunction.value(value);
    }

    @Override
    public double end() {
        return 0.0;
    }
    
}
