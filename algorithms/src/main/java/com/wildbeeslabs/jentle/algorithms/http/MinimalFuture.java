package com.wildbeeslabs.jentle.algorithms.http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import java.util.concurrent.atomic.AtomicLong;

/*
 * A CompletableFuture which does not allow any obtrusion logic.
 * All methods of CompletionStage return instances of this class.
 */
public final class MinimalFuture<T> extends CompletableFuture<T> {

    final static AtomicLong TOKENS = new AtomicLong();
    final long id;

    public static <U> MinimalFuture<U> completedFuture(U value) {
        MinimalFuture<U> f = new MinimalFuture<>();
        f.complete(value);
        return f;
    }

    public static <U> CompletableFuture<U> failedFuture(Throwable ex) {
        requireNonNull(ex);
        MinimalFuture<U> f = new MinimalFuture<>();
        f.completeExceptionally(ex);
        return f;
    }

    public MinimalFuture() {
        super();
        this.id = TOKENS.incrementAndGet();
    }

    /**
     * Creates a defensive copy of the given {@code CompletionStage}.
     *
     * <p> Might be useful both for producers and consumers of {@code
     * CompletionStage}s.
     *
     * <p> Producers are protected from possible uncontrolled modifications
     * (cancellation, completion, obtrusion, etc.) as well as from executing
     * unknown potentially lengthy or faulty dependants in the given {@code
     * CompletionStage}'s default execution facility or synchronously.
     *
     * <p> Consumers are protected from some of the aspects of misbehaving
     * implementations (e.g. accepting results, applying functions, running
     * tasks, etc. more than once or escape of a reference to their private
     * executor, etc.) by providing a reliable proxy they use instead.
     *
     * @param src
     *         the {@code CompletionStage} to make a copy from
     * @param executor
     *         the executor used to propagate the completion
     * @param <T>
     *         the type of the {@code CompletionStage}'s result
     *
     * @return a copy of the given stage
     */
    public static <T> MinimalFuture<T> copy(CompletionStage<T> src,
                                            Executor executor) {
        MinimalFuture<T> copy = new MinimalFuture<>();
        BiConsumer<T, Throwable> relay =
                (result, error) -> {
                    if (error != null) {
                        copy.completeExceptionally(error);
                    } else {
                        copy.complete(result);
                    }
                };

        if (src.getClass() == CompletableFuture.class) {
            // No subclasses! Strictly genuine CompletableFuture.
            src.whenCompleteAsync(relay, executor);
            return copy;
        } else {
            // Don't give our executor away to an unknown CS!
            src.whenComplete(relay);
            return (MinimalFuture<T>)
                    copy.thenApplyAsync(Function.identity(), executor);
        }
    }

    public <U> MinimalFuture<U> newIncompleteFuture() {
        return new MinimalFuture<>();
    }

    @Override
    public void obtrudeValue(T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void obtrudeException(Throwable ex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return super.toString() + " (id=" + id +")";
    }
}
