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
@EqualsAndHashCode
@ToString
public class CSalesman {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CSalesman.class);

    /**
     * Default number of places to visit
     */
    protected static final int DEFAULT_PLACE_NUMBER = 50;
    /**
     * Default place radius distance
     */
    protected static final double DEFAULT_PLACE_RADIUS = 50.0;

    protected final double[][] adjacents;
    protected final int numOfPlaces;

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

    protected double distance(final int[] path) {
        return IntStream.range(0, this.numOfPlaces)
                .mapToDouble(i -> adjacents[path[i]][path[(i + 1) % this.numOfPlaces]])
                .sum();
    }
}
