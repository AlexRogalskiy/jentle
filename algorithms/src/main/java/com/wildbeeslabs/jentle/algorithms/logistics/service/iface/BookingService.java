package com.wildbeeslabs.jentle.algorithms.logistics.service.iface;

import se.citerus.dddsample.domain.model.cargo.Itinerary;
import se.citerus.dddsample.domain.model.cargo.TrackingId;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.util.Date;
import java.util.List;

/**
 * Cargo booking service.
 */
public interface BookingService {

  /**
   * Registers a new cargo in the tracking system, not yet routed.
   *
   * @param origin      cargo origin
   * @param destination cargo destination
   * @param arrivalDeadline arrival deadline
   * @return Cargo tracking id
   */
  TrackingId bookNewCargo(final UnLocode origin, final UnLocode destination, final Date arrivalDeadline);

  /**
   * Requests a list of itineraries describing possible routes for this cargo.
   *
   * @param trackingId cargo tracking id
   * @return A list of possible itineraries for this cargo
   */
  List<Itinerary> requestPossibleRoutesForCargo(final TrackingId trackingId);

  /**
   * @param itinerary itinerary describing the selected route
   * @param trackingId cargo tracking id
   */
  void assignCargoToRoute(final Itinerary itinerary, final TrackingId trackingId);

  /**
   * Changes the destination of a cargo.
   *
   * @param trackingId cargo tracking id
   * @param unLocode UN locode of new destination
   */
  void changeDestination(final TrackingId trackingId, final UnLocode unLocode);

}

