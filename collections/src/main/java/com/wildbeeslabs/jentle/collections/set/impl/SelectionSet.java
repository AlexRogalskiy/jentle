package com.wildbeeslabs.jentle.collections.set.impl;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Allows filtering of a collection in order to select a unique element. Once a unique element is found all further
 * filters are ignored.
 */
@RequiredArgsConstructor(staticName = "of")
public class SelectionSet<T> {
    private final Collection<T> collection;
    private final Function<Collection<T>, Optional<T>> fallback;

    /**
     * creates a {@link SelectionSet} with a default fallback of {@literal null}, when no element is found and an
     * {@link IllegalStateException} when no element is found.
     */
    static <T> SelectionSet<T> of(final Collection<T> collection) {
        return new SelectionSet<>(collection, defaultFallback());
    }

    /**
     * If this {@code SelectionSet} contains exactly one element it gets returned. If no unique result can be identified
     * the fallback function passed in at the constructor gets called and its return value becomes the return value of
     * this method.
     *
     * @return a unique result, or the result of the callback provided in the constructor.
     */
    public Optional<T> uniqueResult() {
        Optional<T> uniqueResult = findUniqueResult();
        return uniqueResult.isPresent() ? uniqueResult : fallback.apply(collection);
    }

    /**
     * Filters the collection with the predicate if there are still more then one elements in the collection.
     *
     * @param predicate To be used for filtering.
     */
    public SelectionSet<T> filterIfNecessary(final Predicate<T> predicate) {
        return findUniqueResult().map(it -> this).orElseGet(
            () -> new SelectionSet<T>(collection.stream().filter(predicate).collect(Collectors.toList()), fallback));
    }

    private static <S> Function<Collection<S>, Optional<S>> defaultFallback() {
        return c -> {
            if (c.isEmpty()) {
                return Optional.empty();
            } else {
                throw new IllegalStateException("More then one element in collection.");
            }
        };
    }

    private Optional<T> findUniqueResult() {
        return Optional.ofNullable(collection.size() == 1 ? collection.iterator().next() : null);
    }
}
