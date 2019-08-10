package com.wildbeeslabs.jentle.algorithms.number;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cyclops.async.LazyReact;
import cyclops.control.Trampoline;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cyclops.control.Trampoline.done;
import static cyclops.control.Trampoline.more;

public class Main {

    public BigInteger fib(final BigInteger n) {
        return fibonacci(n, BigInteger.ZERO, BigInteger.ONE).get();
    }

    public Trampoline<BigInteger> fibonacci(final BigInteger count, final BigInteger a, final BigInteger b) {
        return count.equals(BigInteger.ZERO) ? done(a) : more(() -> fibonacci(count.subtract(BigInteger.ONE), b, a.add(b)));
    }

    public void memoization(final List<Integer> array) {
        final Cache<BigInteger, BigInteger> cache = CacheBuilder.newBuilder()
            .maximumSize(1_000_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
        final LazyReact react = new LazyReact().autoMemoizeOn((key, fn) -> cache.get((BigInteger) key, () -> (BigInteger) fn.apply((BigInteger) key)));
        final List<Trampoline<BigInteger>> result = react.from(array)
            .map(i -> fibonacci(BigInteger.valueOf(i), BigInteger.ZERO, BigInteger.ONE))
            .toList();
    }

    public static void main(final String[] args) {
        final Main main = new Main();
        final List<Integer> array = Arrays.asList(500_000, 499_999);
        long start = System.currentTimeMillis();

        array.stream().map(BigInteger::valueOf).forEach(x -> main.fib(x));
        System.out.println("Regular version took " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        main.memoization(array);
        System.out.println("Memoized version took " + (System.currentTimeMillis() - start) + " ms");
    }
}
