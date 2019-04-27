package com.wildbeeslabs.jentle.collections.model.interfaces;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

public interface Locator<T> {

    Position<T> getPosition();

    void setPosition(final Position<T> position);
}
