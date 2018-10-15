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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 *
 * Custom individual model
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
public class CIndividual {

    private static final int DEFAULT_GENE_LENGTH = 64;
    @Setter(AccessLevel.NONE)
    private final byte[] genes;
    private int fitness = 0;

    public CIndividual() {
        this(DEFAULT_GENE_LENGTH);
    }

    public CIndividual(int geneLength) {
        assert (geneLength > 0);
        this.genes = new byte[geneLength];
        for (int i = 0; i < this.genes.length; i++) {
            byte gene = (byte) Math.round(Math.random());
            this.genes[i] = gene;
        }
    }

    protected byte getSingleGene(int index) {
        return this.genes[index];
    }

    protected void setSingleGene(int index, byte value) {
        this.genes[index] = value;
        this.fitness = 0;
    }

    public int getDefaultGeneLength() {
        return this.genes.length;
    }

    @Override
    public String toString() {
        final StringBuffer geneString = new StringBuffer();
        for (int i = 0; i < this.genes.length; i++) {
            geneString.append(getSingleGene(i));
        }
        return geneString.toString();
    }
}
