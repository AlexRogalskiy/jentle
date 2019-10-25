package com.wildbeeslabs.jentle.algorithms.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Pair;

/**
 *
 * @author Michael Brzustowicz
 */
public class MultiNormalMixtureDataset {
    int dimension;
    List<Pair<Double, MultivariateNormalDistribution>> mixture;
    MixtureMultivariateNormalDistribution mixtureDistribution;

    public MultiNormalMixtureDataset(int dimension) {
        this.dimension = dimension;
        mixture = new ArrayList<>();
    }

    public MixtureMultivariateNormalDistribution getMixtureDistribution() {
        return mixtureDistribution;
    }

    public void createRandomMixtureModel(int numComponents, double boxSize, long seed) {
        Random rnd = new Random(seed);
        double limit = boxSize / dimension;
        UniformRealDistribution dist = new UniformRealDistribution(-limit, limit);
        UniformRealDistribution disC = new UniformRealDistribution(-1, 1);
        dist.reseedRandomGenerator(seed);
        disC.reseedRandomGenerator(seed);
        for (int i = 0; i < numComponents; i++) {
            double alpha = rnd.nextDouble();
            double[] means = dist.sample(dimension);
            double[][] cov = getRandomCovariance(disC);
            MultivariateNormalDistribution multiNorm = new MultivariateNormalDistribution(means, cov);
            addMultinormalDistributionToModel(alpha, multiNorm);
        }
        mixtureDistribution = new MixtureMultivariateNormalDistribution(mixture);
        mixtureDistribution.reseedRandomGenerator(seed); // calls to sample() will return same results
    }
    
    /**
     * NOTE this is for adding both internal and external, known distros but
     * need to figure out clean way to add the mixture to mixtureDistribution!!!
     * @param alpha
     * @param dist 
     */
    public void addMultinormalDistributionToModel(double alpha, MultivariateNormalDistribution dist) {
        // note all alpha will be L1 normed
        mixture.add(new Pair(alpha, dist));
    }
    
    public double[][] getSimulatedData(int size) {
        return mixtureDistribution.sample(size);
    }
    
    private double[][] getRandomCovariance(AbstractRealDistribution dist) {
        double[][] data = new double[2*dimension][dimension];
        double determinant = 0.0;
        Covariance cov = new Covariance();
        while(Math.abs(determinant) == 0) {
            for (int i = 0; i < data.length; i++) {
                data[i] = dist.sample(dimension);
            }
            // check if cov matrix is singular ... if so, keep going
            cov = new Covariance(data);
            determinant = new CholeskyDecomposition(cov.getCovarianceMatrix()).getDeterminant();
        }
        return cov.getCovarianceMatrix().getData();
    }
    
}
