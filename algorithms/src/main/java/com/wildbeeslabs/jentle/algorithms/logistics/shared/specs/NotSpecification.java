package com.wildbeeslabs.jentle.algorithms.logistics.shared.specs;

/**
 * NOT decorator used to create a new specification that is the inverse (NOT) of the given specification
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    /**
     * Default specification entities
     */
    private Specification<T> spec1;

    /**
     * Create a new NOT specification based on another spec.
     *
     * @param spec1 Specification instance to not.
     */
    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSatisfiedBy(final T t) {
        return !this.spec1.isSatisfiedBy(t);
    }
}
