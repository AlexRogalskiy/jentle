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
package com.wildbeeslabs.jentle.algorithms.genetics.knapsack;

import com.wildbeeslabs.jentle.algorithms.random.CRandom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom member implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CMember {

    /**
     * Default gen length
     */
    protected static final Integer DEFAULT_GEN_LENGTH = 64;

    protected int fitness;
    protected final long[] gens;

    public CMember() {
        this(CMember.DEFAULT_GEN_LENGTH);
    }

    public CMember(int genLength) {
        assert (genLength > 0);
        this.fitness = 0;
        this.gens = new long[genLength];
        for (int i = 0; i < genLength; i++) {
            this.gens[i] = CRandom.generateRandomLong();
        }
    }

    protected long getSingleGene(int index) {
        assert (index >= 0);
        return this.gens[index];
    }

    protected void setSingleGene(int index, long value) {
        assert (index >= 0);
        this.gens[index] = value;
        this.fitness = 0;
    }

    public int getFitness() {
        if (this.fitness == 0) {
            this.fitness = CGenetics.CMemberGenetics.getFitness(this);
        }
        return this.fitness;
    }

    public int getGenLength() {
        return this.gens.length;
    }

    public String toFormatString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.gens.length; i++) {
            sb.append(this.getSingleGene(i));
        }
        return sb.toString();
    }
}
