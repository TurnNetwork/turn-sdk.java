package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.RestrictingPlan;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.resp.RestrictingItem;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;
import com.bubble.utils.Convert.Unit;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RestrictingScenario extends Scenario {

	private BigDecimal transferValue = new BigDecimal("10000000");

	/**
	 *  正常的场景:
	 *  初始化账户余额
	 *  创建锁仓计划(4000)
	 *  获取锁仓信息(4100)
	 */
	@Test
	public void executeScenario() throws Exception {
		//初始化账户余额
		transfer();
		BigInteger restrictingBalance = web3j.bubbleGetBalance(restrictingSendCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
		assertTrue(new BigDecimal(restrictingBalance).compareTo(Convert.fromVon(transferValue, Unit.VON))>=0);

		//创建锁仓计划(4000)
		TransactionResponse createRestrictingPlanResponse = createRestrictingPlan();
		assertTrue(createRestrictingPlanResponse.toString(),createRestrictingPlanResponse.isStatusOk());

		//获取锁仓信息(4100)
		CallResponse<RestrictingItem> getRestrictingPlanInfoResponse = getRestrictingPlanInfo();
		assertTrue(getRestrictingPlanInfoResponse.toString(),getRestrictingPlanInfoResponse.isStatusOk());
	}


	public CallResponse<RestrictingItem> getRestrictingPlanInfo() throws Exception {
		CallResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(restrictingRecvCredentials.getAddress()).send();
		return baseResponse;
	}


    public TransactionResponse createRestrictingPlan() throws Exception {
        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(100), new BigInteger("100000000000000000000")));
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(200), new BigInteger("200000000000000000000")));
        BubbleSendTransaction bubbleSendTransaction = restrictingPlanContract.createRestrictingPlanReturnTransaction(restrictingRecvCredentials.getAddress(), restrictingPlans).send();
        TransactionResponse baseResponse = restrictingPlanContract.getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

	public void transfer() throws Exception {
		Transfer.sendFunds(web3j, superCredentials, restrictingSendCredentials.getAddress(), transferValue, Unit.KPVON).send();
	}
}
