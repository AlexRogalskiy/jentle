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
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom tour implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CTour<T extends CPlace> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Default travel list
     */
    protected List<T> travel = new ArrayList<>();
    /**
     * Default previous travel list
     */
    protected List<T> previousTravel = new ArrayList<>();

    public CTour(int numOfPlaces) {
        this.generateInitialTravel(numOfPlaces);
    }

    public void generateInitialTravel(int num) {
        if (this.travel.isEmpty()) {
            final Class<? extends T> clazz = (Class<? extends T>) CPlace.class;
            for (int i = 0; i < num; i++) {
                this.travel.add((T) CUtils.getInstance(clazz));
            }
        }
        Collections.shuffle(this.travel);
    }

    public void swapPlaces() {
        int a = CNumericUtils.generateRandomInt(0, this.travel.size());
        int b = CNumericUtils.generateRandomInt(0, this.travel.size());
        this.previousTravel = this.travel;
        final T x = this.travel.get(a);
        final T y = this.travel.get(b);
        this.travel.set(a, y);
        this.travel.set(b, x);
    }

    public void revertSwapPlaces() {
        this.travel = this.previousTravel;
    }

    public T getPlace(int index) {
        return this.travel.get(index);
    }

    public int getDistance() {
        int distance = 0;
        for (int index = 0; index < this.travel.size(); index++) {
            final T starting = this.getPlace(index);
            T destination;
            if (index + 1 < this.travel.size()) {
                destination = this.getPlace(index + 1);
            } else {
                destination = this.getPlace(0);
            }
            distance += starting.distance(destination);
        }
        return distance;
    }

    public double getBestDistance(int iterations, double temperature, double coolRate) {
        LOGGER.debug("Initial state: iterations=" + iterations + ", temperature=" + temperature + ", coolRate=" + coolRate);
        double t = temperature;
        double bestDistance = this.getDistance();
        LOGGER.debug("Initial travel distance=" + bestDistance);

        final CTour<T> currentSolution = this;
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
