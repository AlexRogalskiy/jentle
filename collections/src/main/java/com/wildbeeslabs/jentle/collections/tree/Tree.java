package com.wildbeeslabs.jentle.collections.tree;

abstract class Tree<E> {
    public interface Visitor<E, R> {
        R leaf(final E elt);

        R branch(final R left, final R right);
    }

    public abstract <R> R visit(final Visitor<E, R> v);

    public static <T> Tree<T> leaf(final T e) {
        return new Tree<T>() {
            public <R> R visit(Visitor<T, R> v) {
                return v.leaf(e);
            }
        };
    }

    public static <T> Tree<T> branch(final Tree<T> l, final Tree<T> r) {
        return new Tree<T>() {
            public <R> R visit(Visitor<T, R> v) {
                return v.branch(l.visit(v), r.visit(v));
            }
        };
    }
}
