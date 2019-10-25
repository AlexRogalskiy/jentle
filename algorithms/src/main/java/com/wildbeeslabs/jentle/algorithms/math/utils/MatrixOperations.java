//package com.wildbeeslabs.jentle.algorithms.math.utils;
//
//import org.apache.commons.math3.analysis.UnivariateFunction;
//import org.apache.commons.math3.distribution.AbstractRealDistribution;
//import org.apache.commons.math3.distribution.UniformRealDistribution;
//import org.apache.commons.math3.linear.ArrayRealVector;
//import org.apache.commons.math3.linear.BlockRealMatrix;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class MatrixOperations {
//
//    // TODO name this similar to BLAS ???
//    public static RealMatrix XWplusB(RealMatrix X, RealMatrix W, RealVector b) {
//        RealVector h = new ArrayRealVector(X.getRowDimension(), 1.0);
//        return X.multiply(W).add(h.outerProduct(b));
//    }
//
//    public static RealMatrix XWplusB(RealMatrix X, RealMatrix W, RealVector b, UnivariateFunction univariateFunction) {
//        RealMatrix z = XWplusB(X, W, b);
//        z.walkInOptimizedOrder(new UnivariateFunctionMapper(univariateFunction));
//        return z;
//    }
//
//    public static RealMatrix ebeMultiply(RealMatrix a, RealMatrix b) {
//        int rowDimension = a.getRowDimension();
//        int columnDimension = a.getColumnDimension();
//        //TODO a and b should have same dimensions
//        RealMatrix output = new BlockRealMatrix(rowDimension, columnDimension);
//        for (int i = 0; i < rowDimension; i++) {
//            for (int j = 0; j < columnDimension; j++) {
//                output.setEntry(i, j, a.getEntry(i, j) * b.getEntry(i, j));
//            }
//        }
//        return output;
//    }
//
//}
