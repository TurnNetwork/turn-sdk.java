package com.bubble.contracts.dpos;

import com.alibaba.fastjson.JSONObject;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.req.EditCandidateParam;
import com.bubble.contracts.dpos.dto.req.L2StakingParam;
import com.bubble.contracts.dpos.dto.resp.StakingL2Info;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.methods.response.BubbleChainId;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;
import com.bubble.utils.Convert.Unit;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class StakingL2ContractTest {

    private String nodeId = "a5880c17478c173f407e38c997c4defe290779d15dda19a1e1bd5ca66429006cd7007fd8e1590df120f6f590c3aa054bbb29d0ab01b48a52ede6e4bd53c82c12";
    String blsPubKey = "64dec26c755c0b08f7e7e80b0e299cf273e2a8ee26540fa16ca9287c2d9a7076f6d9971bf5974b2b8e9e86858a27130f570a682dcd12074a35051d8a1ab86934efbddffa1e9c110851447c0ea849d90bd808d9f683786fbbfb184fc7dba4aa13";
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.155:18001"));
    long chainId = 2501;

    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials benefitCredentials;
    private StakingL2Contract stakingContract;

    @Before
    public void init() throws Exception {
        NetworkParameters.init(chainId);

    	superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("superCredentials balance="+ web3j.bubbleGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	stakingCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	benefitCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	//System.out.println("benefitCredentials balance="+ web3j.bubbleGetBalance(benefitCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        stakingContract = StakingL2Contract.load(web3j, stakingCredentials);
    }

    @Test
    public void transfer() throws Exception {
    	Transfer.sendFunds(web3j, superCredentials, stakingCredentials.getAddress(), new BigDecimal("1000"), Unit.VON).send();
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }

    @Test
    public void staking() {
        try {
        	String benefitAddress = benefitCredentials.getAddress();
        	String p2pURI = "enode://"+nodeId+"@ip:port127.0.0.1:8080";
            String electronURI = "http://127.0.0.1:18080";
            String name = "node123";
            String details = "node123-details";
            Boolean isOperator = true;
            BigDecimal stakingAmount = Convert.toVon("100000", Unit.KPVON);

            BubbleSendTransaction bubbleSendTransaction = stakingContract.stakingReturnTransaction(new L2StakingParam.Builder()
                    .setNodeId(nodeId)
                    .setAmount(stakingAmount.toBigInteger())
                    .setBeneficiary(benefitAddress)
                    .setName(name)
                    .setDetails(details)
                    .setBlsPubKey(blsPubKey)
                    .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                    .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                    .setElectronURI(electronURI)
                    .setP2pURI(p2pURI)
                    .setOperator(isOperator)
                    .build()).send();

            TransactionResponse baseResponse = stakingContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse.toString());  //14431
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getCandidateInfo() {
    	try {
    		CallResponse<StakingL2Info> baseResponse = stakingContract.getCandidateInfo(nodeId).send();
    		System.out.println(JSONObject.toJSONString(baseResponse));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Test
    public void editCandidate() {
        try {
        	String benefitAddress = benefitCredentials.getAddress();
            String nodeName = "node123";
            String detail = "node123-details";

            BubbleSendTransaction bubbleSendTransaction = stakingContract.updateStakingInfoReturnTransaction(new EditCandidateParam.Builder()
            		.setBeneficiary(benefitAddress)
            		.setNodeId(nodeId)
            		.setName(nodeName)
            		.setDetail(detail)
            		.build()).send();

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
