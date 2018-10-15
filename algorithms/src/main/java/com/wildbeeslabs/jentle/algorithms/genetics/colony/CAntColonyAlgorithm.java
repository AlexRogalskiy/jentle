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
package com.wildbeeslabs.jentle.algorithms.genetics.colony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom ant colony algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CAntColonyAlgorithm {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CAntColonyAlgorithm.class);

    private final double c = 1.0;
    private final double alpha = 1;
    private final double beta = 5;
    private final double evaporation = 0.5;
    private final double Q = 500;
    private final double antFactor = 0.8;
    private final double randomFactor = 0.01;

    private final int maxIterations = 1000;

    private final int numberOfCities;
    private final int numberOfAnts;
    private final double graph[][];
    private final double trails[][];
    private final List<CAnt> ants = new ArrayList<>();
    private final Random random = new Random();
    private final double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    public CAntColonyAlgorithm(int noOfCities) {
        this.graph = generateRandomMatrix(noOfCities);
        this.numberOfCities = this.graph.length;
        this.numberOfAnts = (int) (this.numberOfCities * this.antFactor);

        this.trails = new double[this.numberOfCities][this.numberOfCities];
        this.probabilities = new double[this.numberOfCities];
        IntStream.range(0, this.numberOfAnts)
                .forEach(i -> this.ants.add(new CAnt(this.numberOfCities)));
    }

    /**
     * Generate initial solution
     *
     * @param n
     * @return
     */
    public double[][] generateRandomMatrix(int n) {
        double[][] randomMatrix = new double[n][n];
        IntStream.range(0, n)
                .forEach(i -> IntStream.range(0, n)
                        .forEach(j -> randomMatrix[i][j] = Math.abs(random.nextInt(100) + 1)));
        return randomMatrix;
    }

    /**
     * Perform ant optimization
     */
    public void startAntOptimization() {
        IntStream.rangeClosed(1, 3)
                .forEach(i -> {
                    LOGGER.debug("Attempt #" + i);
                    solve();
                });
    }

    /**
     * Use this method to run the main logic
     *
     * @return
     */
    public int[] solve() {
        this.setupAnts();
        this.clearTrails();
        IntStream.range(0, this.maxIterations)
                .forEach(i -> {
                    moveAnts();
                    updateTrails();
                    updateBest();
                });
        LOGGER.debug("Best tour length: " + (this.bestTourLength - this.numberOfCities));
        LOGGER.debug("Best tour order: " + Arrays.toString(this.bestTourOrder));
        return this.bestTourOrder.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        IntStream.range(0, this.numberOfAnts)
                .forEach(i -> {
                    this.ants.forEach(ant -> {
                        ant.clear();
                        ant.visitCity(-1, random.nextInt(this.numberOfCities));
                    });
                });
        this.currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        IntStream.range(this.currentIndex, this.numberOfCities - 1)
                .forEach(i -> {
                    this.ants.forEach(ant -> ant.visitCity(this.currentIndex, selectNextCity(ant)));
                    this.currentIndex++;
                });
    }

    /**
     * Select next city for each ant
     */
    private int selectNextCity(final CAnt ant) {
        int t = this.random.nextInt(this.numberOfCities - this.currentIndex);
        if (this.random.nextDouble() < this.randomFactor) {
            OptionalInt cityIndex = IntStream.range(0, this.numberOfCities)
                    .filter(i -> i == t && !ant.visited(i))
                    .findFirst();
            if (cityIndex.isPresent()) {
                return cityIndex.getAsInt();
            }
        }
        calculateProbabilities(ant);
        double r = this.random.nextDouble();
        double total = 0;
        for (int i = 0; i < this.numberOfCities; i++) {
            total += this.probabilities[i];
            if (total >= r) {
                return i;
            }
        }
        throw new RuntimeException("There are no other cities");
    }

    /**
     * Calculate the next city picks probabilites
     *
     * @param ant
     */
    public void calculateProbabilities(final CAnt ant) {
        int i = ant.trail[this.currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < this.numberOfCities; l++) {
            if (!ant.visited(l)) {
                pheromone += Math.pow(trails[i][l], this.alpha) * Math.pow(1.0 / this.graph[i][l], this.beta);
            }
        }
        for (int j = 0; j < this.numberOfCities; j++) {
            if (ant.visited(j)) {
                this.probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(this.trails[i][j], alpha) * Math.pow(1.0 / this.graph[i][j], this.beta);
                this.probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        for (int i = 0; i < this.numberOfCities; i++) {
            for (int j = 0; j < this.numberOfCities; j++) {
                this.trails[i][j] *= this.evaporation;
            }
        }
        this.ants.stream().forEach((a) -> {
            double contribution = Q / a.trailLength(this.graph);
            for (int i = 0; i < this.numberOfCities - 1; i++) {
                this.trails[a.trail[i]][a.trail[i + 1]] += contribution;
            }
            this.trails[a.trail[this.numberOfCities - 1]][a.trail[0]] += contribution;
        });
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (Objects.isNull(this.bestTourOrder)) {
            this.bestTourOrder = ants.get(0).trail;
            this.bestTourLength = ants.get(0)
                    .trailLength(this.graph);
        }
        ants.stream().filter((a) -> (a.trailLength(this.graph) < this.bestTourLength)).map((a) -> {
            this.bestTourLength = a.trailLength(this.graph);
            return a;
        }).forEach((a) -> {
            this.bestTourOrder = a.trail.clone();
        });
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        IntStream.range(0, this.numberOfCities)
                .forEach(i -> {
                    IntStream.range(0, this.numberOfCities)
                    .forEach(j -> this.trails[i][j] = c);
                });
    }
}
