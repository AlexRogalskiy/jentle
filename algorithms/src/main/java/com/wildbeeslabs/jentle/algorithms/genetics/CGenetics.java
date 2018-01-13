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

import com.wildbeeslabs.jentle.algorithms.utils.CNumericUtils;

import lombok.Data;

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
    public static class SimpleGenetics<T extends CMember> {

        private static final double uniformRate = 0.5;
        private static final double mutationRate = 0.025;
        private static final int tournamentSize = 5;
        private static final boolean elitism = true;
        private static long[] solution;

        public boolean run(int populationSize, final String solution) {
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
            final T newSol = this.getInstance(clazz);
            for (int i = 0; i < newSol.getGenLength(); i++) {
                if (Math.random() <= uniformRate) {
                    newSol.setSingleGene(i, indiv1.getSingleGene(i));
                } else {
                    newSol.setSingleGene(i, indiv2.getSingleGene(i));
                }
            }
            return newSol;
        }

        private T getInstance(final Class<? extends T> clazz) {
            try {
                return (T) clazz.getComponentType().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.error("ERROR: cannot initialize class instance, message=" + ex.getMessage());
            }
            return null;
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

        protected static int getFitness(final CMember individual) {
            int fitness = 0;
            for (int i = 0; i < individual.getGenLength() && i < solution.length; i++) {
                if (individual.getSingleGene(i) == solution[i]) {
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
}
