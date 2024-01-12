package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

public class Operator {

    @JSONField(name = "NodeId")
    private BigInteger nodeId;
    @JSONField(name = "RPC")
    private String rpc;
    @JSONField(name = "OpAddr")
    private String OpAddr;
    @JSONField(name = "Balance")
    private BigInteger balance;
}
