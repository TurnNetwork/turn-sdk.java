package com.bubble.tx;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.BubbleGasPrice;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.exceptions.TransactionException;
import com.bubble.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {

    /**
     * @deprecated use ContractGasProvider
     * @see DefaultGasProvider
     */
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);

    protected Web3j web3j;

    protected TransactionManager transactionManager;

    protected ManagedTransaction(Web3j web3j, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.web3j = web3j;
    }

    /**
     * Return the current gas price from the ethereum node.
     * <p>
     *     Note: this method was previously called {@code getGasPrice} but was renamed to
     *     distinguish it when a bean accessor method on {@link Contract} was added with that name.
     *     If you have a Contract subclass that is calling this method (unlikely since those
     *     classes are usually generated and until very recently those generated subclasses were
     *     marked {@code final}), then you will need to change your code to call this method
     *     instead, if you want the dynamic behavior.
     * </p>
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the ethereum node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        BubbleGasPrice ethGasPrice = web3j.bubbleGasPrice().send();

        return ethGasPrice.getGasPrice();
    }

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {

        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
    }

    protected TransactionReceipt getTransactionReceipt(BubbleSendTransaction ethSendTransaction) throws IOException, TransactionException {
        return transactionManager.getTransactionReceipt(ethSendTransaction);
    }

    protected BubbleSendTransaction sendBubbleRawTransaction(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException {
        return transactionManager.sendTransaction(gasPrice, gasLimit, to, data, value);
    }
}
