//package com.wildbeeslabs.jentle.algorithms.math;
//
//import org.apache.commons.math3.linear.Array2DRowRealMatrix;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
//import org.apache.commons.math3.util.FastMath;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class SoftMaxCrossEntropyLossFunction implements LossFunction {
//
//    @Override
//    public double getSampleLoss(double predicted, double target) {
//        return predicted > 0 ? -1.0 * target * FastMath.log(predicted) : 0;
//    }
//
//    @Override
//    public double getSampleLoss(RealVector predicted, RealVector target) {
//        double sampleLoss = 0.0;
//        for (int i = 0; i < predicted.getDimension(); i++) {
//            sampleLoss += getSampleLoss(predicted.getEntry(i), target.getEntry(i));
//        }
//        return sampleLoss;
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
//    /**
//     * dE/dy = - t_i / y_i
//     * @param predicted
//     * @param target
//     * @return
//     */
//    @Override
//    public double getSampleLossGradient(double predicted, double target) {
//        return -1.0 * target / predicted;
//    }
//
//    @Override
//    public RealVector getSampleLossGradient(RealVector predicted, RealVector target) {
//        return target.ebeDivide(predicted).mapMultiplyToSelf(-1.0);
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
//}
