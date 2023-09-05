package com.bubble.contracts.dpos;

import com.alibaba.fastjson.JSONObject;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.contracts.dpos.dto.req.*;
import com.bubble.contracts.dpos.dto.resp.StakingL2Info;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.core.methods.response.Log;
import com.bubble.protocol.http.HttpService;
import com.bubble.rlp.wasm.RLPDecoder;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;
import com.bubble.utils.Convert.Unit;
import com.bubble.utils.Numeric;
import jnr.ffi.StructLayout;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class BubbleTokenContractTest {

    private String nodeId = "405e2aae5566492aa5da37eb81291e502cee02eac7b03e2734d1a75d3eff78cb7cf849b68f072128e1d5324c150dce4dce06d675ed97f97daca5ce651d9c7303";
    String blsPubKey = "0b7418f7f774f538b610fa5a70d797fd4b205b7b6e91185119c603b9c4c5236c721af355ca109bedfd06ebadaccfe20ea1bf100b40d4e2f6ad5f5152515d7375d043ff988297d4d6826f1931c74e535fbd4b780d49f45df445caadba585fd810";
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.234:2789"));
    long chainId = 2501;

    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials benefitCredentials;
    private BubbleTokenContract bubbleTokenContract;

    @Before
    public void init() throws Exception {
        NetworkParameters.init(chainId);

    	superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("superCredentials balance="+ web3j.bubbleGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	stakingCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	benefitCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("benefitCredentials balance="+ web3j.bubbleGetBalance(benefitCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        bubbleTokenContract = BubbleTokenContract.load(web3j, stakingCredentials);
    }

    @Test
    public void mintToken() {
        try {
            String account = "0x36c756417E63F740d83908d5216310A4603d6ecc";
            AccountAsset accountAsset = new AccountAsset();
            accountAsset.setAccount(account);
            accountAsset.setNativeAmount(BigInteger.valueOf(1000));
            AccTokenAsset accTokenAsset = new AccTokenAsset();
            accTokenAsset.setTokenAddr("0xa18B2442CBee8D22bED5a9d2b5ce98EFB7E497f2");
            accTokenAsset.setBalance(BigInteger.valueOf(100));
            AccTokenAsset accTokenAsset2 = new AccTokenAsset();
            accTokenAsset2.setTokenAddr("0x3aa4e6d933342648173f17cf8557a75850f29b59");
            accTokenAsset2.setBalance(BigInteger.valueOf(200));
            AccTokenAsset[] accTokenAssets = new AccTokenAsset[]{accTokenAsset,accTokenAsset2};
            accountAsset.setAccTokenAssets(accTokenAssets);

            String l1StakingTokenTxHash = "0x45fa0186ba70a82e03e6523a08d25f1232a718a4ef111eb56f0b8a56d06f6487";

            BubbleSendTransaction bubbleSendTransaction = bubbleTokenContract.mintToken(new MintTokenParam.Builder()
                    .setStakingAsset(accountAsset)
                    .setL1StakingTokenTxHash(l1StakingTokenTxHash)
                    .build()).send();

            TransactionResponse baseResponse = bubbleTokenContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());//14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void settleBubble() {
    	try {
            BubbleSendTransaction bubbleSendTransaction = bubbleTokenContract.settleBubble().send();
            TransactionResponse baseResponse = bubbleTokenContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());  //14431
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getL2HashByL1Hash() {
        try {
            String tx = "0x45fa0186ba70a82e03e6523a08d25f1232a718a4ef111eb56f0b8a56d06f6487";
            CallResponse<String> send = bubbleTokenContract.getL2HashByL1Hash(tx).send();
            System.out.println(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
