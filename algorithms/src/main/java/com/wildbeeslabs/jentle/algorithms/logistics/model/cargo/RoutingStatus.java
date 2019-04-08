package com.wildbeeslabs.jentle.algorithms.logistics.model.cargo;

import se.citerus.dddsample.domain.shared.ValueObject;

/**
 * Routing status. 
 */
public enum RoutingStatus implements ValueObject<RoutingStatus> {
  NOT_ROUTED, ROUTED, MISROUTED;

  @Override
  public boolean sameValueAs(final RoutingStatus other) {
    return this.equals(other);
  }
  
}
