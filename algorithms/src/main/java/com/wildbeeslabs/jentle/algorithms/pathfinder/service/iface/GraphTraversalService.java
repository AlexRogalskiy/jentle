package com.wildbeeslabs.jentle.algorithms.pathfinder.service.iface;

import com.wildbeeslabs.jentle.algorithms.pathfinder.model.TransitPath;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 * Part of the external graph traversal API exposed by the routing team
 * and used by us (booking and tracking team).
 */
public interface GraphTraversalService extends Remote {

    /**
     * @param originUnLocode      origin UN Locode
     * @param destinationUnLocode destination UN Locode
     * @param limitations         restrictions on the path selection, as key-value according to some API specification
     * @return A list of transit paths
     * @throws RemoteException RMI problem
     */
    Iterable<TransitPath> findShortestPath(final String originUnLocode, final String destinationUnLocode, final Properties limitations) throws RemoteException;
}
