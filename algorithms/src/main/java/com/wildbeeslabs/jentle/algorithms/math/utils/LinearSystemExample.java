package com.wildbeeslabs.jentle.algorithms.math.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 *
 * @author Michael Brzustowicz
 */
public class LinearSystemExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[][] xData = {{0, 0.5, 0.2}, {1, 1.2, .9}, {2, 2.5, 1.9}, {3, 3.6, 4.2}};
        double[][] yData = {{-1, -0.5}, {0.2, 1}, {0.9, 1.2}, {2.1, 1.5}};
        double[] ones = {1.0, 1.0, 1.0, 1.0};
        int xRows = 4;
        int xCols = 3;
        RealMatrix x = new Array2DRowRealMatrix(xRows, xCols + 1);
        x.setSubMatrix(xData, 0, 0);
        x.setColumn(3, ones); // 4th column is index of 3 !!!
        RealMatrix y = new Array2DRowRealMatrix(yData);
        
        SingularValueDecomposition svd = new SingularValueDecomposition(x);
        RealMatrix solution = svd.getSolver().solve(y);
        System.out.println(solution);
    }
    
}
