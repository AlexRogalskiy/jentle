package com.wildbeeslabs.jentle.algorithms.io;

/**
 * Interface of an object that represents a started transaction that can be committed or rolled back.
 *
 * @author Rene de Waele
 */
public interface Transaction {

    /**
     * Commit this transaction.
     */
    void commit();

    /**
     * Roll back this transaction.
     */
    void rollback();

}
