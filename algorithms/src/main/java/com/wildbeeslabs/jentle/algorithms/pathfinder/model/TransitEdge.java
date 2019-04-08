package com.wildbeeslabs.jentle.algorithms.pathfinder.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * TransitEdge entity
 */
@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class TransitEdge implements Serializable {

    private final String voyageNumber;
    private final String fromUnLocode;
    private final String toUnLocode;
    private final Date fromDate;
    private final Date toDate;
}
