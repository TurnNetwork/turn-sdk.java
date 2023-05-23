package com.bubble.tx.response;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.BubbleGetTransactionReceipt;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {

    private final Web3j web3j;

    public TransactionReceiptProcessor(Web3j web3j) {
        this.web3j = web3j;
    }

    public abstract TransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        BubbleGetTransactionReceipt transactionReceipt =
                web3j.bubbleGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
