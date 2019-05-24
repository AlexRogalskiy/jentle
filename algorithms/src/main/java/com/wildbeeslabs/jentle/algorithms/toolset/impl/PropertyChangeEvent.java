package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
class PropertyChangeEvent {
    private final Object item;
    private final String property;
    private final Object prevValue;
    private final Object value;
}
