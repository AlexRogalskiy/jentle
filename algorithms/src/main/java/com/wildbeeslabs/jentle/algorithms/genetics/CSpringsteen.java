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

import io.jenetics.BitGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom Springsteen algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CSpringsteen implements Problem<ISeq<CSpringsteenItem>, BitGene, Double> {

    private ISeq<CSpringsteenItem> records;
    private double maxPricePerUniqueSong;

    public CSpringsteen(final ISeq<CSpringsteenItem> records, double maxPricePerUniqueSong) {
        Objects.requireNonNull(records);
        this.records = records;
        this.maxPricePerUniqueSong = maxPricePerUniqueSong;
    }

    @Override
    public Function<ISeq<CSpringsteenItem>, Double> fitness() {
        return SpringsteenRecords -> {
            double cost = SpringsteenRecords.stream()
                    .mapToDouble(r -> r.price)
                    .sum();

            int uniqueSongCount = SpringsteenRecords.stream()
                    .flatMap(r -> r.songs.stream())
                    .collect(Collectors.toSet())
                    .size();

            double pricePerUniqueSong = cost / uniqueSongCount;
            return pricePerUniqueSong <= maxPricePerUniqueSong ? uniqueSongCount : 0.0;
        };
    }

    @Override
    public Codec<ISeq<CSpringsteenItem>, BitGene> codec() {
        return Codecs.ofSubSet(records);
    }
}
