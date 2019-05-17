package com.wildbeeslabs.jentle.algorithms.utils;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility methods for {@link String} parsing.
 *
 * @author Oliver Gierke
 * @since 1.5
 */
@UtilityClass
public class CParsingUtils {

    /**
     * Default parsing patterns
     */
    private static final String UPPER = "\\p{Lu}|\\P{InBASIC_LATIN}";
    private static final String LOWER = "\\p{Ll}";
    private static final String CAMEL_CASE_REGEX = "(?<!(^|[%u_$]))(?=[%u])|(?<!^)(?=[%u][%l])".replace("%u", UPPER).replace("%l", LOWER);
    /**
     * Default camel case {@link Pattern}
     */
    private static final Pattern CAMEL_CASE = Pattern.compile(CAMEL_CASE_REGEX);

    /**
     * Splits up the given camel-case {@link String}.
     *
     * @param source must not be {@literal null}.
     * @return
     */
    public static List<String> splitCamelCase(final String source) {
        return split(source, false);
    }

    /**
     * Splits up the given camel-case {@link String} and returns the parts in lower case.
     *
     * @param source must not be {@literal null}.
     * @return
     */
    public static List<String> splitCamelCaseToLower(final String source) {
        return split(source, true);
    }

    /**
     * Reconcatenates the given camel-case source {@link String} using the given delimiter. Will split up the camel-case
     * {@link String} and use an uncapitalized version of the parts.
     *
     * @param source    must not be {@literal null}.
     * @param delimiter must not be {@literal null}.
     * @return
     */
    public static String reconcatenateCamelCase(final String source, final String delimiter) {
        Objects.requireNonNull(source, "Source string must not be null!");
        Objects.requireNonNull(delimiter, "Delimiter must not be null!");

        return Joiner.on(delimiter).join(splitCamelCaseToLower(source));
    }

    /**
     * Returns {@link List} of {@link String} by input source {@link String} and lower case flag
     *
     * @param source  - initial input source {@link String}
     * @param toLower - initial input lower case flag
     * @return {@link List} of {@link String}
     */
    private static List<String> split(final String source, final boolean toLower) {
        Objects.requireNonNull(source, "Source string must not be null!");

        final List<String> result = CAMEL_CASE.splitAsStream(source).map(i -> toLower ? i.toLowerCase() : i).collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns {@link Optional} by input content {@link String} and {@link List} collection of regexes {@link String}
     *
     * @param content   - initial input content {@link String}
     * @param regexList - initial input {@link List} collection of regexes {@link String}
     * @return {@link Optional}
     */
    public static Optional<String> getIfAnyMatch(final String content, final List<String> regexList) {
        return Optional.ofNullable(regexList).orElseGet(Collections::emptyList)
            .stream()
            .map(Pattern::compile)
            .map(pattern -> pattern.matcher(content))
            .filter(Matcher::find)
            .map(Matcher::group)
            .findFirst();
    }
}
