package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.contracts.dpos.dto.req.StakingParam;
import com.bubble.contracts.dpos.dto.req.UpdateStakingParam;
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
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StakingContractTest {

    private String nodeId = "405e2aae5566492aa5da37eb81291e502cee02eac7b03e2734d1a75d3eff78cb7cf849b68f072128e1d5324c150dce4dce06d675ed97f97daca5ce651d9c7303";
    String blsPubKey = "0b7418f7f774f538b610fa5a70d797fd4b205b7b6e91185119c603b9c4c5236c721af355ca109bedfd06ebadaccfe20ea1bf100b40d4e2f6ad5f5152515d7375d043ff988297d4d6826f1931c74e535fbd4b780d49f45df445caadba585fd810";
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
    long chainId = 100;

    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials benefitCredentials;
    private StakingContract stakingContract;

    @Before
    public void init() throws Exception {
        NetworkParameters.init(chainId);

    	superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	System.out.println("superCredentials balance="+ web3j.bubbleGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	stakingCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	benefitCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	System.out.println("benefitCredentials balance="+ web3j.bubbleGetBalance(benefitCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        stakingContract = StakingContract.load(web3j, stakingCredentials);
    }

    @Test
    public void transfer() throws Exception {
    	Transfer.sendFunds(web3j, superCredentials, stakingCredentials.getAddress(), new BigDecimal("1000"), Unit.VON).send();
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }

    @Test
    public void staking() {
        try {
        	StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
        	String benefitAddress = benefitCredentials.getAddress();
        	String externalId = "";
            String nodeName = "node5";
            String webSite = "www.bubble.network";
            String details = "node5-details";
            BigDecimal stakingAmount = Convert.toVon("100000", Unit.KPVON);
            BigInteger rewardPer = BigInteger.valueOf(1000L);

            BubbleSendTransaction bubbleSendTransaction = stakingContract.stakingReturnTransaction(new StakingParam.Builder()
                    .setNodeId(nodeId)
                    .setAmount(stakingAmount.toBigInteger())
                    .setStakingAmountType(stakingAmountType)
                    .setBenifitAddress(benefitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite(webSite)
                    .setDetails(details)
                    .setBlsPubKey(blsPubKey)
                    .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                    .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                    .setRewardPer(rewardPer)
                    .build()).send();

            TransactionResponse baseResponse = stakingContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getStakingInfo() {
    	try {
    		CallResponse<Node> baseResponse = stakingContract.getStakingInfo(nodeId).send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getPackageReward() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getPackageReward().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getStakingReward() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getStakingReward().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void getAvgPackTime() {
    	try {
    		CallResponse<BigInteger> baseResponse = stakingContract.getAvgPackTime().send();
    		System.out.println(baseResponse);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void benefitAddress() {
        try {
        	String benefitAddress = benefitCredentials.getAddress();
        	String externalId = "";
            String nodeName = "node5-u";
            String webSite = "www.bubble.network";
            String details = "node5-details-u";
            BigInteger rewardPer = BigInteger.valueOf(2000L);

            BubbleSendTransaction bubbleSendTransaction = stakingContract.updateStakingInfoReturnTransaction(new UpdateStakingParam.Builder()
            		.setBenifitAddress(benefitAddress)
            		.setExternalId(externalId)
            		.setNodeId(nodeId)
            		.setNodeName(nodeName)
            		.setWebSite(webSite)
            		.setDetails(details)
                    .setRewardPer(rewardPer)
            		.build()).send();

            TransactionResponse baseResponse = stakingContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addStaking() {
        try {
        	StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
            BigDecimal addStakingAmount = Convert.toVon("4000000", Unit.KPVON).add(new BigDecimal("999999999999999998"));

            BubbleSendTransaction bubbleSendTransaction = stakingContract.addStakingReturnTransaction(nodeId, stakingAmountType, addStakingAmount.toBigInteger()).send();
            TransactionResponse baseResponse = stakingContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unStaking() {
        try {
            BubbleSendTransaction bubbleSendTransaction = stakingContract.unStakingReturnTransaction(nodeId).send();
            TransactionResponse baseResponse = stakingContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
