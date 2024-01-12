package com.bubble.protocol;

import com.bubble.contracts.dpos.Scenario;
import com.bubble.protocol.core.methods.response.bean.EconomicConfig;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RpcScenario extends Scenario {

    @Test
    public void getEconomicConfig() throws Exception {
    	EconomicConfig economicConfig = web3j.getEconomicConfig().send().getEconomicConfig();
    	assertThat(economicConfig.getCommon(), is(notNullValue()));
    	assertThat(economicConfig.getGov(), is(notNullValue()));
    	assertThat(economicConfig.getInnerAcc(), is(notNullValue()));
    	assertThat(economicConfig.getReward(), is(notNullValue()));
    	assertThat(economicConfig.getSlashing(), is(notNullValue()));
    	assertThat(economicConfig.getStaking(), is(notNullValue()));
    	System.out.println(economicConfig);
		System.out.println(web3j.getEconomicConfig().send().getResult());
    }

    @Test
    public void getProgramVersion() throws Exception {
    	ProgramVersion programVersion = web3j.getProgramVersion().send().getAdminProgramVersion();
    	assertThat(programVersion.getProgramVersion(), is(instanceOf(BigInteger.class)));
    	assertThat(programVersion.getProgramVersionSign(), is(instanceOf(String.class)));
    	System.out.println(programVersion);
    }

    @Test
    public void getSchnorrNIZKProve() throws Exception {
    	String nizkProve = web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve();
    	assertThat(nizkProve, is(instanceOf(String.class)));
    	System.out.println(nizkProve);
    }
}
