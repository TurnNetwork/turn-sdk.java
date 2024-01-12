package com.bubble.protocol.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bubble.protocol.ObjectMapperFactory;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.websocket.WebSocketClient;
import com.bubble.protocol.websocket.WebSocketListener;
import com.bubble.protocol.websocket.WebSocketService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class WebSocketEventTest {

    private WebSocketClient webSocketClient = mock(WebSocketClient.class);

    private WebSocketService webSocketService = new WebSocketService(
            webSocketClient, true
    );

    private Web3j web3j = Web3j.build(webSocketService);

    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private WebSocketListener listener;

    @Before
    public void before() throws Exception {
        when(webSocketClient.connectBlocking()).thenReturn(true);

        doAnswer(invocation -> {
            listener = invocation.getArgumentAt(0, WebSocketListener.class);
            return null;
        }).when(webSocketClient).setListener(any());

        doAnswer(invocation -> {
            String message = invocation.getArgumentAt(0, String.class);
            int requestId = getRequestId(message);

            sendSubscriptionConfirmation(requestId);
            return null;
        }).when(webSocketClient).send(anyString());

        webSocketService.connect();
    }

    @Test
    public void testNewHeadsNotifications() {
        web3j.newHeadsNotifications();

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"bub_subscribe\","
                        + "\"params\":\\[\"newHeads\"],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testLogsNotificationsWithoutArguments() {
        web3j.logsNotifications(new ArrayList<>(), new ArrayList<>());

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"bub_subscribe\","
                        + "\"params\":\\[\"logs\",\\{}],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testLogsNotificationsWithArguments() {
        web3j.logsNotifications(
                Collections.singletonList("0x1"),
                Collections.singletonList("0x2"));

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"bub_subscribe\","
                        + "\"params\":\\[\"logs\",\\{\"address\":\\[\"0x1\"],"
                        + "\"topics\":\\[\"0x2\"]}],\"id\":[0-9]{1,}}"));
    }

    private int getRequestId(String message) throws IOException {
        JsonNode messageJson = objectMapper.readTree(message);
        return messageJson.get("id").asInt();
    }

    private void sendSubscriptionConfirmation(int requestId) throws IOException {
        listener.onMessage(
                String.format(
                        "{"
                                + "\"jsonrpc\":\"2.0\","
                                + "\"id\":%d,"
                                + "\"result\":\"0xcd0c3e8af590364c09d0fa6a1210faf5\""
                                + "}",
                        requestId));
    }
}
