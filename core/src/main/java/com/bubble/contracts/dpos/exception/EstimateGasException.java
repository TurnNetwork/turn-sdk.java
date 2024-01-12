package com.bubble.contracts.dpos.exception;

import com.bubble.protocol.exceptions.TransactionException;

public class EstimateGasException extends TransactionException {
    public EstimateGasException(String message) {
        super(message);
    }
}
