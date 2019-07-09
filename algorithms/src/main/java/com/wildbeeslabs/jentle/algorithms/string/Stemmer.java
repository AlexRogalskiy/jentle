package com.wildbeeslabs.jentle.algorithms.string;

/**
 * The stemmer is reducing a word to its stem.
 */
public interface Stemmer {

    CharSequence stem(final CharSequence word);
}
