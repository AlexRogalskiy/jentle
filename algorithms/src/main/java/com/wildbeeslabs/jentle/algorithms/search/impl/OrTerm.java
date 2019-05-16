package com.wildbeeslabs.jentle.algorithms.search.impl;

import com.wildbeeslabs.jentle.algorithms.search.iface.SearchTermIF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This class implements the logical OR operator on individual {@link SearchTermIF}s
 */
@Getter
@EqualsAndHashCode
@ToString
public final class OrTerm<T> implements SearchTermIF<T> {

    private static final long serialVersionUID = 5380534067523646936L;

    /**
     * The array of terms on which the OR operator should
     * be applied
     */
    private final SearchTermIF<T>[] terms;

    /**
     * Constructor that takes two operands.
     *
     * @param t1 first term
     * @param t2 second term
     */
    public OrTerm(final SearchTermIF<T> t1, SearchTermIF<T> t2) {
        this.terms = new SearchTermIF[2];
        this.terms[0] = t1;
        this.terms[1] = t2;
    }

    /**
     * Constructor that takes an array of SearchTerms.
     *
     * @param term array of search terms
     */
    public OrTerm(final SearchTermIF<T>[] term) {
        this.terms = new SearchTermIF[term.length];
        for (int i = 0; i < term.length; i++) {
            this.terms[i] = term[i];
        }
    }

    /**
     * Return the search terms.
     *
     * @return the search terms
     */
    public SearchTermIF<T>[] getTerms() {
        return this.terms.clone();
    }

    /**
     * The OR operation. <p>
     * <p>
     * The terms specified in the constructor are applied to
     * the given object and the OR operator is applied to their results.
     *
     * @param value The specified SearchTerms are applied to this Message and the OR operator is applied to their results.
     * @return true if the OR succeds, otherwise false
     */
    @Override
    public boolean match(final T value) {
        for (int i = 0; i < this.terms.length; i++) {
            if (this.terms[i].match(value)) {
                return true;
            }
        }
        return false;
    }
}
