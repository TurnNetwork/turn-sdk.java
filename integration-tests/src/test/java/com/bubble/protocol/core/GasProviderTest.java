package com.bubble.protocol.core;

import com.alibaba.fastjson.JSONObject;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.resp.Proposal;
import com.bubble.contracts.dpos.utils.EncoderUtils;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.request.Transaction;
import com.bubble.protocol.core.methods.response.BubbleEstimateGas;
import com.bubble.protocol.http.HttpService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @Author liushuyu
 * @Date 2021/6/4 11:01
 * @Version
 * @Desc
 */
public class GasProviderTest {

    @Before
    public void init(){
        NetworkParameters.selectBubble();
    }

    /**
     * 底层1.0.0版本
     * @throws IOException
     */
    @Test
    public void testGetProviderWithGasPrice_version1_0_0() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6789"));
        String fromAddr = Credentials.create("3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668").getAddress();
        String contractAddr = NetworkParameters.getDposContractAddressOfProposal();

        String nodePublicKey = "8d4967ba21c46d63a352b7bccc47810ebdda7869861fbd341d50bfdeb4318e6e429749a3595102392921e434f8d46720af90d018da3a616f931597f57990ded0";
        String pipId = "111";
        String module = "staking";
        String paramName = "stakeThreshold";
        String paramValue = "1";
        Proposal proposal =
                Proposal.createSubmitParamProposalParam(
                        nodePublicKey,
                        pipId,
                        module,
                        paramName,
                        paramValue);
        Function function = new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters());
        //BigInteger gasLimit = web3j.bubbleEstimateGas(transaction).send().getAmountUsed();
        //gasPrice必须首先获得，在estimateGas的时候，治理合约就需要gasPrice。
        //estimateGas的时候，交易的所有参数，除了gasLimit，应该和真正发送时的参数一样。
        BigInteger gasPrice = BigInteger.valueOf(100000000000000L);

        BigInteger gasLimit = null;
        BigInteger nonce = null;

        //String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddr, nonce, gasPrice, gasLimit, contractAddr, EncoderUtils.functionEncoder(function));

        BubbleEstimateGas bubbleEstimateGas = web3j.bubbleEstimateGas(transaction).send();

        //不带gasprice 估算一次
        Transaction transaction_noGasPrice = Transaction.createEthCallTransaction(fromAddr, contractAddr, EncoderUtils.functionEncoder(function));
        BubbleEstimateGas bubbleEstimateGas_noGasPrice = web3j.bubbleEstimateGas(transaction_noGasPrice).send();

        System.out.println("带gas Price：" + JSONObject.toJSONString(bubbleEstimateGas));
        System.out.println("不带gas Price：" + JSONObject.toJSONString(bubbleEstimateGas_noGasPrice));
        Assert.assertTrue(bubbleEstimateGas.getError() != null);
        Assert.assertTrue(bubbleEstimateGas_noGasPrice.getError() != null);
    }


    /**
     * 底层1.1.0版本
     * @throws IOException
     */
    @Test
    public void testGetProviderWithGasPrice_version1_1_0() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6780"));
        String fromAddr = Credentials.create("3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668").getAddress();
        String contractAddr = NetworkParameters.getDposContractAddressOfProposal();

        String nodePublicKey = "0abaf3219f454f3d07b6cbcf3c10b6b4ccf605202868e2043b6f5db12b745df0604ef01ef4cb523adc6d9e14b83a76dd09f862e3fe77205d8ac83df707969b47";
        String pipId = "111";
        String module = "staking";
        String paramName = "stakeThreshold";
        String paramValue = "1";
        Proposal proposal =
                Proposal.createSubmitParamProposalParam(
                        nodePublicKey,
                        pipId,
                        module,
                        paramName,
                        paramValue);
        Function function = new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters());
        //BigInteger gasLimit = web3j.bubbleEstimateGas(transaction).send().getAmountUsed();
        //gasPrice必须首先获得，在estimateGas的时候，治理合约就需要gasPrice。
        //estimateGas的时候，交易的所有参数，除了gasLimit，应该和真正发送时的参数一样。
        BigInteger gasPrice = BigInteger.valueOf(100000000000000L);

        BigInteger gasLimit = null;
        BigInteger nonce = null;

        //String from, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddr, nonce, gasPrice, gasLimit, contractAddr, EncoderUtils.functionEncoder(function));

        BubbleEstimateGas bubbleEstimateGas = web3j.bubbleEstimateGas(transaction).send();

        //不带gasprice 估算一次
        Transaction transaction_noGasPrice = Transaction.createEthCallTransaction(fromAddr, contractAddr, EncoderUtils.functionEncoder(function));
        BubbleEstimateGas bubbleEstimateGas_noGasPrice = web3j.bubbleEstimateGas(transaction_noGasPrice).send();

        System.out.println("带gas Price：" + JSONObject.toJSONString(bubbleEstimateGas));
        System.out.println("不带gas Price：" + JSONObject.toJSONString(bubbleEstimateGas_noGasPrice));
        Assert.assertTrue(bubbleEstimateGas.getError() == null);
        Assert.assertTrue(bubbleEstimateGas_noGasPrice.getError() == null);
    }

}
