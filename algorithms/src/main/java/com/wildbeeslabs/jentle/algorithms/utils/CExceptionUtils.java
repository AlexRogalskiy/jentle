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
package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Custom exception utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CExceptionUtils {

    @FunctionalInterface
    public interface UnaryConsumerFunction<T, E extends Exception> {

        void accept(final T t) throws E;
    }

    @FunctionalInterface
    public interface BinaryConsumerFunction<T, U, E extends Exception> {

        void accept(final T t, final U u) throws E;
    }

    @FunctionalInterface
    public interface BindingFunction<T, R, E extends Exception> {

        R apply(final T t) throws E;
    }

    @FunctionalInterface
    public interface SupplierFunction<T, E extends Exception> {

        T get() throws E;
    }

    @FunctionalInterface
    public interface RunnableFunction<E extends Exception> {

        void run() throws E;
    }

    public static RuntimeException sneakyThrow(final Throwable throwable) {
        return CExceptionUtils.doThrow(throwable);
    }

    private static <T extends Throwable> T doThrow(final Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * Retrieves cause exception and wraps to {@link CommandActionExecutionException}.
     *
     * @param throwable the throwable
     */
    public static void propagateCause(final Throwable throwable) throws RuntimeException {
        throw new RuntimeException(throwable.getCause());
    }

    /**
     * Wraps cause exception to {@link CommandActionExecutionException}.
     *
     * @param throwable the throwable
     */
    public static RuntimeException wrapCause(final Throwable throwable) {
        return new RuntimeException(throwable.getCause());
    }

    /**
     * Gets actual exception if it's wrapped in {@link CommandActionExecutionException} or {@link HystrixBadRequestException}.
     *
     * @param e the exception
     * @return unwrapped
     */
    public static Throwable unwrapCause(final Throwable e) {
        if (e instanceof Exception) {
            return e.getCause();
        }
        if (e instanceof Error) {
            throw (Error) e;
        }
        if (e instanceof RuntimeException) {
            return e.getCause();
        }
        return e;
    }

    /**
     * Wrap unary consumer exceptions
     * <p>
     * .forEach(wrapUnaryConsumer(name ->
     * System.out.println(Class.forName(name))));
     * .forEach(wrapUnaryConsumer(ClassNameUtil::println));
     * <p>
     * List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
     * integers.forEach(throwingConsumerWrapper(i -> writeToFile(i)));
     *
     * @param <T>
     * @param <E>
     * @param unaryConsumer
     * @return unary consumer function
     */
    public static <T, E extends Exception> Consumer<T> wrapUnaryConsumer(@NonNull final UnaryConsumerFunction<T, E> unaryConsumer) {
        return (t) -> {
            try {
                unaryConsumer.accept(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    // List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
    // integers.forEach(handlingConsumerWrapper(i -> writeToFile(i), IOException.class));
    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(@NonNull final UnaryConsumerFunction<T, E> unaryConsumer, @NonNull final Class<E> exClass) {
        return i -> {
            try {
                unaryConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    final E exCast = exClass.cast(ex);
                    log.error("Exception occured: message=" + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    /**
     * Wrap binary consumer exceptions
     * <p>
     * .forEach(wrapBinaryConsumer(name ->
     * System.out.println(Class.forName(name))));
     * .forEach(wrapBinaryConsumer(ClassNameUtil::println));
     *
     * @param <T>
     * @param <E>
     * @param <U>
     * @param binaryConsumer
     * @return binary consumer function
     */
    public static <T, U, E extends Exception> BiConsumer<T, U> wrapBinaryConsumer(@NonNull final BinaryConsumerFunction<T, U, E> binaryConsumer) {
        return (t, u) -> {
            try {
                binaryConsumer.accept(t, u);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
            }
        };
    }

    /**
     * Wrap function exceptions with binding context
     * <p>
     * .map(wrapFunction(name -> Class.forName(name)));
     * .map(wrapFunction(Class::forName));
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param function
     * @return function with binding context
     */
    public static <T, R, E extends Exception> Function<T, R> wrapFunction(@NonNull final BindingFunction<T, R, E> function) {
        return (t) -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * Wrap supplier exceptions
     * <p>
     * wrapSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114,
     * 107}, "UTF-8")));
     *
     * @param <T>
     * @param <E>
     * @param function
     * @return function result set
     */
    public static <T, E extends Exception> Supplier<T> wrapSupplier(@NonNull final SupplierFunction<T, E> function) {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    /**
     * Uncheck runnable exceptions
     * <p>
     * uncheck(() -> Class.forName("default"));
     *
     * @param <E>
     * @param t
     */
    public static <E extends Exception> void uncheck(@NonNull final RunnableFunction<E> t) {
        try {
            t.run();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
        }
    }

    /**
     * Uncheck supplier exceptions
     * <p>
     * uncheck(() -> Class.forName("default"));
     *
     * @param <R>
     * @param <E>
     * @param supplier
     * @return supplier result set
     */
    public static <R, E extends Exception> R uncheck(@NonNull final SupplierFunction<R, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * Uncheck function exceptions with binding context
     * <p>
     * uncheck(Class::forName, "default");
     *
     * @param <T>
     * @param <R>
     * @param <E>
     * @param function
     * @param t
     * @return function with binding context result set
     */
    public static <T, R, E extends Exception> R uncheck(@NonNull final BindingFunction<T, R, E> function, final T t) {
        try {
            return function.apply(t);
        } catch (Exception exception) {
            throwAsUnchecked(exception);
            return null;
        }
    }

    /**
     * Rethrow exception as unchecked
     *
     * @param <E>
     * @param exception
     * @throws E
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(final Exception exception) throws E {
        throw (E) exception;
    }

    /**
     * Returns the deepest cause of the supplied exception
     *
     * @param throwable the exception for which the stack trace is to be
     *                  returned
     * @return the deepest cause of the supplied exception
     */
    public static Throwable getDeepestThrowable(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }
        Throwable parent = throwable;
        Throwable child = throwable.getCause();
        while (Objects.nonNull(child)) {
            parent = child;
            child = parent.getCause();
        }
        return parent;
    }

    /**
     * Returns the stack trace of the supplied exception.
     *
     * @param throwable the exception for which the stack trace is to be
     *                  returned
     * @return the stack trace, or null if the supplied exception is null
     */
    public static String getStackTrace(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }
        final ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try (final PrintWriter pw = new PrintWriter(bas)) {
            throwable.printStackTrace(pw);
        }
        return bas.toString();
    }

    /**
     * Returns whether the supplied exception is a checked exception
     *
     * @param throwable the supplied exception to check
     * @return true if supplied exception is checked, false - otherwise
     * @see java.lang.Exception
     * @see java.lang.RuntimeException
     * @see java.lang.Error
     */
    public static boolean isCheckedException(final Throwable throwable) {
        return !(throwable instanceof RuntimeException || throwable instanceof Error);
    }

    public static String stringify(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }
        final StringWriter stm = new StringWriter();
        try (final PrintWriter wrt = new PrintWriter(stm)) {
            throwable.printStackTrace(wrt);
        }
        return stm.toString();
    }

    /**
     * Locates a particular type of the supplied exception
     *
     * @param throwable the supplied exception
     * @param type      the type of exception to search for
     * @param <T>
     * @return the first exception of the given type, if found, or null
     */
    public static <T extends Throwable> T findCause(final Throwable throwable, final Class<? extends T> type) {
        Throwable current = throwable;
        while (Objects.nonNull(current)) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }

    //    List<Integer> integers = Arrays.asList(3, 9, 7, 0, 10, 20);
    //    integers.forEach(lambdaWrapper(i -> System.out.println(50 / i), ArithmeticException.class));
    public static <T, E extends Exception> Consumer<T> consumerWrapper(@NonNull final Consumer<T> consumer, @NonNull final Class<E> exClass) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    final E exCast = exClass.cast(ex);
                    log.error("Exception occured: message=" + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }

    //    public long getStackCount() {
//        return StackWalker.getInstance()
//                .walk(Stream::count);
//    }
//
//    public String getStack() {
//        return StackWalker.getInstance()
//                .walk(frames -> frames.map(Object::toString)
//                        .collect(joining(System.lineSeparator())));
//    }
    public int getStackCount() {
        return Thread.currentThread().getStackTrace().length;
    }

    public String getStack() {
        return Arrays.stream(Thread.currentThread()
            .getStackTrace())
            .map(Objects::toString)
            .collect(Collectors.joining(System.lineSeparator()));
    }

    public static RuntimeException throwAsUncheckedException(final Throwable t) {
        Objects.requireNonNull(t, "Throwable must not be null");
        throwAs(t);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwAs(final Throwable t) throws T {
        throw (T) t;
    }

    /**
     * Read the stacktrace of the supplied {@link Throwable} into a String.
     */
    public static String readStackTrace(final Throwable throwable) {
        Objects.requireNonNull(throwable, "Throwable must not be null");
        final StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
        }
        return stringWriter.toString();
    }
}
