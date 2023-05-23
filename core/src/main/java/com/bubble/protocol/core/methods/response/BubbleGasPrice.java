package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_gasPrice.
 */
public class BubbleGasPrice extends Response<String> {
    public BigInteger getGasPrice() {
        return Numeric.decodeQuantity(getResult());
    }
}
