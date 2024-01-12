package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_call.
 */
public class BubbleCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
