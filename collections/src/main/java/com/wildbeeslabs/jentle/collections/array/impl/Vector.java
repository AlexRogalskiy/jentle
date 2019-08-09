package com.wildbeeslabs.jentle.collections.array.impl;

public interface Vector {
    interface Op {
        int apply(int a, int b);
    }

    Vector apply(Vector vector, Op op);

    int length();

    int get(int index);

    default Vector add(Vector vector) {
        return apply(vector, (a, b) -> a + b);
    }

    default Vector subtract(Vector vector) {
        return apply(vector, (a, b) -> a - b);
    }

    default Vector multiply(Vector vector) {
        return apply(vector, (a, b) -> a * b);
    }

    default Vector divide(Vector vector) {
        return apply(vector, (a, b) -> a / b);
    }

    default Vector addExact(Vector vector) {
        return apply(vector, Math::addExact);
    }

    default Vector subtractExact(Vector vector) {
        return apply(vector, Math::subtractExact);
    }

    default Vector multiplyExact(Vector vector) {
        return apply(vector, Math::multiplyExact);
    }

    static Vector of(int v0) {
        return VectorImpl.VectorInt1.of(v0);
    }

    static Vector of(int v0, int v1) {
        return VectorImpl.VectorInt2.of(v0, v1);
    }

    static Vector of(int v0, int v1, int v2) {
        return VectorImpl.VectorInt3.of(v0, v1, v2);
    }

    static Vector of(int v0, int v1, int v2, int v3) {
        return VectorImpl.VectorInt4.of(v0, v1, v2, v3);
    }

    static Vector zero(int length) {
        switch (length) {
            case 1:
                return VectorImpl.VectorInt1.zero();
            case 2:
                return VectorImpl.VectorInt2.zero();
            case 3:
                return VectorImpl.VectorInt3.zero();
            case 4:
                return VectorImpl.VectorInt4.zero();
            default:
                return VectorImpl.VectorBig.zero(length);
        }
    }
}
