package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_sendTransaction.
 */
public class BubbleSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
