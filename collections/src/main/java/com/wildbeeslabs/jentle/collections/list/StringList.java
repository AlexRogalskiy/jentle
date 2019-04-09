package com.wildbeeslabs.jentle.collections.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public final class StringList implements Serializable {

    private static final long serialVersionUID = 93248094385L;

    private transient int size = 0;
    private transient Entry head = null;

    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    public final void add(final String s) {
    }

    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        for (Entry e = head; Objects.nonNull(e); e = e.next) {
            s.writeObject(e.data);
        }
    }

    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();
        for (int i = 0; i < numElements; i++) {
            add((String) s.readObject());
        }
    }
}
