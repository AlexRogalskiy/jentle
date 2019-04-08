package com.wildbeeslabs.jentle.algorithms.logistics.service.iface;

import se.citerus.dddsample.domain.model.cargo.TrackingId;

/**
 * Cargo inspection service.
 */
public interface CargoInspectionService {

  /**
   * Inspect cargo and send relevant notifications to interested parties,
   * for example if a cargo has been misdirected, or unloaded
   * at the final destination.
   *
   * @param trackingId cargo tracking id
   */
  void inspectCargo(final TrackingId trackingId);

}
