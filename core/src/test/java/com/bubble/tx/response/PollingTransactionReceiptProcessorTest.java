package com.bubble.tx.response;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.response.BubbleGetTransactionReceipt;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.exceptions.TransactionException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@SuppressWarnings({"deprecation","unchecked"})
public class PollingTransactionReceiptProcessorTest {
    private static final String TRANSACTION_HASH = "0x00";
    private Web3j web3j;
    private long sleepDuration;
    private int attempts;
    private PollingTransactionReceiptProcessor processor;

    @Before
    public void setUp() {
        web3j = mock(Web3j.class);
        sleepDuration = 100;
        attempts = 3;
        processor = new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts);
    }

    @Test
    public void returnsTransactionReceiptWhenItIsAvailableInstantly() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        doReturn(requestReturning(response(transactionReceipt)))
                .when(web3j).bubbleGetTransactionReceipt(TRANSACTION_HASH);

        TransactionReceipt receipt = processor.waitForTransactionReceipt(TRANSACTION_HASH);

        assertThat(receipt, sameInstance(transactionReceipt));
    }

    @Test
    public void throwsTransactionExceptionWhenReceiptIsNotAvailableInTime() throws Exception {
        doReturn(requestReturning(response(null)))
                .when(web3j).bubbleGetTransactionReceipt(TRANSACTION_HASH);

        try {
            processor.waitForTransactionReceipt(TRANSACTION_HASH);
            fail("call should fail with TransactionException");
        } catch (TransactionException e) {
            // this is expected
        }
    }

    private static <T extends Response<?>> Request<String, T> requestReturning(T response) {
        Request<String, T> request = mock(Request.class);
        try {
            when(request.send()).thenReturn(response);
        } catch (IOException e) {
            // this will never happen
        }
        return request;
    }

    private static BubbleGetTransactionReceipt response(TransactionReceipt transactionReceipt) {
        BubbleGetTransactionReceipt response = new BubbleGetTransactionReceipt();
        response.setResult(transactionReceipt);
        return response;
    }
}
