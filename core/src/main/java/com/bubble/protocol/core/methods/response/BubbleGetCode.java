package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_getCode.
 */
public class BubbleGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }
}
