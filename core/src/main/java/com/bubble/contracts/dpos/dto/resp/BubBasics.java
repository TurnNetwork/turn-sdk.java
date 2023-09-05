package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

public class BubBasics {

    @JSONField(name = "BubbleId")
    private BigInteger bubbleId;

    @JSONField(name = "address")
    private String creator;

    @JSONField(name = "CreateBlock")
    private Long createBlock;

    @JSONField(name = "OperatorsL1")
    private Operator[] OperatorsL1;

    @JSONField(name = "OperatorsL2")
    private Operator[] OperatorsL2;

    @JSONField(name = "CandidateQueue")
    private Candidate[] microNodes;

}
