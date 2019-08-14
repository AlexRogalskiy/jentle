package com.wildbeeslabs.jentle.algorithms.automaton;

/**
 * State visitor.
 *
 * @see FSA#visitInPostOrder(StateVisitor)
 * @see FSA#visitInPreOrder(StateVisitor)
 */
public interface StateVisitor {

    boolean accept(final int state);
}
