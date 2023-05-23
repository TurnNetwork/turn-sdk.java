package com.bubble.contracts.ppos.exception;

import com.bubble.protocol.exceptions.TransactionException;

public class EstimateGasException extends TransactionException {
    public EstimateGasException(String message) {
        super(message);
    }
}
