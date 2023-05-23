package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

/**
 * shh_newFilter.
 */
public class ShhNewFilter extends Response<String> {

    public BigInteger getFilterId() {
        return Numeric.decodeQuantity(getResult());
    }
}
