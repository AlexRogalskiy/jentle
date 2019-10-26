package com.wildbeeslabs.jentle.algorithms.math;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * 
 * @author Michael Brzustowicz
 */
public interface LossFunction {
    
    /**
     * loss of one dimension of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLoss(double predicted, double target);
    
    /**
     * combined loss over all dimensions of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLoss(RealVector predicted, RealVector target);
    
    /**
     * average loss over all samples
     * @param predicted
     * @param target
     * @return 
     */
    public double getMeanLoss(RealMatrix predicted, RealMatrix target);
    
    /**
     * derivative of loss of one dimension of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLossGradient(double predicted, double target);
    
    /**
     * derivative of loss over all dimensions of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public RealVector getSampleLossGradient(RealVector predicted, RealVector target);
    
    /**
     * 
     * @param predicted
     * @param target
     * @return 
     */
    public RealMatrix getLossGradient(RealMatrix predicted, RealMatrix target);
}
