package com.wildbeeslabs.jentle.algorithms.io;

import java.util.function.Supplier;

/**
 * Interface towards a mechanism that manages transactions
 * <p/>
 * Typically, this will involve opening database transactions or connecting to external systems.
 *
 * @author Allard Buijze
 * @since 2.0
 */
public interface TransactionManager {

    /**
     * Starts a transaction. The return value is the started transaction that can be committed or rolled back.
     *
     * @return The object representing the transaction
     */
    Transaction startTransaction();

    /**
     * Executes the given {@code task} in a new {@link Transaction}. The transaction is committed when the task
     * completes normally, and rolled back when it throws an exception.
     *
     * @param task The task to execute
     */
    default void executeInTransaction(Runnable task) {
        Transaction transaction = startTransaction();
        try {
            task.run();
            transaction.commit();
        } catch (Throwable e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * Invokes the given {@code supplier} in a transaction managed by the current TransactionManager. Upon completion
     * of the call, the transaction will be committed in the case of a regular return value, or rolled back in case an
     * exception occurred.
     * <p>
     * This method is an alternative to {@link #executeInTransaction(Runnable)} in cases where a result needs to be
     * returned from the code to be executed transactionally.
     *
     * @param supplier The supplier of the value to return
     * @param <T>      The type of value to return
     * @return The value returned by the supplier
     */
    default <T> T fetchInTransaction(Supplier<T> supplier) {
        Transaction transaction = startTransaction();
        try {
            T result = supplier.get();
            transaction.commit();
            return result;
        } catch (Throwable e) {
            transaction.rollback();
            throw e;
        }
    }
}
