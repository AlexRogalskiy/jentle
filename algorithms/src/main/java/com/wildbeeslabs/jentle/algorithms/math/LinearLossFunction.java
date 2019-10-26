//package com.wildbeeslabs.jentle.algorithms.math;
//
//import org.apache.commons.math3.analysis.function.Signum;
//import org.apache.commons.math3.linear.Array2DRowRealMatrix;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class LinearLossFunction implements LossFunction {
//
//    @Override
//    public double getSampleLoss(double predicted, double target) {
//        return Math.abs(predicted - target);
//    }
//
//    @Override
//    public double getSampleLoss(RealVector predicted, RealVector target) {
//        return predicted.getL1Distance(target);
//    }
//
//    @Override
//    public double getMeanLoss(RealMatrix predicted, RealMatrix target) {
//        SummaryStatistics stats = new SummaryStatistics();
//        for (int i = 0; i < predicted.getRowDimension(); i++) {
//            double dist = getSampleLoss(predicted.getRowVector(i), target.getRowVector(i));
//            stats.addValue(dist);
//        }
//        return stats.getMean();
//    }
//
//    @Override
//    public double getSampleLossGradient(double predicted, double target) {
//        return Math.signum(predicted - target); // -1, 0, 1
//    }
//
//    @Override
//    public RealVector getSampleLossGradient(RealVector predicted, RealVector target) {
//        return predicted.subtract(target).map(new Signum());
//    }
//
//    //TODO SparseToSignum would be nice!!! ie only process elements of the iterable
//    @Override
//    public RealMatrix getLossGradient(RealMatrix predicted, RealMatrix target) {
//        RealMatrix loss = new Array2DRowRealMatrix(predicted.getRowDimension(), predicted.getColumnDimension());
//        for (int i = 0; i < predicted.getRowDimension(); i++) {
//            loss.setRowVector(i, getSampleLossGradient(predicted.getRowVector(i), target.getRowVector(i)));
//        }
//        return loss;
//    }
//
//}
