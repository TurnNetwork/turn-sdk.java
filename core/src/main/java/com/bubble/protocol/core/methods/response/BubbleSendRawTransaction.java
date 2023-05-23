package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_sendRawTransaction.
 */
public class BubbleSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
