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
 * Custom swarm model
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CSwarm {

    /**
     * The particles of this swarm.
     */
    @Setter(AccessLevel.NONE)
    private final CParticle[] particles;

    /**
     * The best position found within the particles of this swarm.
     */
    private long[] bestPosition;

    /**
     * The best fitness score found within the particles of this swarm.
     */
    private double bestFitness = Double.NEGATIVE_INFINITY;

    /**
     * Instantiates a new Swarm.
     *
     * @param numParticles the number of particles of the swarm
     */
    public CSwarm(int numParticles) {
        assert (numParticles > 0);
        this.particles = new CParticle[numParticles];
        for (int i = 0; i < numParticles; i++) {
            long[] initialParticlePosition = {CRandom.generateRandomInt(CSwarmConstants.PARTICLE_UPPER_BOUND), CRandom.generateRandomInt(CSwarmConstants.PARTICLE_UPPER_BOUND)};
            long[] initialParticleSpeed = {CRandom.generateRandomInt(CSwarmConstants.PARTICLE_UPPER_BOUND), CRandom.generateRandomInt(CSwarmConstants.PARTICLE_UPPER_BOUND)};
            this.particles[i] = new CParticle(initialParticlePosition, initialParticleSpeed);
        }
    }
}
