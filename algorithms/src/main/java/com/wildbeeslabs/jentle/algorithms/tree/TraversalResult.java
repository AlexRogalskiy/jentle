package com.wildbeeslabs.jentle.algorithms.tree;

import lombok.Data;

@Data
public class TraversalResult<T> {
    private T leftResult;
    private T rightResult;
    private T finalResult;
}
