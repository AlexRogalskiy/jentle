package com.wildbeeslabs.jentle.algorithms.toolset.iface;

import java.io.Serializable;
import java.util.Map;

public interface SessionAttributesProvider {
    Map<String, Serializable> getSessionAttributes();
}
