package com.bubble.crypto;


import com.bubble.parameters.NetworkParameters;
import com.bubble.utils.Numeric;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class TransactionDecoderTest {

    @Test
    public void testDecoding() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        String to = "0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A";
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, gasLimit, to, value);
        byte[] encodedMessage = TransactionEncoder.encode(rawTransaction);
        String hexMessage = Numeric.toHexString(encodedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
    }

    @Test
    public void testDecodingSignedChainId() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        String to = "atp1x0yc7gxaw0tmk82n8392xdcl9vcvd677g57j0q";
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, gasLimit, to, value);
        byte[] signedMessage = TransactionEncoder.signMessage(
                rawTransaction, NetworkParameters.getChainId(), SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
        assertTrue(result instanceof SignedRawTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertEquals(SampleKeys.HEX_ADDRESS, signedResult.getFrom());
        signedResult.verify(SampleKeys.HEX_ADDRESS);
        assertEquals(NetworkParameters.getChainId(), signedResult.getChainId().longValue());
    }

    @Test
    public void testRSize31() throws Exception {
        //CHECKSTYLE:OFF
        String hexTransaction = "0xf885370183419ce09433c98f20dd73d7bb1d533c4aa3371f2b30c6ebde80a45093dc7d000000000000000000000000000000000000000000000000000000000000003581eba08b3d958db92dec4a69402f285ad2654b9d6b50e2b5bd751fad02bc9820248233a07cbf75551af26b9d73b27ae94099fd45378d50cb5d94ca73644427a20f0638fb";
        //CHECKSTYLE:ON
        RawTransaction result = TransactionDecoder.decode(hexTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;

        assertEquals(SampleKeys.HEX_ADDRESS, signedResult.getFrom());
    }
}
