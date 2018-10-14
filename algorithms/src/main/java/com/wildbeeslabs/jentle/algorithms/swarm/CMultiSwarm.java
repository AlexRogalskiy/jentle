/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.swarm;

import com.wildbeeslabs.jentle.algorithms.random.CRandom;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom swarm algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CMultiSwarm {

    /**
     * The swarms managed by this multiswarm.
     */
    @Setter(AccessLevel.NONE)
    private final CSwarm[] swarms;

    /**
     * The best position found within all the {@link #swarms}.
     */
    @Setter(AccessLevel.NONE)
    private long[] bestPosition;

    /**
     * The best fitness score found within all the {@link #swarms}.
     */
    @Setter(AccessLevel.NONE)
    private double bestFitness = Double.NEGATIVE_INFINITY;

    /**
     * The fitness function used to determine how good is a particle.
     */
    @Setter(AccessLevel.NONE)
    private final CFitnessFunction fitnessFunction;

    /**
     * Instantiates a new CMultiSwarm.
     *
     * @param numSwarms the number of {@link #swarms}
     * @param particlesPerSwarm the number of particle for each {@link #swarms}
     * @param fitnessFunction the {@link #fitnessFunction}
     */
    public CMultiSwarm(int numSwarms, int particlesPerSwarm, final CFitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        this.swarms = new CSwarm[numSwarms];
        for (int i = 0; i < numSwarms; i++) {
            this.swarms[i] = new CSwarm(particlesPerSwarm);
        }
    }

    /**
     * Main loop of the algorithm. Iterates all particles of all
     * {@link #swarms}. For each particle, computes the new fitness and checks
     * if a new best position has been found among itself, the swarm and all the
     * swarms and finally updates the particle position and speed.
     */
    public void mainLoop() {
        for (final CSwarm swarm : this.swarms) {
            for (final CParticle particle : swarm.getParticles()) {
                long[] particleOldPosition = particle.getPosition().clone();

                // Calculate the particle fitness.
                particle.setFitness(fitnessFunction.getFitness(particleOldPosition));

                // Check if a new best position has been found for the particle
                // itself, within the swarm and the multiswarm.
                if (particle.getFitness() > particle.getBestFitness()) {
                    particle.setBestFitness(particle.getFitness());
                    particle.setBestPosition(particleOldPosition);

                    if (particle.getFitness() > swarm.getBestFitness()) {
                        swarm.setBestFitness(particle.getFitness());
                        swarm.setBestPosition(particleOldPosition);

                        if (swarm.getBestFitness() > bestFitness) {
                            this.bestFitness = swarm.getBestFitness();
                            this.bestPosition = swarm.getBestPosition().clone();
                        }

                    }
                }

                // Updates the particle position by adding the speed to the
                // actual position.
                @SuppressWarnings("MismatchedReadAndWriteOfArray")
                long[] position = particle.getPosition();
                long[] speed = particle.getSpeed();

                position[0] += speed[0];
                position[1] += speed[1];

                // Updates the particle speed.
                speed[0] = getNewParticleSpeedForIndex(particle, swarm, 0);
                speed[1] = getNewParticleSpeedForIndex(particle, swarm, 1);
            }
        }
    }

    /**
     * Computes a new speed for a given particle of a given swarm on a given
     * axis. The new speed is computed using the formula:
     *
     * <pre>
     * ({@link Constants#INERTIA_FACTOR} * {@link Particle#getSpeed()}) +
     * (({@link Constants#COGNITIVE_WEIGHT} * random(0,1)) * ({@link Particle#getBestPosition()} - {@link Particle#getPosition()})) +
     * (({@link Constants#SOCIAL_WEIGHT} * random(0,1)) * ({@link Swarm#getBestPosition()} - {@link Particle#getPosition()})) +
     * (({@link Constants#GLOBAL_WEIGHT} * random(0,1)) * ({@link #bestPosition} - {@link Particle#getPosition()}))
     * </pre>
     *
     * @param particle the particle whose new speed needs to be computed
     * @param swarm the swarm which contains the particle
     * @param index the index of the particle axis whose speeds needs to be
     * computed
     * @return the new speed of the particle passed on the given axis
     */
    private int getNewParticleSpeedForIndex(final CParticle particle, final CSwarm swarm, int index) {
        return (int) ((CSwarmConstants.INERTIA_FACTOR * particle.getSpeed()[index])
                + (randomizePercentage(CSwarmConstants.COGNITIVE_WEIGHT)
                * (particle.getBestPosition()[index] - particle.getPosition()[index]))
                + (randomizePercentage(CSwarmConstants.SOCIAL_WEIGHT)
                * (swarm.getBestPosition()[index] - particle.getPosition()[index]))
                + (randomizePercentage(CSwarmConstants.GLOBAL_WEIGHT)
                * (this.bestPosition[index] - particle.getPosition()[index])));
    }

    /**
     * Returns a random number between 0 and the value passed as argument.
     *
     * @param value the value to randomize
     * @return a random value between 0 and the one passed as argument
     */
    private double randomizePercentage(double value) {
        return CRandom.generateRandomDouble() * value;
    }
}
