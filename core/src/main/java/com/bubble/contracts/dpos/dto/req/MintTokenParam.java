package com.bubble.contracts.dpos.dto.req;

import com.alibaba.fastjson.JSONObject;
import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Bytes32;
import com.bubble.abi.solidity.datatypes.generated.Int256;
import com.bubble.abi.solidity.datatypes.generated.Uint16;
import com.bubble.abi.solidity.datatypes.generated.Uint32;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.dto.RestrictingPlan;
import com.bubble.utils.Bytes;
import com.bubble.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MintTokenParam implements Cloneable {

    /**
     * 主链质押Token交易Hash
     */
    private String l1StakingTokenTxHash;
    /**
     * 质押的von
     */
    private AccountAsset accountAsset;

    public String getL1StakingTokenTxHash() {
        return l1StakingTokenTxHash;
    }

    public void setL1StakingTokenTxHash(String l1StakingTokenTxHash) {
        this.l1StakingTokenTxHash = l1StakingTokenTxHash;
    }

    public AccountAsset getStakingAsset() {
        return accountAsset;
    }

    public void setStakingAsset(AccountAsset accountAsset) {
        this.accountAsset = accountAsset;
    }

    public MintTokenParam(Builder builder) {
        this.accountAsset = builder.accountAsset;
        this.l1StakingTokenTxHash = builder.l1StakingTokenTxHash;
    }

    @Override
    public MintTokenParam clone() {
        MintTokenParam stakingParam = null;
        try {
            stakingParam = (MintTokenParam) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stakingParam;
    }

    public static final class Builder {

        private String l1StakingTokenTxHash;;

        private AccountAsset accountAsset;

        public Builder setL1StakingTokenTxHash(String l1StakingTokenTxHash) {
            this.l1StakingTokenTxHash = l1StakingTokenTxHash;
            return this;
        }

        public Builder setStakingAsset(AccountAsset accountAsset) {
            this.accountAsset = accountAsset;
            return this;
        }

        public MintTokenParam build() {
            return new MintTokenParam(this);
        }
    }

    public List<Type> getSubmitInputParameters() {
        return Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(l1StakingTokenTxHash))
                , accountAsset);

    }
}
