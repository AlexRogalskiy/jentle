package com.wildbeeslabs.jentle.algorithms.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Foo extends AbstractFoo implements Serializable {

    private static final long serialVersionUID = 1856835860954L;

    public Foo(int x, int y) {
        super(x, y);
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        int x = s.readInt();
        int y = s.readInt();
        initialize(x, y);
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(getX());
        s.writeInt(getY());
    }
}
