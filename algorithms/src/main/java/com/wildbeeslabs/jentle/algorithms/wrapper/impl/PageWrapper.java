package com.wildbeeslabs.jentle.algorithms.wrapper.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Page wrapper implementation
 *
 * @param <T> type of page content item
 */
@Data
@EqualsAndHashCode
@ToString
public class PageWrapper<T> implements Serializable {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -547298677024456198L;

    private List<T> content;
    private Boolean last;
    private Boolean first;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
}
