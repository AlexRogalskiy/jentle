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

/**
 *
 * Custom swarm algorithm constants
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CSwarmConstants {

    /**
     * The inertia factor encourages a particle to continue moving in its
     * current direction.
     */
    public static final double INERTIA_FACTOR = 0.729;

    /**
     * The cognitive weight encourages a particle to move toward its historical
     * best-known position.
     */
    public static final double COGNITIVE_WEIGHT = 1.49445;

    /**
     * The social weight encourages a particle to move toward the best-known
     * position found by any of the particle’s swarm-mates.
     */
    public static final double SOCIAL_WEIGHT = 1.49445;

    /**
     * The global weight encourages a particle to move toward the best-known
     * position found by any particle in any swarm.
     */
    public static final double GLOBAL_WEIGHT = 0.3645;

    /**
     * Upper bound for the random generation. We use it to reduce the
     * computation time since we can rawly estimate it.
     */
    public static final int PARTICLE_UPPER_BOUND = 10000000;

    private CSwarmConstants() {
        // PRIVATE DEFAULT_EMPTY_REGEX CONSTRUCTOR
    }
}
