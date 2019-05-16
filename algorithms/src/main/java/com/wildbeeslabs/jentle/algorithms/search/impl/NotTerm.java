package com.wildbeeslabs.jentle.algorithms.search.impl;

import com.wildbeeslabs.jentle.algorithms.search.iface.SearchTermIF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This class implements the logical NEGATION operator on invididual {@link SearchTermIF}
 */
@Getter
@EqualsAndHashCode
@ToString
public final class NotTerm<T> implements SearchTermIF<T> {

    private static final long serialVersionUID = 7152293214217310216L;

    /**
     * The search term to negate.
     *
     * @serial
     */
    private final SearchTermIF<T> term;

    public NotTerm(final SearchTermIF<T> term) {
        this.term = term;
    }

    @Override
    public boolean match(final T value) {
        return !this.term.match(value);
    }
}
