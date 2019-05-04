package com.wildbeeslabs.jentle.collections.iface.locator;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

public interface Locator<T> {

    Position<T> position();

    void setPosition(final Position<T> position);
}
