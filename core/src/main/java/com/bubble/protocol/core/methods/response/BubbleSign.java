package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_sign.
 */
public class BubbleSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
