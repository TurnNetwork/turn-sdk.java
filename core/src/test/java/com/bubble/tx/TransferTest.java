package com.bubble.tx;

import com.bubble.crypto.Credentials;
import com.bubble.crypto.SampleKeys;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.http.HttpService;
import com.bubble.utils.Convert;
import jdk.nashorn.internal.ir.CallNode;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferTest extends ManagedTransactionTester {

    protected TransactionReceipt transactionReceipt;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        transactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
        assertThat(sendFunds(SampleKeys.CREDENTIALS, ADDRESS, BigDecimal.TEN, Convert.Unit.KPVON),
                is(transactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
        Credentials credentials = Credentials.create(
                "daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        sendFunds(credentials, "0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A",
                new BigDecimal(1000), Convert.Unit.VON);
    }

    protected TransactionReceipt sendFunds(Credentials credentials, String toAddress,
                                           BigDecimal value, Convert.Unit unit) throws Exception {
        return new Transfer(web3j, getVerifiedTransactionManager(credentials))
                .sendFunds(toAddress, value, unit).send();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void aa(){
        Credentials credentials = Credentials.create(
                "daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        Transfer transfer = new Transfer(web3j, transactionManager);
        transfer.sendFunds("0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A",
                new BigDecimal(1000), Convert.Unit.VON);
    }
}
