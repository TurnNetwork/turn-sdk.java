package com.bubble.tx;

import com.bubble.crypto.SampleKeys;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.tx.exceptions.TxHashMismatchException;
import com.bubble.utils.Convert;
import org.junit.Test;

import java.math.BigDecimal;

public class RawTransactionManagerTest extends ManagedTransactionTester {

    @Test(expected = TxHashMismatchException.class)
    public void testTxHashMismatch() throws Exception {
        TransactionReceipt transactionReceipt = prepareTransfer();
        prepareTransaction(transactionReceipt);

        TransactionManager transactionManager =
                new RawTransactionManager(web3j, SampleKeys.CREDENTIALS);
        Transfer transfer = new Transfer(web3j, transactionManager);
        transfer.sendFunds(ADDRESS, BigDecimal.ONE, Convert.Unit.KPVON).send();
    }
}
