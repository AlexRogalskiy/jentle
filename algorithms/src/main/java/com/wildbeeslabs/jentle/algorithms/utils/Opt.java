package com.wildbeeslabs.jentle.algorithms.utils;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class for the Nullness Checker, providing every method in {@link java.util.Optional}, but
 * written for possibly-null references rather than for the {@code Optional} type.
 *
 * <p>To avoid the need to write the {@code Opt} class name at invocation sites, do:
 *
 * <pre>import static org.checkerframework.checker.nullness.Opt.orElse;</pre>
 * <p>
 * or
 *
 * <pre>import static org.checkerframework.checker.nullness.Opt.*;</pre>
 *
 * <p><b>Runtime Dependency</b>
 *
 * <p>Please note that using this class introduces a runtime dependency. This means that you need to
 * distribute (or link to) the Checker Framework, along with your binaries.
 *
 * <p>To eliminate this dependency, you can simply copy this class into your own project.
 *
 * @see java.util.Optional
 */
public final class Opt {

    private Opt() {
        throw new AssertionError("shouldn't be instantiated");
    }

    /**
     * If primary is non-null, returns it, otherwise throws NoSuchElementException.
     *
     * @see java.util.Optional#get()
     */
    public static <T> @lombok.NonNull T get(T primary) {
        if (primary == null) {
            throw new NoSuchElementException("No value present");
        }
        return primary;
    }

    /**
     * Returns true if primary is non-null, false if primary is null.
     *
     * @see java.util.Optional#isPresent()
     */
    public static boolean isPresent(final Object primary) {
        return primary != null;
    }

    /**
     * If primary is non-null, invoke the specified consumer with the value, otherwise do nothing.
     *
     * @see java.util.Optional#ifPresent(Consumer)
     */
    public static <T> void ifPresent(final T primary, final Consumer<@lombok.NonNull ? super @lombok.NonNull T> consumer) {
        if (primary != null) {
            consumer.accept(primary);
        }
    }

    /**
     * If primary is non-null, and its value matches the given predicate, return the value. If
     * primary is null or its non-null value does not match the predicate, return null.
     *
     * @see java.util.Optional#filter(Predicate)
     */
    public static <T> T filter(final T primary, Predicate<@lombok.NonNull ? super @lombok.NonNull T> predicate) {
        if (primary == null) {
            return null;
        } else {
            return predicate.test(primary) ? primary : null;
        }
    }

    /**
     * If primary is non-null, apply the provided mapping function to it and return the result. If
     * primary is null, return null.
     *
     * @see java.util.Optional#map(Function)
     */
    public static <T, U> U map(final T primary, Function<@lombok.NonNull ? super @lombok.NonNull T, ? extends U> mapper) {
        if (primary == null) {
            return null;
        } else {
            return mapper.apply(primary);
        }
    }

    // flatMap would have the same signature and implementation as map

    /**
     * Return primary if it is non-null. If primary is null, return other.
     *
     * @see java.util.Optional#orElse(Object)
     */
    public static <T> @lombok.NonNull T orElse(final T primary, final @lombok.NonNull T other) {
        return primary != null ? primary : other;
    }

    /**
     * Return primary if it is non-null. If primary is null, invoke {@code other} and return the
     * result of that invocation.
     *
     * @see java.util.Optional#orElseGet(Supplier)
     */
    public static <T> @lombok.NonNull T orElseGet(final T primary, final Supplier<? extends @lombok.NonNull T> other) {
        return primary != null ? primary : other.get();
    }

    /**
     * Return primary if it is non-null. If primary is null, return an exception to be created by
     * the provided supplier.
     *
     * @see java.util.Optional#orElseThrow(Supplier)
     */
    public static <T, X extends @lombok.NonNull Throwable> @lombok.NonNull T orElseThrow(final T primary, final Supplier<? extends @lombok.NonNull X> exceptionSupplier) throws X {
        if (primary != null) {
            return primary;
        } else {
            throw exceptionSupplier.get();
        }
    }
}
