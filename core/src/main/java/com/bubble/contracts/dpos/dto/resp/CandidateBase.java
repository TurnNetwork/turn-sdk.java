package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.bubble.abi.solidity.datatypes.Bool;

import java.math.BigInteger;

public class CandidateBase {

    @JSONField(name = "BubbleId")
    private BigInteger nodeId;
    @JSONField(name = "BubbleId")
    private String blsPubKey;
    @JSONField(name = "StakingAddress")
    private String stakingAddress;
    @JSONField(name = "BenefitAddress")
    private String benefitAddress;
    @JSONField(name = "StakingTxIndex")
    private BigInteger stakingTxIndex;
    @JSONField(name = "ProgramVersion")
    private BigInteger programVersion;
    @JSONField(name = "StakingBlockNum")
    private BigInteger stakingBlockNum;
    @JSONField(name = "IsOperator")
    private Bool isOperator;
    @JSONField(name = "Description")
    private Description description;

}
