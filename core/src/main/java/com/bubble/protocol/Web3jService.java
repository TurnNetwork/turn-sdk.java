package com.bubble.protocol;

import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.Response;
import com.bubble.protocol.websocket.events.Notification;
import rx.Observable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Services API.
 */
public interface Web3jService {

    /**
     * Perform a synchronous JSON-RPC request.
     *
     * @param request request to perform
     * @param responseType class of a data item returned by the request
     * @param <T> type of a data item returned by the request
     * @return deserialized JSON-RPC response
     * @throws IOException thrown if failed to perform a request
     */
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    /**
     * Performs an asynchronous JSON-RPC request.
     *
     * @param request request to perform
     * @param responseType class of a data item returned by the request
     * @param <T> type of a data item returned by the request
     * @return CompletableFuture that will be completed when a result is returned or if a
     *         request has failed
     */
    <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType);

    /**
     * Subscribe to a stream of notifications. A stream of notifications is opened by
     * by performing a specified JSON-RPC request and is closed by calling
     * the unsubscribe method. Different WebSocket implementations use different pair of
     * subscribe/unsubscribe methods.
     *
     * <p>This method creates an Observable that can be used to subscribe to new notifications.
     * When a client unsubscribes from this Observable the service unsubscribes from
     * the underlying stream of events.
     *
     * @param request JSON-RPC request that will be send to subscribe to a stream of
     *                events
     * @param unsubscribeMethod method that will be called to unsubscribe from a
     *                          stream of notifications
     * @param responseType class of incoming events objects in a stream
     * @param <T> type of incoming event objects
     * @return Observable that emits incoming events
     */
    <T extends Notification<?>> Observable<T> subscribe(
            Request request,
            String unsubscribeMethod,
            Class<T> responseType);

    /**
     * Closes resources used by the service.
     *
     * @throws IOException thrown if a service failed to close all resources
     */
    void close() throws IOException;
}
