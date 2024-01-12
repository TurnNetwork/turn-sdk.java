package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Uint16;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class EditCandidateParam {

    /**
     * 64bytes 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;
    /**
     * 20bytes 用于接受出块奖励和质押奖励的收益账户
     */
    private String beneficiary;
    /**
     * 节点的名称(有长度限制，表示该节点的名称)
     */
    private String name;
    /**
     * 节点的描述(有长度限制，表示该节点的描述)
     */
    private String detail;

    public EditCandidateParam(Builder builder) {
        this.nodeId = builder.nodeId;
        this.beneficiary = builder.beneficiary;
        this.name = builder.name;
        this.detail = builder.detail;
    }

    @Override
    public String toString() {
        return "UpdateStakingParam{" +
                "nodeId='" + nodeId + '\'' +
                ", beneficiary='" + beneficiary + '\'' +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public List<Type> getSubmitInputParameters() {
        return Arrays.asList(
                new BytesType(Numeric.hexStringToByteArray(nodeId)),
                beneficiary == null ? null:new BytesType(Numeric.hexStringToByteArray(beneficiary)),
                name == null ? null : new Utf8String(name),
                detail == null ? null : new Utf8String(detail));
    }


    public static final class Builder {
        private String nodeId;
        private String beneficiary;
        private String name;
        private String detail;

        public Builder setNodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder setBeneficiary(String beneficiary) {
            this.beneficiary = beneficiary;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public EditCandidateParam build() {
            return new EditCandidateParam(this);
        }
    }
}
