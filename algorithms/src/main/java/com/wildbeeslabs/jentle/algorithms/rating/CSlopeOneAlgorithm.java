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
package com.wildbeeslabs.jentle.algorithms.rating;

import com.wildbeeslabs.jentle.algorithms.format.CNumberFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Custom slope one algorithm implementation
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
public final class CSlopeOneAlgorithm<T extends CDataItem, E extends CActorItem> {

    private final List<T> items = new ArrayList<>();
    private final Map<T, Map<T, Double>> diff = new HashMap<>();
    private final Map<T, Map<T, Integer>> freq = new HashMap<>();
    private final Map<E, Map<T, Double>> inputData = new HashMap<>();
    private final Map<E, Map<T, Double>> outputData = new HashMap<>();

    public CSlopeOneAlgorithm(int numberOfActors, final List<T> items) {
        this.items.addAll(items);
        this.inputData.putAll(initializeData(numberOfActors, items));
    }

    public void slopeOne() {
        log.debug("Slope One - Before the Prediction");
        buildDifferencesMatrix(this.inputData);
        log.debug("\nSlope One - With Predictions");
        predict(this.inputData);
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data existing user data and their items' ratings
     */
    private void buildDifferencesMatrix(final Map<E, Map<T, Double>> data) {
        for (final Map<T, Double> user : data.values()) {
            for (final Map.Entry<T, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }
                user.entrySet().stream().forEach((e2) -> {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                });
            }
        }
        diff.keySet().stream().forEach((T j) -> {
            diff.get(j).keySet().stream().forEach((i) -> {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            });
        });
        printData(data);
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data existing user data and their items' ratings
     */
    private void predict(final Map<E, Map<T, Double>> data) {
        final Map<T, Double> uPred = new HashMap<>();
        final Map<T, Integer> uFreq = new HashMap<>();
        for (final T j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (final Map.Entry<E, Map<T, Double>> e : data.entrySet()) {
            for (final T j : e.getValue().keySet()) {
                for (final T k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            final Map<T, Double> clean = new HashMap<>();
            for (final T j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            this.items.stream().forEach((j) -> {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else {
                    clean.put(j, -1.0);
                }
            });
            outputData.put(e.getKey(), clean);
        }
        printData(outputData);
    }

    private void printData(final Map<E, Map<T, Double>> data) {
        for (final E user : data.keySet()) {
            log.debug(user.getName() + ":");
            print(data.get(user));
        }
    }

    private void print(final Map<T, Double> hashMap) {
        hashMap.keySet().stream().forEach((j) -> {
            log.debug(StringUtils.SPACE + j.getName() + " --> " + CNumberFormatter.formatByPattern(hashMap.get(j).doubleValue(), "#0.000"));
        });
    }

    private Map<E, Map<T, Double>> initializeData(int numberOfUsers, final List<T> items) {
        final Map<E, Map<T, Double>> data = new HashMap<>();
        Map<T, Double> newUser;
        Set<T> newRecommendationSet;
        for (int i = 0; i < numberOfUsers; i++) {
            newUser = new HashMap<>();
            newRecommendationSet = new HashSet<>();
            for (int j = 0; j < 3; j++) {
                newRecommendationSet.add(items.get((int) (Math.random() * 5)));
            }
            for (final T item : newRecommendationSet) {
                newUser.put(item, Math.random());
            }
            data.put((E) CActorItem.of("Actor " + i), newUser);
        }
        return data;
    }
}
