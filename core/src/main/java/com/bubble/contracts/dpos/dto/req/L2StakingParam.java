package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.*;
import com.bubble.abi.solidity.datatypes.generated.Bytes32;
import com.bubble.abi.solidity.datatypes.generated.Int256;
import com.bubble.abi.solidity.datatypes.generated.Uint16;
import com.bubble.abi.solidity.datatypes.generated.Uint32;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.bubble.contracts.dpos.dto.enums.StakingAmountType.AUTO_AMOUNT_TYPE;

public class L2StakingParam implements Cloneable {

    /**
     * 64bytes 被质押的节点Id(也叫候选人的节点Id)
     */
    private String nodeId;
    /**
     * 质押的von
     */
    private BigInteger amount;
    /**
     * 20bytes 用于接收质押奖励、出块奖励、验证奖励、代理奖励、零知识证明奖励的收益账户
     */
    private String beneficiary;
    /**
     * 被质押节点的名称(有长度限制，表示该节点的名称)
     */
    private String name;
    /**
     * 对外提供RPC服务的地址，格式如：http://IP:Port,  如果指定isOperator为True，则必填，否则使用空字符串即可“”
     */
    private String electronURI;

    /**
     * 节点的RPC服务地址，格式如：http://IP:Port，如果是运营节点，则不可留空
     */
    private String rpcURI;

    /**
     * 节点之间建立p2p连接的信息，格式：enode://nodeid@ip:port
     */
    private String p2pURI;

    /**
     * 节点的描述(有长度限制，表示该节点的描述)
     */
    private String details;
    /**
     * 程序的真实版本，治理rpc获取
     */
    private ProgramVersion processVersion;
    /**
     * bls的公钥
     */
    private String blsPubKey;

    /**
     * bls的证明
     */
    private String blsProof;

    /**
     * 节点是否运营节点，如果指定为True，则必须要填写RPC字段
     */
    private Boolean isOperator;


    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ProgramVersion getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(ProgramVersion processVersion) {
        this.processVersion = processVersion;
    }

    public String getBlsPubKey() {
        return blsPubKey;
    }

    public void setBlsPubKey(String blsPubKey) {
        this.blsPubKey = blsPubKey;
    }

    public String getBlsProof() {
        return blsProof;
    }

    public void setBlsProof(String blsProof) {
        this.blsProof = blsProof;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElectronURI() {
        return electronURI;
    }

    public void setElectronURI(String electronURI) {
        this.electronURI = electronURI;
    }

    public Boolean getOperator() {
        return isOperator;
    }

    public void setOperator(Boolean operator) {
        isOperator = operator;
    }

    public String getP2pURI() {
        return p2pURI;
    }

    public void setP2pURI(String p2pURI) {
        this.p2pURI = p2pURI;
    }

    public String getRpcURI() {
        return rpcURI;
    }

    public void setRpcURI(String rpcURI) {
        this.rpcURI = rpcURI;
    }

    public L2StakingParam(Builder builder) {
        this.nodeId = builder.nodeId;
        this.amount = builder.amount;
        this.beneficiary = builder.beneficiary;
        this.name = builder.name;
        this.details = builder.details;
        this.processVersion = builder.processVersion;
        this.blsPubKey = builder.blsPubKey;
        this.blsProof = builder.blsProof;
        this.electronURI = builder.electronURI;
        this.p2pURI = builder.p2pURI;
        this.rpcURI = builder.rpcURI;
        this.isOperator = builder.isOperator;

    }

    public List<Type> getSubmitInputParameters() {
        return Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId))
                , new Int256(amount)
                , new BytesType(Numeric.hexStringToByteArray(beneficiary))
                , name == null ? null : new Utf8String(name)
                , details == null ? null : new Utf8String(details)
                , electronURI == null ? null : new Utf8String(electronURI)
                , rpcURI == null ? null : new Utf8String(rpcURI)
                , p2pURI == null ? null : new Utf8String(p2pURI)
                , new Uint32(processVersion.getProgramVersion())
                , new BytesType(Numeric.hexStringToByteArray(processVersion.getProgramVersionSign()))
                , new BytesType(Numeric.hexStringToByteArray(blsPubKey))
                , new BytesType(Numeric.hexStringToByteArray(blsProof))
                , new Bool(isOperator)
        );
    }

    @Override
    public L2StakingParam clone() {
        L2StakingParam stakingParam = null;
        try {
            stakingParam = (L2StakingParam) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stakingParam;
    }

    public static final class Builder {

        private String nodeId;
        private BigInteger amount;

        private String beneficiary;

        private String name;

        private String electronURI;

        private String rpcURI;

        private String p2pURI;

        private String details;

        private ProgramVersion processVersion;

        private String blsPubKey;

        private String blsProof;

        private Boolean isOperator;

        public Builder setNodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder setAmount(BigInteger amount) {
            this.amount = amount;
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
        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setProcessVersion(ProgramVersion processVersion) {
            this.processVersion = processVersion;
            return this;
        }

        public Builder setBlsPubKey(String blsPubKey) {
            this.blsPubKey = blsPubKey;
            return this;
        }
        
        public Builder setBlsProof(String blsProof) {
            this.blsProof = blsProof;
            return this;
        }

        public Builder setElectronURI(String electronURI) {
            this.electronURI = electronURI;
            return this;
        }

        public Builder setOperator(Boolean operator) {
            isOperator = operator;
            return this;
        }

        public Builder setP2pURI(String p2pURI) {
            this.p2pURI = p2pURI;
            return this;
        }

        public Builder setRpcURI(String rpcURI) {
            this.rpcURI = rpcURI;
            return this;
        }

        public L2StakingParam build() {
            return new L2StakingParam(this);
        }
    }
}
