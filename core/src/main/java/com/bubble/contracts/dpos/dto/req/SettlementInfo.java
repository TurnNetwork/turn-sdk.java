package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.abi.CustomType;
import com.bubble.rlp.solidity.RlpEncoder;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.utils.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettlementInfo extends CustomType implements Type {

    /**
     * 质押的von
     */
    private AccountAsset[] settlementInfo;

    public AccountAsset[] getSettlementInfo() {
        return settlementInfo;
    }

    public void setSettlementInfo(AccountAsset[] settlementInfo) {
        this.settlementInfo = settlementInfo;
    }

    @Override
    public RlpType getRlpEncodeData() {
        List<RlpType> rlpListList = new ArrayList<>();
        List<RlpType> accTokenAssetsList = new ArrayList<>();
        for (AccountAsset accountAsset : settlementInfo) {
            accTokenAssetsList.add(accountAsset.getRlpEncodeData2());
        }
        rlpListList.add(new RlpList(accTokenAssetsList));
        return RlpString.create(RlpEncoder.encode(new RlpList(rlpListList)));
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getTypeAsString() {
        return null;
    }
}
