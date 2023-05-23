package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getBlockTransactionCountByHash.
 */
public class BubbleGetBlockTransactionCountByHash extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
