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
package com.wildbeeslabs.jentle.algorithms.genetics.annealing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom travel model
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CTravel {

    private static final int DEFAULT_CITY_NUMBER = 10;

    private List<CCity> travel = new ArrayList<>();
    private List<CCity> previousTravel = new ArrayList<>();

    public CTravel(int numberOfCities) {
        assert (numberOfCities > 0);
        initialize(numberOfCities);
    }

    public void generateInitialTravel() {
        if (this.travel.isEmpty()) {
            initialize(DEFAULT_CITY_NUMBER);
        }
        Collections.shuffle(this.travel);
    }

    private void initialize(int numberOfCities) {
        for (int i = 0; i < numberOfCities; i++) {
            this.travel.add(new CCity());
        }
    }

    public void swapCities() {
        int a = generateRandomIndex();
        int b = generateRandomIndex();
        this.previousTravel = this.travel;
        CCity x = this.travel.get(a);
        CCity y = this.travel.get(b);
        this.travel.set(a, y);
        this.travel.set(b, x);
    }

    public void revertSwap() {
        this.travel = this.previousTravel;
    }

    private int generateRandomIndex() {
        return (int) (Math.random() * this.travel.size());
    }

    public CCity getCity(int index) {
        return this.travel.get(index);
    }

    public int getDistance() {
        int distance = 0;
        for (int index = 0; index < this.travel.size(); index++) {
            CCity starting = getCity(index);
            CCity destination = null;
            if (index + 1 < travel.size()) {
                destination = getCity(index + 1);
            } else {
                destination = getCity(0);
            }
            distance += starting.distanceToCity(destination);
        }
        return distance;
    }
}
