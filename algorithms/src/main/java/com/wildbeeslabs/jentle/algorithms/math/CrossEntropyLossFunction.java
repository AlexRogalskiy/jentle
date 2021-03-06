//package com.wildbeeslabs.jentle.algorithms.math;
//
//import org.apache.commons.math3.linear.Array2DRowRealMatrix;
//import org.apache.commons.math3.linear.ArrayRealVector;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
//import org.apache.commons.math3.util.FastMath;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class CrossEntropyLossFunction implements LossFunction {
//
//    @Override
//    public double getSampleLoss(double predicted, double target) {
//        return -1.0 * (target * ((predicted>0)?FastMath.log(predicted):0) +
//                (1.0 - target)*(predicted<1?FastMath.log(1.0-predicted):0));
//    }
//
//    public double getSampleLoss(double[] predicted, double[] target) {
//        double loss = 0.0;
//        for (int i = 0; i < predicted.length; i++) {
//            loss += getSampleLoss(predicted[i], target[i]);
//        }
//        return loss;
//    }
//
//    @Override
//    public double getSampleLoss(RealVector predicted, RealVector target) {
//        double loss = 0.0;
//        for (int i = 0; i < predicted.getDimension(); i++) {
//            loss += getSampleLoss(predicted.getEntry(i), target.getEntry(i));
//        }
//        return loss;
//    }
//
//    @Override
//    public double getMeanLoss(RealMatrix predicted, RealMatrix target) {
//        SummaryStatistics stats = new SummaryStatistics();
//        for (int i = 0; i < predicted.getRowDimension(); i++) {
//            stats.addValue(getSampleLoss(predicted.getRowVector(i), target.getRowVector(i)));
//        }
//        return stats.getMean();
//    }
//
//    @Override
//    public double getSampleLossGradient(double predicted, double target) {
//        // NOTE this blows up if predicted = 0 or 1, which it should never be
//        return (predicted - target) / (predicted * (1 - predicted));
//    }
//
//    @Override
//    public RealVector getSampleLossGradient(RealVector predicted, RealVector target) {
//        RealVector loss = new ArrayRealVector(predicted.getDimension());
//        for (int i = 0; i < predicted.getDimension(); i++) {
//            loss.setEntry(i, getSampleLossGradient(predicted.getEntry(i), target.getEntry(i)));
//        }
//        return loss;
//    }
//
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
