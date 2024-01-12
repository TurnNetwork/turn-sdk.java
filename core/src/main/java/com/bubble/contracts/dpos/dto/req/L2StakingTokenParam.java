package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.Bool;
import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Int256;
import com.bubble.abi.solidity.datatypes.generated.Uint32;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class L2StakingTokenParam implements Cloneable {

    /**
     * 子链ID
     */
    private BigInteger bubbleId;
    /**
     * 质押的von
     */
    private AccountAsset stakingAsset;

    public BigInteger getBubbleId() {
        return bubbleId;
    }

    public void setBubbleId(BigInteger bubbleId) {
        this.bubbleId = bubbleId;
    }

    public AccountAsset getStakingAsset() {
        return stakingAsset;
    }

    public void setStakingAsset(AccountAsset stakingAsset) {
        this.stakingAsset = stakingAsset;
    }

    public L2StakingTokenParam(Builder builder) {
        this.stakingAsset = builder.stakingAsset;
        this.bubbleId = builder.bubbleId;

    }

    public List<Type> getSubmitInputParameters() {
        return Arrays.<Type>asList(new Int256(bubbleId)
                , stakingAsset
        );
    }

    @Override
    public L2StakingTokenParam clone() {
        L2StakingTokenParam stakingParam = null;
        try {
            stakingParam = (L2StakingTokenParam) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stakingParam;
    }

    public static final class Builder {

        private BigInteger bubbleId;
        /**
         * 质押的von
         */
        private AccountAsset stakingAsset;

        public Builder setBubbleId(BigInteger bubbleId) {
            this.bubbleId = bubbleId;
            return this;
        }

        public Builder setStakingAsset(AccountAsset stakingAsset) {
            this.stakingAsset = stakingAsset;
            return this;
        }

        public L2StakingTokenParam build() {
            return new L2StakingTokenParam(this);
        }
    }
}
