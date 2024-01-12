package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

/**
 * eth_estimateGas.
 */
public class BubbleEstimateGas extends Response<String> {
    /*public BigInteger getAmountUsed() throws EstimateGasException {
        String result = getResult();
        if(hasError()){
            throw new EstimateGasException(result);
        }else{
            return Numeric.decodeQuantity(getResult());
        }
    }*/
}
