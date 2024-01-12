package com.bubble.contracts.dpos.dto;

import com.bubble.protocol.core.methods.response.TransactionReceipt;

public class TransactionResponse extends BaseResponse {

    private TransactionReceipt transactionReceipt;

    public TransactionReceipt getTransactionReceipt() {
        return transactionReceipt;
    }
    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }
    
	@Override
	public String toString() {
		return "TransactionResponse [transactionReceipt=" + transactionReceipt + ", getCode()=" + getCode()
				+ ", getErrMsg()=" + getErrMsg() + "]";
	}    
}
