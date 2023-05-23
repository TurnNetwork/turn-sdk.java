package com.bubble.tx;

import com.bubble.crypto.Credentials;
import com.bubble.crypto.SampleKeys;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.methods.response.*;
import com.bubble.utils.TxHashVerifier;
import org.junit.Before;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class ManagedTransactionTester {

    static final String ADDRESS = "lat184ktzclhcukjpv8u66a2ukyfx2w38zj2prhd65";
    static final String TRANSACTION_HASH = "0xHASH";
    protected Web3j web3j;
    protected TxHashVerifier txHashVerifier;

    @Before
    public void setUp() throws Exception {
        web3j = mock(Web3j.class);
        txHashVerifier = mock(TxHashVerifier.class);
        when(txHashVerifier.verify(any(), any())).thenReturn(true);
    }

    public TransactionManager getVerifiedTransactionManager(Credentials credentials, int attempts, int sleepDuration) {
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials,attempts, sleepDuration);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }

    public TransactionManager getVerifiedTransactionManager(Credentials credentials) {
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        BubbleGetTransactionCount ethGetTransactionCount = new BubbleGetTransactionCount();
        ethGetTransactionCount.setResult("0x1");

        Request<?, BubbleGetTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send())
                .thenReturn(ethGetTransactionCount);
        when(web3j.bubbleGetTransactionCount(SampleKeys.CREDENTIALS.getAddress(), DefaultBlockParameterName.PENDING))
                .thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        BubbleSendTransaction ethSendTransaction = new BubbleSendTransaction();
        ethSendTransaction.setResult(TRANSACTION_HASH);

        Request<?, BubbleSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(ethSendTransaction);
        when(web3j.bubbleSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) throws IOException {
        BubbleGetTransactionReceipt ethGetTransactionReceipt = new BubbleGetTransactionReceipt();
        ethGetTransactionReceipt.setResult(transactionReceipt);

        Request<?, BubbleGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send())
                .thenReturn(ethGetTransactionReceipt);
        when(web3j.bubbleGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);
    }

    @SuppressWarnings("unchecked")
    protected TransactionReceipt prepareTransfer() throws IOException {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setStatus("0x1");
        prepareTransaction(transactionReceipt);

        BubbleGasPrice ethGasPrice = new BubbleGasPrice();
        ethGasPrice.setResult("0x1");

        Request<?, BubbleGasPrice> gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.send()).thenReturn(ethGasPrice);
        when(web3j.bubbleGasPrice()).thenReturn((Request) gasPriceRequest);

        return transactionReceipt;
    }
}
