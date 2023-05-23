package com.bubble.contracts.ppos.abi.custom;

import com.bubble.contracts.ppos.abi.CustomType;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.utils.Numeric;

public class NodeId extends CustomType {

    private String nodeId;

    public NodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public RlpType getRlpEncodeData() {
        return RlpString.create(Numeric.hexStringToByteArray(nodeId));
    }
}
