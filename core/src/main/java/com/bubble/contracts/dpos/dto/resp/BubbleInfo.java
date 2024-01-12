package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class BubbleInfo {

    @JSONField(name = "bubbleID")
    private BubBasics Basics;

    @JSONField(name = "state")
    private Integer state;

    @JSONField(name = "StakingTokenTxHashList")
    private String[] StakingTokenTxHashList;

    @JSONField(name = "WithdrewTokenTxHashList")
    private String[] WithdrewTokenTxHashList;

    @JSONField(name = "SettleBubbleTxHashList")
    private String[] SettleBubbleTxHashList;
}
