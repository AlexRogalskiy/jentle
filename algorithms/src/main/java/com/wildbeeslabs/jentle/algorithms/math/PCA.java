//package com.wildbeeslabs.jentle.algorithms.math;
//
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.linear.RealVector;
//
///**
// *
// * @author Michael Brzustowicz
// */
//public class PCA {
//
//    private final PCAImplementation pCAImplementation;
//
//    /**
//     * default is SVD implementation
//     * @param data
//     */
//    public PCA(RealMatrix data) {
//        this(data, new PCASVDImplementation());
//    }
//
//    public PCA(RealMatrix data, PCAImplementation pCAImplementation) {
//        this.pCAImplementation = pCAImplementation;
//        this.pCAImplementation.compute(data);
//    }
//
//    /**
//     * Projects the centered data onto the new basis with k components
//     * @param k number of components to use
//     * @return
//     */
//    public RealMatrix getPrincipalComponents(int k) {
//        return pCAImplementation.getPrincipalComponents(k);
//    }
//
//    public RealMatrix getPrincipalComponents(int k, RealMatrix otherData) {
//        return pCAImplementation.getPrincipalComponents(k, otherData);
//    }
//
//    public RealVector getExplainedVariances() {
//        return pCAImplementation.getExplainedVariances();
//    }
//
//    public RealVector getCumulativeVariances() {
//        RealVector variances = getExplainedVariances();
//        RealVector cumulative = variances.copy();
//        double sum = 0;
//        for (int i = 0; i < cumulative.getDimension(); i++) {
//            sum += cumulative.getEntry(i);
//            cumulative.setEntry(i, sum);
//        }
//        return cumulative;
//    }
//
//    public int getNumberOfComponents(double threshold) {
//        RealVector cumulative = getCumulativeVariances();
//        int numComponents=1;
//        for (int i = 0; i < cumulative.getDimension(); i++) {
//            numComponents = i + 1;
//            if(cumulative.getEntry(i) >= threshold) {
//                break;
//            }
//        }
//        return numComponents;
//    }
//
//    public RealMatrix getPrincipalComponents(double threshold) {
//        int numComponents = getNumberOfComponents(threshold);
//        return getPrincipalComponents(numComponents);
//    }
//
//    public RealMatrix getPrincipalComponents(double threshold, RealMatrix otherData) {
//        int numComponents = getNumberOfComponents(threshold);
//        return getPrincipalComponents(numComponents, otherData);
//    }
//
//}
