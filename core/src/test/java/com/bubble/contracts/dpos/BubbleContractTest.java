package com.bubble.contracts.dpos;

import com.alibaba.fastjson.JSONObject;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.contracts.dpos.dto.req.*;
import com.bubble.contracts.dpos.dto.resp.Node;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;
import com.bubble.utils.Convert.Unit;
import com.bubble.utils.Numeric;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BubbleContractTest {

    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.155:18001"));
    long chainId = 2501;
    private Credentials stakingCredentials;
    private BubbleContract bubbleContract;

    @Before
    public void init() throws Exception {
        NetworkParameters.init(chainId);

    	stakingCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");

        bubbleContract = BubbleContract.load(web3j, stakingCredentials);
    }

    @Test
    public void createBubble() {
        try {
            TransactionResponse send = bubbleContract.createBubble().send();
            System.out.println(send.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void releaseBubble() {
        try {
            BigInteger bubbleId=BigInteger.valueOf(3547106016L);
            TransactionResponse send = bubbleContract.releaseBubble(bubbleId).send();
            System.out.println(send.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stakingToken() {
    	try {
            String account = "0x36c756417E63F740d83908d5216310A4603d6ecc";
            AccountAsset accountAsset = new AccountAsset();
            accountAsset.setAccount(account);
            accountAsset.setNativeAmount(BigInteger.valueOf(1000));
            AccTokenAsset accTokenAsset = new AccTokenAsset();
            accTokenAsset.setTokenAddr("0x3eF5B81922D6Ab5e26D16B7F9b0B1339dEAB7F5f");
            accTokenAsset.setBalance(BigInteger.valueOf(100));
            AccTokenAsset accTokenAsset2 = new AccTokenAsset();
            accTokenAsset2.setTokenAddr("0x0485b562D60F448f52fB876c292e73045b7E9eE7");
            accTokenAsset2.setBalance(BigInteger.valueOf(200));
            AccTokenAsset[] accTokenAssets = new AccTokenAsset[]{accTokenAsset,accTokenAsset2};
            accountAsset.setAccTokenAssets(accTokenAssets);

            TransactionResponse transactionResponse = bubbleContract.stakingToken(new L2StakingTokenParam.Builder()
                    .setBubbleId(BigInteger.valueOf(3547106016L))
                    .setStakingAsset(accountAsset).build()).send();
            System.out.println(transactionResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void withdrewToken() {
        try {
            BigInteger bubbleId=BigInteger.valueOf(3547106016L);
            TransactionResponse send = bubbleContract.withdrewToken(bubbleId).send();
            System.out.println(send.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBubbleInfo() {
    	try {
            BigInteger bubbleId= BigInteger.valueOf(3547106016L);
            System.out.println(bubbleId);
            CallResponse<String> send = bubbleContract.getBubbleInfo(bubbleId).send();
            System.out.println(send);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getL1HashByL2Hash() {
        try {
            BigInteger bubbleId=BigInteger.valueOf(3547106016L);
            String tx = "0x45fa0186ba70a82e03e6523a08d25f1232a718a4ef111eb56f0b8a56d06f6487";
            CallResponse<String> send = bubbleContract.getL1HashByL2Hash(bubbleId, tx).send();
            System.out.println(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBubTxHashList() {
        try {
            BigInteger bubbleId=BigInteger.valueOf(3547106016L);
            Integer type = 0;
            CallResponse<String> send = bubbleContract.getBubTxHashList(bubbleId, type).send();
            System.out.println(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void settleBubble() {
        try {

            String accountOne = "0x36c756417E63F740d83908d5216310A4603d6ecc";
            AccountAsset accountAssetOne = new AccountAsset();
            accountAssetOne.setAccount(accountOne);
            accountAssetOne.setNativeAmount(BigInteger.valueOf(1000));
            AccTokenAsset accTokenAssetOne = new AccTokenAsset();
            accTokenAssetOne.setTokenAddr("0x3eF5B81922D6Ab5e26D16B7F9b0B1339dEAB7F5f");
            accTokenAssetOne.setBalance(BigInteger.valueOf(100));
            AccTokenAsset accTokenAssetOne2 = new AccTokenAsset();
            accTokenAssetOne2.setTokenAddr("0x0485b562D60F448f52fB876c292e73045b7E9eE7");
            accTokenAssetOne2.setBalance(BigInteger.valueOf(200));
            AccTokenAsset[] accTokenAssets = new AccTokenAsset[]{accTokenAssetOne,accTokenAssetOne2};
            accountAssetOne.setAccTokenAssets(accTokenAssets);

            AccountAsset[] accountAssets = new AccountAsset[]{accountAssetOne};

            SettlementInfo settlementInfo = new SettlementInfo();
            settlementInfo.setSettlementInfo(accountAssets);

            String tx = "0x45fa0186ba70a82e03e6523a08d25f1232a718a4ef111eb56f0b8a56d06f6487";
            BubbleSendTransaction bubbleSendTransaction = bubbleContract.settleBubble(new SettlementBubbleParam.Builder()
                    .setBubbleId(BigInteger.valueOf(3547106016L))
                    .setL2SettleTxHash(tx)
                    .setSettlementInfo(settlementInfo)
                    .build()).send();
            TransactionResponse baseResponse = bubbleContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
