package com.wildbeeslabs.jentle.algorithms.interfaces;

/**
 * {@code ThrowingSupplier} is a functional interface that can be used to
 * implement any generic block of code that returns an object and
 * potentially throws a {@link Throwable}.
 *
 * <p>The {@code ThrowingSupplier} interface is similar to
 * {@link java.util.function.Supplier}, except that a {@code ThrowingSupplier}
 * can throw any kind of exception, including checked exceptions.
 *
 * <h4>Rationale for throwing {@code Throwable} instead of {@code Exception}</h4>
 *
 * <p>Although Java applications typically throw exceptions that are instances
 * of {@link Exception}, {@link RuntimeException},
 * {@link Error}, or {@link AssertionError} (in testing
 * scenarios), there may be use cases where a {@code ThrowingSupplier} needs to
 * explicitly throw a {@code Throwable}. In order to support such specialized
 * use cases, {@link #get} is declared to throw {@code Throwable}.
 *
 * @param <T> the type of argument supplied
 * @see java.util.function.Supplier
 * @see org.junit.jupiter.api.Assertions#assertTimeout(java.time.Duration, ThrowingSupplier)
 * @see org.junit.jupiter.api.Assertions#assertTimeoutPreemptively(java.time.Duration, ThrowingSupplier)
 * @see Executable
 * @see ThrowingConsumer
 * @since 5.0
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {

    /**
     * Get a result, potentially throwing an exception.
     *
     * @return a result
     */
    T get() throws Throwable;

    static <T> T getSafe(final ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            throw new IllegalArgumentException(String.format("Unexpected exception thrown: {%s}", t.getClass().getName()), t);
        }
    }
}
