package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.resp.Reward;
import com.bubble.crypto.Credentials;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert.Unit;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 委托相关接口，包括，
 * 发起委托
 * 减持/撤销委托
 * 查询当前单个委托信息
 */
public class RewardContractTest {

    private String nodeId = "18e5d83b36e1959206b5e582053ae994a00731ccbd7d7ddb6ac7d07eab6ee1d859af91784da6d05336ae04cd96bfc0ae7732818fb2c7ac10e4986a591258239d";

    private long chainId = 2501;
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.234:1789"));

    private Credentials deleteCredentials;
    private Credentials superCredentials;
    private RewardContract rewardContract;

    @Before
    public void init() {
        superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        deleteCredentials = Credentials.create("2cd97f718ea9e37f426f62388563be59b98727cbda89fa429b45798faa2a0853");
        rewardContract = RewardContract.load(web3j, deleteCredentials);
    }

    @Test
    public void transfer() throws Exception {
        Transfer.sendFunds(web3j, superCredentials, deleteCredentials.getAddress(), new BigDecimal("10000000"), Unit.KPVON).send();
        System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(deleteCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }

    @Test
    public void withdrawDelegateReward() {
        try {
            BubbleSendTransaction bubbleSendTransaction = rewardContract.withdrawDelegateRewardReturnTransaction().send();
            TransactionResponse baseResponse = rewardContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse);

            if(baseResponse.isStatusOk()){
                List<Reward> rewardList = rewardContract.decodeWithdrawDelegateRewardLog(baseResponse.getTransactionReceipt());
                System.out.println(rewardList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDelegateReward() {
        try {
            List<String> nodeList = new ArrayList<>();
            nodeList.add(nodeId);
            CallResponse<List<Reward>> baseResponse = rewardContract.getDelegateReward(deleteCredentials.getAddress(), nodeList).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
