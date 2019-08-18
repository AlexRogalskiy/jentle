package com.wildbeeslabs.jentle.algorithms.math;

/**
 * An interface that exposes the clone() method.
 */
public interface PublicCloneable extends Cloneable {

    /**
     * Returns a clone of the object.
     *
     * @return A clone.
     * @throws CloneNotSupportedException if cloning is not supported for some
     *                                    reason.
     */
    Object clone() throws CloneNotSupportedException;
}
