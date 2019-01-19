package com.wildbeeslabs.jentle.algorithms.comparator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString(exclude = {"serialVersionUID"})
public class AddressInfo implements Serializable {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -4457576544688978297L;

    private String city;
    private String zipCode;
}
