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

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

import java.util.function.Function;
import java.util.stream.Collector;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom bag wrapper implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode
@ToString
public class CBagWrapper<T extends CBagItem> implements Function<Genotype<BitGene>, Double> {

    protected T[] items;
    protected double size;

    public CBagWrapper(final T[] items, double size) {
        this.items = items;
        this.size = size;
    }

    @Override
    public Double apply(final Genotype<BitGene> gt) {
        final CBagItem sum = ((BitChromosome) gt.getChromosome()).ones()
                .mapToObj(i -> items[i])
                .collect(toSum());
        return sum.size <= this.size ? sum.value : 0.0;
    }

    protected Collector<CBagItem, ?, CBagItem> toSum() {
        return Collector.of(() -> new double[2], (a, b) -> {
            a[0] += b.size;
            a[1] += b.value;
        }, (a, b) -> {
            a[0] += b[0];
            a[1] += b[1];
            return a;
        }, r -> new CBagItem(r[0], r[1]));
    }
}
