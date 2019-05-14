package com.wildbeeslabs.jentle.algorithms.utils;

import com.codepoetics.protonpack.Streamable;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Utility methods to work with {@link Optional}s.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@UtilityClass
public class COptionalUtils {

    /**
     * Returns whether any of the given {@link Optional}s is present.
     *
     * @param optionals must not be {@literal null}.
     * @return true - if all of the given {@link Optional}s is present, false - otherwise
     */
    public static boolean isAnyPresent(final Optional<?>... optionals) {
        Objects.requireNonNull(optionals, "Optionals must not be null!");
        return Arrays.stream(optionals).anyMatch(Optional::isPresent);
    }

    /**
     * Returns whether all of the given {@link Optional}s is present.
     *
     * @param optionals must not be {@literal null}.
     * @return true - if all of the given {@link Optional}s is present, false - otherwise
     */
    public static boolean isAllPresent(final Optional<?>... optionals) {
        Objects.requireNonNull(optionals, "Optionals must not be null!");
        return Arrays.stream(optionals).allMatch(Optional::isPresent);
    }

    /**
     * Turns the given {@link Optional} into a one-element {@link Stream} or an empty one if not present.
     *
     * @param optionals must not be {@literal null}.
     * @return
     */
    @SafeVarargs
    public static <T> Stream<T> toStream(final Optional<? extends T>... optionals) {
        Objects.requireNonNull(optionals, "Optional must not be null!");
        return Arrays.asList(optionals).stream().flatMap(it -> it.map(Stream::of).orElseGet(Stream::empty));
    }

    /**
     * Applies the given function to the elements of the source and returns the first non-empty result.
     *
     * @param source   - initial input {@link Iterable} source
     * @param function - initial input {@link Function} operator
     * @return first non-empty {@link Optional} result
     */
    public static <S, T> Optional<T> firstNonEmpty(final Iterable<S> source, final Function<S, Optional<T>> function) {
        Objects.requireNonNull(source, "Source must not be null!");
        Objects.requireNonNull(function, "Function must not be null!");
        return Streamable.of(source).stream()
            .map(function::apply)
            .filter(Optional::isPresent)
            .findFirst()
            .orElseGet(Optional::empty);
    }

    /**
     * Applies the given function to the elements of the source and returns the first non-empty result.
     *
     * @param source       - initial input {@link Iterable} source
     * @param function     - initial input {@link Function} operator
     * @param defaultValue - initial input defaut {@code T} value
     * @return first non-empty {@code T} result
     */
    public static <S, T> T firstNonEmpty(final Iterable<S> source, final Function<S, T> function, final T defaultValue) {
        Objects.requireNonNull(source, "Source must not be null!");
        Objects.requireNonNull(function, "Function must not be null!");
        return Streamable.of(source).stream()
            .map(function::apply)
            .filter(it -> !it.equals(defaultValue))
            .findFirst()
            .orElse(defaultValue);
    }

    /**
     * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
     *
     * @param suppliers must not be {@literal null}.
     * @return
     */
    @SafeVarargs
    public static <T> Optional<T> firstNonEmpty(final Supplier<Optional<T>>... suppliers) {
        Objects.requireNonNull(suppliers, "Suppliers must not be null!");
        return firstNonEmpty(Streamable.of(suppliers).toList());
    }

    /**
     * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
     *
     * @param suppliers must not be {@literal null}.
     * @return
     */
    public static <T> Optional<T> firstNonEmpty(final Iterable<Supplier<Optional<T>>> suppliers) {
        Objects.requireNonNull(suppliers, "Suppliers must not be null!");
        return Streamable.of(suppliers).stream()
            .map(Supplier::get)
            .filter(Optional::isPresent)
            .findFirst()
            .orElse(Optional.empty());
    }

    /**
     * Returns the next element of the given {@link Iterator} or {@link Optional#empty()} in case there is no next
     * element.
     *
     * @param iterator must not be {@literal null}.
     * @return
     */
    public static <T> Optional<T> next(final Iterator<T> iterator) {
        Objects.requireNonNull(iterator, "Iterator must not be null!");
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    /**
     * Returns {@link CKeyValueNode} if both {@link Optional} instances have values or {@link Optional#empty()} if one or both
     * are missing.
     *
     * @param left  - initial input left {@link Optional}
     * @param right - initial input right {@link Optional}
     * @return {@link Optional} of {@link CKeyValueNode}
     */
    public static <T, S> Optional<CKeyValueNode<T, S>> withBoth(final Optional<T> left, final Optional<S> right) {
        return left.flatMap(l -> right.map(r -> CKeyValueNode.of(l, r)));
    }

    /**
     * Invokes the given {@link BiConsumer} if all given {@link Optional} are present.
     *
     * @param left     - initial input left {@link Optional}
     * @param right    - initial input right {@link Optional}
     * @param consumer - initial input {@link BiConsumer} operator
     */
    public static <T, S> void ifAllPresent(final Optional<T> left, final Optional<S> right, final BiConsumer<T, S> consumer) {
        Objects.requireNonNull(left, "Optional left must not be null!");
        Objects.requireNonNull(right, "Optional right must not be null!");
        Objects.requireNonNull(consumer, "Consumer must not be null!");
        mapIfAllPresent(left, right, (l, r) -> {
            consumer.accept(l, r);
            return null;
        });
    }

    /**
     * Maps the values contained in the given {@link Optional} if both of them are present.
     *
     * @param left     - initial input left {@link Optional} value
     * @param right    - initial input right {@link Optional} value
     * @param function - initial input {@link BiFunction} operator
     * @return {@link Optional} of both items
     */
    public static <T, S, R> Optional<R> mapIfAllPresent(final Optional<T> left, final Optional<S> right, final BiFunction<T, S, R> function) {
        Objects.requireNonNull(left, "Optional left must not be null!");
        Objects.requireNonNull(right, "Optional right must not be null!");
        Objects.requireNonNull(function, "BiFunction must not be null!");
        return left.flatMap(l -> right.map(r -> function.apply(l, r)));
    }

    /**
     * Invokes the given {@link Consumer} if the {@link Optional} is present or the {@link Runnable} if not.
     *
     * @param optional - initial input {@link Optional} value
     * @param consumer - initial input {@link Consumer} operator
     * @param runnable - initial input {@link Runnable} operator
     */
    public static <T> void ifPresentOrElse(final Optional<T> optional, final Consumer<? super T> consumer, final Runnable runnable) {
        Objects.requireNonNull(optional, "Optional must not be null!");
        Objects.requireNonNull(consumer, "Consumer must not be null!");
        Objects.requireNonNull(runnable, "Runnable must not be null!");

        if (optional.isPresent()) {
            optional.ifPresent(consumer);
        } else {
            runnable.run();
        }
    }
}
