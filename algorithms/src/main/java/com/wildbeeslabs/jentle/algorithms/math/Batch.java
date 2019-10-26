//package com.wildbeeslabs.jentle.algorithms.math;
//
//import org.apache.commons.math3.linear.RealMatrix;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class Batch extends MatrixResampler {
//
//    public Batch(RealMatrix features, RealMatrix labels) {
//        super(features, labels);
//    }
//
//    public void calcNextBatch(int batchSize) {
//        super.calculateTestTrainSplit(batchSize);
//    }
//
//    public RealMatrix getInputBatch() {
//        return super.getTestingFeatures();
//    }
//
//    public RealMatrix getTargetBatch() {
//        return super.getTestingLabels();
//    }
//
//}
