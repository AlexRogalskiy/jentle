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
package com.wildbeeslabs.jentle.algorithms.genetics;

import com.wildbeeslabs.jentle.algorithms.utils.CMatrixUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CNumericUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import lombok.Data;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom genetics algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CGenetics {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CGenetics.class);

    private CGenetics() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    @Data
    public static class CMemberGenetics<T extends CMember> {

        private final double uniformRate = 0.5;
        private final double mutationRate = 0.025;
        private final int tournamentSize = 5;
        private final boolean elitism = true;
        private static long[] solution;

        public boolean runTask(int populationSize, final String solution) {
            setSolution(solution);
            CMemberSet<T> myPop = new CMemberSet<>(populationSize, true);

            int generationCount = 1;
            while (myPop.getFittest().getFitness() < getMaxFitness()) {
                LOGGER.debug(String.format("Generation: %s, Correct genes found: %s", generationCount, myPop.getFittest().getFitness()));
                myPop = evolvePopulation(myPop);
                generationCount++;
            }
            LOGGER.debug("Solution found!");
            LOGGER.debug("Generation: " + generationCount);
            LOGGER.debug("Genes: ");
            LOGGER.debug(myPop.getFittest());
            return true;
        }

        public CMemberSet<T> evolvePopulation(final CMemberSet<T> pop) {
            int elitismOffset;
            CMemberSet<T> newPopulation = new CMemberSet<>(pop.getPopulation().size(), false);

            if (elitism) {
                newPopulation.getPopulation().add(0, pop.getFittest());
                elitismOffset = 1;
            } else {
                elitismOffset = 0;
            }

            for (int i = elitismOffset; i < pop.getPopulation().size(); i++) {
                T indiv1 = tournamentSelection(pop);
                T indiv2 = tournamentSelection(pop);
                T newIndiv = crossover(indiv1, indiv2);
                newPopulation.getPopulation().add(i, newIndiv);
            }

            for (int i = elitismOffset; i < newPopulation.getPopulation().size(); i++) {
                mutate(newPopulation.getMember(i));
            }
            return newPopulation;
        }

        private T crossover(final T indiv1, final T indiv2) {
            final Class<? extends T> clazz = (Class<? extends T>) indiv1.getClass();
            final T newSol = CUtils.getInstance(clazz);
            for (int i = 0; i < newSol.getGenLength(); i++) {
                if (Math.random() <= uniformRate) {
                    newSol.setSingleGene(i, indiv1.getSingleGene(i));
                } else {
                    newSol.setSingleGene(i, indiv2.getSingleGene(i));
                }
            }
            return newSol;
        }

        private void mutate(final T indiv) {
            for (int i = 0; i < indiv.getGenLength(); i++) {
                if (Math.random() <= mutationRate) {
                    indiv.setSingleGene(i, CNumericUtils.generateRandomLong());
                }
            }
        }

        private T tournamentSelection(final CMemberSet<T> pop) {
            final CMemberSet<T> tournament = new CMemberSet<>(tournamentSize, false);
            for (int i = 0; i < tournamentSize; i++) {
                int randomId = (int) (Math.random() * pop.getPopulation().size());
                tournament.getPopulation().add(i, pop.getMember(randomId));
            }
            return tournament.getFittest();
        }

        protected static int getFitness(final CMember member) {
            int fitness = 0;
            for (int i = 0; i < member.getGenLength() && i < solution.length; i++) {
                if (member.getSingleGene(i) == solution[i]) {
                    fitness++;
                }
            }
            return fitness;
        }

        protected int getMaxFitness() {
            int maxFitness = solution.length;
            return maxFitness;
        }

        protected void setSolution(final String newSolution) {
            solution = new long[newSolution.length()];
            for (int i = 0; i < newSolution.length(); i++) {
                final String character = newSolution.substring(i, i + 1);
                if (character.contains("0") || character.contains("1")) {
                    solution[i] = Long.valueOf(character);
                } else {
                    solution[i] = 0;
                }
            }
        }
    }

    public static class CActorGenetics<T extends CActor> {

        /**
         * Default max number of iterations
         */
        private static final int DEFAULT_MAX_ITERATIONS = 1000;

        private static final double c = 1.0;
        private static final double alpha = 1;
        private static final double beta = 5;
        private static final double evaporation = 0.5;
        private static final double Q = 500;
        private static final double actorFactor = 0.8;
        private static final double randomFactor = 0.01;

        private final int numOfIterations;
        private final int numOfPlaces;
        private final int numberOfActors;
        private final double[][] graph;
        private final double[][] trails;
        private final List<T> actors = new ArrayList<>();
        private final double[] probabilities;

        private int currentIndex;
        private int[] bestTrailOrder;
        private double bestTrailLength;

        public CActorGenetics(int numOfPlaces) {
            this(numOfPlaces, CActorGenetics.DEFAULT_MAX_ITERATIONS);
        }

        public CActorGenetics(int numOfPlaces, int numOfIterations) {
            this(numOfPlaces, numOfIterations, 0, 100);
        }

        public CActorGenetics(int numOfPlaces, int numOfIterations, int lowerLimit, int upperLimit) {
            assert (numOfPlaces > 0);
            assert (lowerLimit >= 0 && upperLimit >= 0 && lowerLimit > upperLimit);

            this.numOfPlaces = numOfPlaces;
            this.numOfIterations = numOfIterations;

            this.graph = CMatrixUtils.generateRandomDoubleMatrix(numOfPlaces, lowerLimit, upperLimit);
            this.numberOfActors = (int) (numOfPlaces * CActorGenetics.actorFactor);
            this.trails = new double[numOfPlaces][numOfPlaces];
            this.probabilities = new double[numOfPlaces];
            IntStream.range(0, this.numberOfActors).forEach(i -> actors.add((T) new CActor(numOfPlaces)));
        }

        public void optimize(int numOfAttempts) {
            assert (numOfAttempts > 0);
            IntStream.rangeClosed(1, numOfAttempts)
                    .forEach(i -> {
                        LOGGER.debug("Attempt #" + i);
                        runTask();
                    });
        }

        public int[] runTask() {
            setup();
            clearTrails();
            IntStream.range(0, this.numOfIterations)
                    .forEach(i -> {
                        moveActors();
                        updateTrails();
                        updateBestTrail();
                    });
            LOGGER.debug("Best tour length: " + (this.bestTrailLength - this.numOfPlaces));
            LOGGER.debug("Best tour order: " + Arrays.toString(this.bestTrailOrder));
            return SerializationUtils.clone(this.bestTrailOrder);
        }

        private void setup() {
            IntStream.range(0, this.numberOfActors)
                    .forEach(i -> {
                        this.actors.forEach(actor -> {
                            actor.clear();
                            actor.visit(-1, CNumericUtils.generateRandomInt(0, this.numOfPlaces));
                        });
                    });
            this.currentIndex = 0;
        }

        private void moveActors() {
            IntStream.range(currentIndex, this.numOfPlaces - 1)
                    .forEach(i -> {
                        this.actors.forEach(actor -> actor.visit(this.currentIndex, getNextPlace(actor)));
                        this.currentIndex++;
                    });
        }

        private Integer getNextPlace(final T actor) {
            int t = CNumericUtils.generateRandomInt(0, this.numOfPlaces - this.currentIndex);
            if (CNumericUtils.generateRandomDouble() < CActorGenetics.randomFactor) {
                OptionalInt cityIndex = IntStream.range(0, this.numOfPlaces)
                        .filter(i -> i == t && !actor.visited(i))
                        .findFirst();
                if (cityIndex.isPresent()) {
                    return cityIndex.getAsInt();
                }
            }
            calculateProbabilities(actor);
            double r = CNumericUtils.generateRandomDouble();
            double total = 0;
            for (int i = 0; i < this.numOfPlaces; i++) {
                total += this.probabilities[i];
                if (total >= r) {
                    return i;
                }
            }
            return null;
        }

        public void calculateProbabilities(final T actor) {
            int i = actor.trail[this.currentIndex];
            double denominator = 0.0;
            for (int l = 0; l < this.numOfPlaces; l++) {
                if (!actor.visited(l)) {
                    denominator += Math.pow(this.trails[i][l], CActorGenetics.alpha) * Math.pow(1.0 / this.graph[i][l], CActorGenetics.beta);
                }
            }
            for (int j = 0; j < this.numOfPlaces; j++) {
                if (actor.visited(j)) {
                    this.probabilities[j] = 0.0;
                } else {
                    double numerator = Math.pow(this.trails[i][j], CActorGenetics.alpha) * Math.pow(1.0 / this.graph[i][j], CActorGenetics.beta);
                    this.probabilities[j] = numerator / denominator;
                }
            }
        }

        private void updateTrails() {
            for (int i = 0; i < this.numOfPlaces; i++) {
                for (int j = 0; j < this.numOfPlaces; j++) {
                    this.trails[i][j] *= CActorGenetics.evaporation;
                }
            }
            this.actors.stream().forEach((actor) -> {
                double contribution = CActorGenetics.Q / actor.trailLength(this.graph);
                for (int i = 0; i < this.numOfPlaces - 1; i++) {
                    this.trails[actor.trail[i]][actor.trail[i + 1]] += contribution;
                }
                this.trails[actor.trail[this.numOfPlaces - 1]][actor.trail[0]] += contribution;
            });
        }

        private void updateBestTrail() {
            if (Objects.isNull(this.bestTrailOrder)) {
                this.bestTrailOrder = this.actors.get(0).trail;
                this.bestTrailLength = this.actors.get(0).trailLength(this.graph);
            }
            this.actors.stream().filter((actor) -> (actor.trailLength(this.graph) < this.bestTrailLength)).map((actor) -> {
                this.bestTrailLength = actor.trailLength(this.graph);
                return actor;
            }).forEach((actor) -> {
                this.bestTrailOrder = SerializationUtils.clone(actor.trail);
            });
        }

        private void clearTrails() {
            IntStream.range(0, this.numOfPlaces)
                    .forEach(i -> {
                        IntStream.range(0, this.numOfPlaces)
                        .forEach(j -> this.trails[i][j] = CActorGenetics.c);
                    });
        }
    }

    public static class CTourGenetics<T extends CPlace> {

        /**
         * Default tour size
         */
        private static final int DEFAULT_TOUR_SIZE = 10;

        private CTour<T> travel;

        public CTourGenetics() {
            this(CTourGenetics.DEFAULT_TOUR_SIZE);
        }

        public CTourGenetics(int tourSize) {
            assert (tourSize > 0);
            this.travel = new CTour<>(tourSize);
        }

        public double runTask(int iterations, double temperature, double coolRate) {
            LOGGER.debug("Initial state: iterations=" + iterations + ", temperature=" + temperature + ", coolRate=" + coolRate);
            double t = temperature;
            double bestDistance = this.travel.getDistance();
            LOGGER.debug("Initial travel distance=" + bestDistance);

            final CTour<T> currentSolution = this.travel;
            for (int i = 0; i < iterations; i++) {
                if (t > 0.1) {
                    currentSolution.swapPlaces();
                    double currentDistance = currentSolution.getDistance();
                    if (currentDistance < bestDistance) {
                        bestDistance = currentDistance;
                    } else if (Math.exp((bestDistance - currentDistance) / t) < Math.random()) {
                        currentSolution.revertSwapPlaces();
                    }
                    t *= coolRate;
                }
            }
            return bestDistance;
        }
    }
}
