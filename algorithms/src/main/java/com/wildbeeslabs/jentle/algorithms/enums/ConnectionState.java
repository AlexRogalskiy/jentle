package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * The states of a node connection
 * <p>
 * DISCONNECTED: connection has not been successfully established yet
 * CONNECTING: connection is under progress
 * CHECKING_API_VERSIONS: connection has been established and api versions check is in progress. Failure of this check will cause connection to close
 * READY: connection is ready to send requests
 * AUTHENTICATION_FAILED: connection failed due to an authentication error
 */
public enum ConnectionState {
    DISCONNECTED, CONNECTING, CHECKING_API_VERSIONS, READY, AUTHENTICATION_FAILED;

    public boolean isDisconnected() {
        return this == AUTHENTICATION_FAILED || this == DISCONNECTED;
    }

    public boolean isConnected() {
        return this == CHECKING_API_VERSIONS || this == READY;
    }
}
