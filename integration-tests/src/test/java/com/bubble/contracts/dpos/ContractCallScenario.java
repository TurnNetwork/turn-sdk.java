package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.resp.GovernParam;
import com.bubble.contracts.dpos.dto.resp.Node;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ContractCallScenario extends Scenario {

    @Test
    public void getValidatorList() throws Exception {
    	CallResponse<List<Node>> response = nodeContract.getValidatorList().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getVerifierList() throws Exception {
    	CallResponse<List<Node>> response = nodeContract.getVerifierList().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getCandidateList() throws Exception {
    	CallResponse<List<Node>> response = nodeContract.getCandidateList().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getPackageReward() throws Exception {
    	CallResponse<BigInteger> response = stakingContract.getPackageReward().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getStakingReward() throws Exception {
    	CallResponse<BigInteger> response = stakingContract.getStakingReward().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getAvgPackTime() throws Exception {
    	CallResponse<BigInteger> response = stakingContract.getAvgPackTime().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getGovernParamValue() throws Exception {
        String module = "staking";
        String name = "operatingThreshold";
    	CallResponse<String> response = proposalContract.getGovernParamValue(module, name).send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getParamList() throws Exception {
        String module = "";
    	CallResponse<List<GovernParam>> response = proposalContract.getParamList(module).send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }

    @Test
    public void getActiveVersion() throws Exception {
    	CallResponse<BigInteger> response = proposalContract.getActiveVersion().send();
    	assertThat(response.isStatusOk(), equalTo(true));
    	System.out.println(response);
    }
}
