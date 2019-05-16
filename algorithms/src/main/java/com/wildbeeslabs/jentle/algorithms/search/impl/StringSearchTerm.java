package com.wildbeeslabs.jentle.algorithms.search.impl;

import com.wildbeeslabs.jentle.algorithms.search.iface.SearchTermIF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This class implements string {@link SearchTermIF}
 */
@Getter
@EqualsAndHashCode
@ToString
public class StringSearchTerm implements SearchTermIF<String> {

    private static final long serialVersionUID = 1274042129007696269L;

    /**
     * The pattern.
     *
     * @serial
     */
    protected final String pattern;

    /**
     * Ignore case when comparing?
     *
     * @serial
     */
    protected final boolean ignoreCase;

    /**
     * Construct a StringSearchTerm with the given pattern.
     * Case will be ignored.
     *
     * @param pattern the pattern
     */
    public StringSearchTerm(final String pattern) {
        this(pattern, true);
    }

    /**
     * Construct a StringSearchTerm with the given pattern and ignoreCase flag.
     *
     * @param pattern    the pattern
     * @param ignoreCase should we ignore case?
     */
    public StringSearchTerm(final String pattern, final boolean ignoreCase) {
        this.pattern = pattern;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean match(final String value) {
        int len = value.length() - this.pattern.length();
        for (int i = 0; i <= len; i++) {
            if (value.regionMatches(this.ignoreCase, i, this.pattern, 0, this.pattern.length())) {
                return true;
            }
        }
        return false;
    }
}
