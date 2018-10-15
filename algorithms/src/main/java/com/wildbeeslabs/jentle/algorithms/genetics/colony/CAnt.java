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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * Custom ant model
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CAnt {

    protected int trailSize;
    protected int trail[];
    protected boolean visited[];

    public CAnt(int tourSize) {
        this.trailSize = tourSize;
        this.trail = new int[tourSize];
        this.visited = new boolean[tourSize];
    }

    protected void visitCity(int currentIndex, int city) {
        this.trail[currentIndex + 1] = city;
        this.visited[city] = true;
    }

    protected boolean visited(int i) {
        return this.visited[i];
    }

    protected double trailLength(double graph[][]) {
        double length = graph[this.trail[this.trailSize - 1]][this.trail[0]];
        for (int i = 0; i < this.trailSize - 1; i++) {
            length += graph[this.trail[i]][this.trail[i + 1]];
        }
        return length;
    }

    protected void clear() {
        for (int i = 0; i < this.trailSize; i++) {
            this.visited[i] = false;
        }
    }
}
