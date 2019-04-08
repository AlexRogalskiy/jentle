package com.wildbeeslabs.jentle.algorithms.logistics.shared.specs;

/**
 * AND specification, used to create a new specification that is the conjunction of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    /**
     * Default specification entities
     */
    /**
     * Default specification entities
     */
    private Specification<T> spec1;
    private Specification<T> spec2;

    /**
     * Create a new AND specification based on two other spec.
     *
     * @param spec1 Specification one.
     * @param spec2 Specification two.
     */
    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSatisfiedBy(final T t) {
        return this.spec1.isSatisfiedBy(t) && this.spec2.isSatisfiedBy(t);
    }
}
