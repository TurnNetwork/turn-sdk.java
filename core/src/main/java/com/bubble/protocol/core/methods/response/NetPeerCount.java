package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/**
 * net_peerCount.
 */
public class NetPeerCount extends Response<String> {

    public BigInteger getQuantity() {
        return Numeric.decodeQuantity(getResult());
    }
}
