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

import io.jenetics.EnumGene;
import io.jenetics.Optimize;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.Phenotype;
import io.jenetics.SwapMutator;
import io.jenetics.engine.Engine;
import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import io.jenetics.engine.EvolutionStatistics;
import static io.jenetics.engine.Limits.bySteadyFitness;
import io.jenetics.engine.Codecs;

import java.util.stream.IntStream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom salesman implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CSalesman {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CSalesman.class);

    /**
     * Default number of places to visit
     */
    private static final int DEFAULT_PLACE_NUMBER = 50;
    /**
     * Default place radius distance
     */
    private static final double DEFAULT_PLACE_RADIUS = 50.0;

    private final double[][] adjacents;
    private final int numOfPlaces;

    public CSalesman() {
        this(CSalesman.DEFAULT_PLACE_NUMBER, CSalesman.DEFAULT_PLACE_RADIUS);
    }

    public CSalesman(int numOfPlaces, double radius) {
        assert (numOfPlaces > 0);
        assert (radius > 0);
        this.numOfPlaces = numOfPlaces;
        this.adjacents = this.generateMatrix(numOfPlaces, radius);
    }

    private double[][] generateMatrix(int numOfPlaces, double radius) {
        double[][] matrix = new double[numOfPlaces][numOfPlaces];
        for (int i = 0; i < numOfPlaces; i++) {
            for (int j = 0; j < numOfPlaces; j++) {
                matrix[i][j] = this.chord(numOfPlaces, Math.abs(i - j), radius);
            }
        }
        return matrix;
    }

    private double chord(int numOfPlaces, int i, double r) {
        return (2.0 * r * Math.abs(Math.sin(Math.PI * i / numOfPlaces)));
    }

    private double distance(final int[] path) {
        return IntStream.range(0, this.numOfPlaces)
                .mapToDouble(i -> adjacents[path[i]][path[(i + 1) % this.numOfPlaces]])
                .sum();
    }

    public static void main(final String[] args) {
        final CSalesman data = new CSalesman();
        final Engine<EnumGene<Integer>, Double> engine = Engine.builder((path) -> data.distance(path), Codecs.ofPermutation(data.numOfPlaces))
                .optimize(Optimize.MINIMUM)
                .maximalPhenotypeAge(11)
                .populationSize(500)
                .alterers(new SwapMutator<>(0.2), new PartiallyMatchedCrossover<>(0.35))
                .build();

        final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<EnumGene<Integer>, Double> best = engine.stream()
                .limit(bySteadyFitness(15))
                .limit(250)
                .peek(statistics)
                .collect(toBestPhenotype());

        LOGGER.debug("Evolution statistics: " + statistics);
        LOGGER.debug("Best phenotype" + best);
    }
}
