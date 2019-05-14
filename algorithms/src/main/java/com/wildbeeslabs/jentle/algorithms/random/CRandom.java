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
package com.wildbeeslabs.jentle.algorithms.random;

import lombok.NonNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.*;

/**
 * Custom random utilities implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CRandom {

    /**
     * Default Random instance
     */
    private static final Random DEFAULT_RANDOM_INSTANCE = new Random();

    public static long generateRandomLong() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextLong();
        //ThreadLocalRandom.current().nextLong();
        //new RandomDataGenerator().getRandomGenerator().nextLong();
    }

    public static int generateRandomInt() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextInt();
        //ThreadLocalRandom.current().nextInt();
        //new RandomDataGenerator().getRandomGenerator().nextInt();
    }

    public static float generateRandomFloat() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat();
        //ThreadLocalRandom.current().nextFloat();
        //new RandomDataGenerator().getRandomGenerator().nextFloat();
    }

    public static double generateRandomDouble() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble();
        //ThreadLocalRandom.current().nextDouble();
        //new RandomDataGenerator().getRandomGenerator().nextDouble();
    }

    public static List<Double> generateRandomDouble(int limit) {
        return CRandom.generateRandomDouble(limit, new Random()::nextDouble);
    }

    public static List<Double> generateRandomDouble(int limit, final DoubleSupplier supplier) {
        final DoubleStream stream = DoubleStream.generate(supplier);
        return stream.boxed().limit(limit).collect(Collectors.toList());
    }

    public static List<Double> generateDouble(double seed, int limit, final DoubleUnaryOperator operator) {
        final DoubleStream stream = DoubleStream.iterate(seed, operator);
        return stream.boxed().limit(limit).collect(Collectors.toList());
    }

    public static <T> List<Double> generateDouble(@NonNull final Stream<T> stream, int limit, final ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper).boxed().limit(limit).collect(Collectors.toList());
    }

    public static List<Integer> generateInt(int seed, int limit, final IntUnaryOperator operator) {
        final IntStream stream = IntStream.iterate(seed, operator);
        return stream.boxed().limit(limit).collect(Collectors.toList());
    }

    public static <T> List<Integer> generateInt(@NonNull final Stream<T> stream, int limit, final ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper).boxed().limit(limit).collect(Collectors.toList());
    }

    public static <T> List<Long> generateLong(@NonNull final Stream<T> stream, int limit, final ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper).boxed().limit(limit).collect(Collectors.toList());
    }

    public static <U> List<U> generateLong(@NonNull final DoubleStream stream, int limit, final DoubleFunction<? extends U> mapper) {
        return stream.mapToObj(mapper).limit(limit).collect(Collectors.toList());
    }

    public static List<Long> generateLong(long seed, int limit, final LongUnaryOperator operator) {
        final LongStream stream = LongStream.iterate(seed, operator);
        return stream.boxed().limit(limit).collect(Collectors.toList());
    }

    public static long generateRandomLong(long topLimit) {
        return generateRandomLong(0, topLimit);
    }

    public static long generateRandomLong(long bottomLimit, long topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + (long) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit)));
        //ThreadLocalRandom.current().nextLong(bottomLimit, topLimit);
        //new RandomDataGenerator().nextLong(bottomLimit, topLimit);
    }

    public static int generateRandomInt(int topLimit) {
        return generateRandomInt(0, topLimit);
    }

    public static int generateRandomInt(int bottomLimit, int topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + (int) (CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit)));
        //ThreadLocalRandom.current().nextInt(bottomLimit, topLimit);
        //new RandomDataGenerator().nextInt(bottomLimit, topLimit);
    }

    public static List<Integer> generateRandomInt(int limit, final IntSupplier supplier) {
        final IntStream stream = IntStream.generate(supplier);
        return stream.boxed().limit(limit).collect(Collectors.toList());
    }

    public static float generateRandomFloat(float topLimit) {
        return generateRandomFloat(0f, topLimit);
    }

    public static float generateRandomFloat(float bottomLimit, float topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextFloat() * (topLimit - bottomLimit));
        //float randomFloat = new RandomDataGenerator().getRandomGenerator().nextFloat();
        //bottomLimit + randomFloat * (upLimit - bottomLimit);
    }

    public static double generateRandomDouble(double topLimit) {
        return generateRandomDouble(0d, topLimit);
    }

    public static double generateRandomDouble(double bottomLimit, double topLimit) {
        assert (bottomLimit <= topLimit);
        return (bottomLimit + CRandom.DEFAULT_RANDOM_INSTANCE.nextDouble() * (topLimit - bottomLimit));
        //ThreadLocalRandom.current().nextDouble(upLimit, topLimit);
        //new RandomDataGenerator().nextUniform(bottomLimit, upLimit);
    }

    public static boolean generateRandomBoolean() {
        return CRandom.DEFAULT_RANDOM_INSTANCE.nextBoolean();
        //ThreadLocalRandom.current().nextBoolean();
        //new RandomDataGenerator().getRandomGenerator().nextBoolean();
    }

    public static double generateRandomGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }

    public static List<Integer> generateRandomIntsInRange(int bottomLimit, int topLimit) {
        return ThreadLocalRandom.current().ints(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }

    public static List<Double> generateRandomDoublesInRange(double bottomLimit, double topLimit) {
        return ThreadLocalRandom.current().doubles(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }

    public static List<Long> generateRandomLongsInRange(long bottomLimit, long topLimit) {
        return ThreadLocalRandom.current().longs(bottomLimit, topLimit).distinct().boxed().collect(Collectors.toList());
    }

    public static int generate(int min, int max) {
        return new Random().ints(min, (max + 1)).findFirst().getAsInt();
    }

    public static int[] generateIntArray(int bound, long maxSize) {
        assert bound > 0 : "Bound should be greater than zero";
        assert maxSize > 0 : "Max size should be greater than zero";
        return IntStream.generate(() -> new Random().nextInt(bound)).limit(maxSize).toArray();
    }

    public static long[] generateLongArray(long maxSize) {
        assert maxSize > 0 : "Max size should be greater than zero";
        return LongStream.generate(() -> new Random().nextLong()).limit(maxSize).toArray();
    }

    public static double[] generateDoubleArray(long maxSize) {
        assert maxSize > 0 : "Max size should be greater than zero";
        return DoubleStream.generate(() -> new Random().nextDouble()).limit(maxSize).toArray();
    }
}
