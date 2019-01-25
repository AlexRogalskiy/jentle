package com.wildbeeslabs.jentle.algorithms.comparator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString
public class DeliveryInfo implements Serializable {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -376282547690062738L;

    private AddressInfo addressInfo;
}
