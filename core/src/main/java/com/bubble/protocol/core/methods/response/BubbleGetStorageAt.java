package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_getStorageAt.
 */
public class BubbleGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
