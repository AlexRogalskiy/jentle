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
package com.wildbeeslabs.jentle.algorithms.genetics.binary;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom simple genetic algorithm implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public final class CSimpleGeneticAlgorithm {

    private final double uniformRate = 0.5;
    private final double mutationRate = 0.025;
    private final int tournamentSize = 5;
    private final boolean elitism = true;
    private byte[] solution = new byte[64];

    public boolean runAlgorithm(int populationSize, final String solution) {
        if (solution.length() != this.solution.length) {
            throw new RuntimeException("The solution needs to have " + this.solution.length + " bytes");
        }
        setSolution(solution);
        CPopulation myPop = new CPopulation(populationSize, true);

        int generationCount = 1, fitness = 0;
        updatePopulationFitness(myPop);
        while ((fitness = myPop.getFittest().getFitness()) < getMaxFitness()) {
            log.debug("Generation: " + generationCount + " Correct genes found: " + fitness);
            myPop = evolvePopulation(myPop);
            updatePopulationFitness(myPop);
            generationCount++;
        }
        log.debug("Solution found!");
        log.debug("Generation: " + generationCount);
        log.debug("Genes: ");
        log.debug(myPop.getFittest().toString());
        return true;
    }

    public CPopulation evolvePopulation(final CPopulation pop) {
        int elitismOffset;
        final CPopulation newPopulation = new CPopulation(pop.getIndividuals().size(), false);

        if (this.elitism) {
            newPopulation.getIndividuals().add(0, pop.getFittest());
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < pop.getIndividuals().size(); i++) {
            CIndividual indiv1 = tournamentSelection(pop);
            CIndividual indiv2 = tournamentSelection(pop);
            CIndividual newIndiv = crossover(indiv1, indiv2);
            newPopulation.getIndividuals().add(i, newIndiv);
        }

        for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    private CIndividual crossover(final CIndividual indiv1, final CIndividual indiv2) {
        final CIndividual newSol = new CIndividual();
        for (int i = 0; i < newSol.getDefaultGeneLength(); i++) {
            if (Math.random() <= this.uniformRate) {
                newSol.setSingleGene(i, indiv1.getSingleGene(i));
            } else {
                newSol.setSingleGene(i, indiv2.getSingleGene(i));
            }
        }
        return newSol;
    }

    private void mutate(final CIndividual indiv) {
        for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
            if (Math.random() <= this.mutationRate) {
                byte gene = (byte) Math.round(Math.random());
                indiv.setSingleGene(i, gene);
            }
        }
    }

    private CIndividual tournamentSelection(final CPopulation pop) {
        final CPopulation tournament = new CPopulation(this.tournamentSize, false);
        for (int i = 0; i < this.tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getIndividuals().size());
            tournament.getIndividuals().add(i, pop.getIndividual(randomId));
        }
        return tournament.getFittest();
    }

    protected void updatePopulationFitness(final CPopulation myPop) {
        myPop.getIndividuals().stream().filter((individual) -> (individual.getFitness() == 0)).forEach((individual) -> {
            individual.setFitness(this.calculateFitness(individual));
        });
    }

    protected int calculateFitness(final CIndividual individual) {
        int fitness = 0;
        for (int i = 0; i < individual.getDefaultGeneLength() && i < this.solution.length; i++) {
            if (individual.getSingleGene(i) == this.solution[i]) {
                fitness++;
            }
        }
        return fitness;
    }

    protected int getMaxFitness() {
        return this.solution.length;
    }

    protected void setSolution(final String newSolution) {
        this.solution = new byte[newSolution.length()];
        for (int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if (character.contains("0") || character.contains("1")) {
                this.solution[i] = Byte.parseByte(character);
            } else {
                this.solution[i] = 0;
            }
        }
    }
}
