package com.wildbeeslabs.jentle.algorithms.math;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public interface PCAImplementation {
    
    void compute(RealMatrix data);
    
    RealVector getExplainedVariances();
    
    RealMatrix getPrincipalComponents(int numComponents);
    
    RealMatrix getPrincipalComponents(int numComponents, RealMatrix otherData);
}
