package com.wildbeeslabs.jentle.algorithms.toolset;

import java.beans.PropertyChangeEvent;

@FunctionalInterface
public interface PropertyChangeListener {
    /**
     * Called when value of instance property changed.
     *
     * @param e event object
     */
    void propertyChanged(final PropertyChangeEvent e);
}
