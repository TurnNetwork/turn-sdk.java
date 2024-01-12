package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_protocolVersion.
 */
public class BubbleProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
