package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.enums.DelegateAmountType;
import com.bubble.contracts.dpos.dto.resp.*;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.Convert.Unit;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 委托相关接口，包括，
 * 发起委托
 * 减持/撤销委托
 * 查询当前单个委托信息
 */
public class DelegateContractTest {
    private String nodeId = "405e2aae5566492aa5da37eb81291e502cee02eac7b03e2734d1a75d3eff78cb7cf849b68f072128e1d5324c150dce4dce06d675ed97f97daca5ce651d9c7303";
    private BigInteger stakingNb = BigInteger.valueOf(14431L);
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
    long chainId = 2501;

    private Credentials deleteCredentials;
    private Credentials superCredentials;
    private DelegateContract delegateContract;

    @Before
    public void init() {
        NetworkParameters.init(chainId);
        superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        deleteCredentials = Credentials.create("2cd97f718ea9e37f426f62388563be59b98727cbda89fa429b45798faa2a0853");
        delegateContract = DelegateContract.load(web3j, deleteCredentials);
    }

    @Test
    public void transfer() throws Exception {
    	Transfer.sendFunds(web3j, superCredentials, deleteCredentials.getAddress(), new BigDecimal("10000000"), Unit.KPVON).send();
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(deleteCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }

    @Test
    public void delegate() {
        try {
            BubbleSendTransaction bubbleSendTransaction = delegateContract.delegateReturnTransaction(nodeId, DelegateAmountType.DELEGATE_LOCK_AMOUNT_TYPE, new BigInteger("100000000000000000000")).send();
            TransactionResponse baseResponse = delegateContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void estimateDelegateGasLimit() {
        try {
            GasProvider gasProvider = delegateContract.getDelegateGasProvider(nodeId, DelegateAmountType.FREE_AMOUNT_TYPE, new BigInteger("200000000000000000000"));
            System.out.println("gasProvider.getGasLimit():" + gasProvider.getGasLimit());
            TransactionResponse response = delegateContract.delegate(nodeId, DelegateAmountType.FREE_AMOUNT_TYPE, new BigInteger("200000000000000000000")).send();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unDelegate() {
        try {
            BubbleSendTransaction bubbleSendTransaction = delegateContract.unDelegateReturnTransaction(nodeId, stakingNb, new BigInteger("200000000000000000000")).send();
            TransactionResponse baseResponse = delegateContract.getTransactionResponse(bubbleSendTransaction).send();

            if(baseResponse.isStatusOk()){
                UnDelegation unDelegation = delegateContract.decodeUnDelegateLogOfNew(baseResponse.getTransactionReceipt());
                System.out.println(unDelegation);
            }

            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redeemDelegation() {
        try {
            BubbleSendTransaction bubbleSendTransaction = delegateContract.redeemDelegationReturnTransaction().send();
            TransactionResponse baseResponse = delegateContract.getTransactionResponse(bubbleSendTransaction).send();

            if(baseResponse.isStatusOk()){
                RedeemDelegation redeemDelegation = delegateContract.decodeRedeemDelegateLog(baseResponse.getTransactionReceipt());
                System.out.println(redeemDelegation);
            }
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDelegateInfo() {
        try {
            CallResponse<Delegation> baseResponse = delegateContract.getDelegateInfo(nodeId, deleteCredentials.getAddress(), stakingNb).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRelatedListByDelAddr() {
        try {
        	CallResponse<List<DelegationIdInfo>> baseResponse = delegateContract.getRelatedListByDelAddr(deleteCredentials.getAddress()).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDelegateLockInfo() {
        try {
            CallResponse<DelegationLockInfo> baseResponse = delegateContract.getDelegationLockInfo(deleteCredentials.getAddress()).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
