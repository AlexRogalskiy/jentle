package com.wildbeeslabs.jentle.algorithms.map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom map pair implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CPair<L, R> {

    private final L left;
    private final R right;

    public static <L, R> Set<R> collectRightAsSet(final List<CPair<L, R>> pairList) {
        return Optional.ofNullable(pairList).orElseGet(Collections::emptyList).stream().map(pair -> pair.right).collect(Collectors.toSet());
    }
}