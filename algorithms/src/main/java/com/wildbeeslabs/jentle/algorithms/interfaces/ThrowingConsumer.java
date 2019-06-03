package com.wildbeeslabs.jentle.algorithms.interfaces;

/**
 * {@code ThrowingConsumer} is a functional interface that can be used to
 * implement any generic block of code that consumes an argument and
 * potentially throws a {@link Throwable}.
 *
 * <p>The {@code ThrowingConsumer} interface is similar to
 * {@link java.util.function.Consumer}, except that a {@code ThrowingConsumer}
 * can throw any kind of exception, including checked exceptions.
 *
 * <h4>Rationale for throwing {@code Throwable} instead of {@code Exception}</h4>
 *
 * <p>Although Java applications typically throw exceptions that are instances
 * of {@link java.lang.Exception}, {@link java.lang.RuntimeException},
 * {@link java.lang.Error}, or {@link java.lang.AssertionError} (in testing
 * scenarios), there may be use cases where a {@code ThrowingConsumer} needs to
 * explicitly throw a {@code Throwable}. In order to support such specialized
 * use cases, {@link #accept} is declared to throw {@code Throwable}.
 *
 * @param <T> the type of argument consumed
 * @see java.util.function.Consumer
 * @see org.junit.jupiter.api.DynamicTest#stream
 * @see Executable
 * @see ThrowingSupplier
 * @since 5.0
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Consume the supplied argument, potentially throwing an exception.
     *
     * @param t the argument to consume
     */
    void accept(final T t) throws Throwable;
}
