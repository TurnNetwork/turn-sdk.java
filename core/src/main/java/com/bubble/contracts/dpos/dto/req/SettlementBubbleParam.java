
package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Int16;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SettlementBubbleParam {

    /**
     * 子链结算交易Hash
     */
    private String l2SettleTxHash;

    /**
     * 子链id
     */

    private BigInteger bubbleId;

    /**
     * 要结算的账户原生代币资产，key为账户地址，value为Token数量
     */

    private SettlementInfo settlementInfo;

    public SettlementBubbleParam(Builder builder) {
        this.bubbleId = builder.bubbleId;
        this.l2SettleTxHash = builder.l2SettleTxHash;
        this.settlementInfo = builder.settlementInfo;
    }

    public List<Type> getSubmitInputParameters() {
        return Arrays.asList(
                l2SettleTxHash == null ? null: new BytesType(Numeric.hexStringToByteArray(l2SettleTxHash)),
                bubbleId == null ? null: new Uint(bubbleId),
                settlementInfo);
    }


    public static final class Builder {

        /**
         * 子链结算交易Hash
         */
        private String l2SettleTxHash;

        /**
         * 子链id
         */

        private BigInteger bubbleId;

        /**
         * 要结算的账户原生代币资产，key为账户地址，value为Token数量
         */

        private SettlementInfo settlementInfo;

        public Builder setBubbleId(BigInteger bubbleId) {
            this.bubbleId = bubbleId;
            return this;
        }

        public Builder setL2SettleTxHash(String l2SettleTxHash) {
            this.l2SettleTxHash = l2SettleTxHash;
            return this;
        }

        public Builder setSettlementInfo(SettlementInfo settlementInfo) {
            this.settlementInfo = settlementInfo;
            return this;
        }

        public SettlementBubbleParam build() {
            return new SettlementBubbleParam(this);
        }
    }
}

