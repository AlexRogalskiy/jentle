package com.wildbeeslabs.jentle.collections.map.iface;

/**
 * A factory for creating objects to be used as a component in another structure.
 *
 * @param <T> type of component to be created.
 */
public interface ComponentFactory<T> {
    /**
     * Create a new instance of a component object based on initial arguments.  If no arguments are required then
     * and empty array is required.
     *
     * @param initArgs - arguments for initialisation.
     * @return a newly initialised object.
     */
    T newInstance(final Object... initArgs);
}
