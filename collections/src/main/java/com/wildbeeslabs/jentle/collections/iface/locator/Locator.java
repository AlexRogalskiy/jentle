package com.wildbeeslabs.jentle.collections.iface.locator;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

/**
 * Locator interface declaration
 *
 * @param <T> type of position element
 */
public interface Locator<T> {

    /**
     * Returns {@link Position} of current node
     *
     * @return {@link Position} of current node
     */
    Position<T> position();

    /**
     * Updates {@link Position} of current node
     *
     * @param position - initial input {@link Position} to update
     */
    void setPosition(final Position<T> position);
}
