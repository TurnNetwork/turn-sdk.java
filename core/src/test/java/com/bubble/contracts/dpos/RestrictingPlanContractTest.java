package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.BaseResponse;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.RestrictingPlan;
import com.bubble.contracts.dpos.dto.resp.RestrictingItem;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 锁仓计划接口，包括，
 * 创建锁仓计划
 * 获取锁仓信息
 */
public class RestrictingPlanContractTest {

    private RestrictingPlanContract restrictingPlanContract;

    private Credentials credentials;
    private Credentials deleteCredentials;

    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
    long chainId = 2501;

    @Before
    public void init() {
        NetworkParameters.init(chainId);

        credentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        deleteCredentials = Credentials.create("2cd97f718ea9e37f426f62388563be59b98727cbda89fa429b45798faa2a0853");
        restrictingPlanContract = RestrictingPlanContract.load(web3j, credentials);
    }

    /**
     * 创建锁仓计划
     * account 锁仓释放到账账户
     * plan plan 为 RestrictingPlan 类型的列表（数组），RestrictingPlan 定义如下：type RestrictingPlan struct {     Epoch uint64    Amount：*big.Int}其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     */
    @Test
    public void createRestrictingPlan() {

        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(1000), new BigInteger("1000000000000000000000")));
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(2000), new BigInteger("1000000000000000000000")));
        try {
            BubbleSendTransaction bubbleSendTransaction = restrictingPlanContract.createRestrictingPlanReturnTransaction(deleteCredentials.getAddress(), restrictingPlans).send();
            BaseResponse baseResponse = restrictingPlanContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取锁仓信息。
     * account 锁仓释放到账账户
     */
    @Test
    public void getRestrictingPlanInfo() {
        try {
            CallResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(deleteCredentials.getAddress()).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
