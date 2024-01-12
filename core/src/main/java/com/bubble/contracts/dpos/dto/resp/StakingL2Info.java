package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

public class StakingL2Info {

    /**
     * 节点ID
     */
    @JSONField(name = "NodeId")
    private String nodeId;

    /**
     * 质押时Bls公钥
     */
    @JSONField(name = "BlsPubKey")
    private String BlsPubKey;

    /**
     * 质押时使用的账户
     */
    @JSONField(name = "StakingAddress")
    private String stakingAddress;

    /**
     * 节点收益地址
     */
    @JSONField(name = "BenefitAddress")
    private String benefitAddress;

    /**
     * 质押交易在块高的交易索引
     */
    @JSONField(name = "StakingTxIndex")
    private BigInteger stakingTxIndex;

    /**
     * 程序版本
     */
    @JSONField(name = "ProgramVersion")
    private BigInteger programVersion;

    /**
     * 节点状态
     */
    @JSONField(name = "Status")
    private BigInteger status;

    /**
     * 质押或更新节点信息的周期数
     */
    @JSONField(name = "StakingEpoch")
    private BigInteger stakingEpoch;

    /**
     * 质押时的块高
     */
    @JSONField(name = "StakingBlockNum")
    private BigInteger stakingBlockNum;

    /**
     * 当前节点总共质押的token数量
     */
    /*@JSONField(name = "Shares")
    private BigInteger shares;*/

    /**
     * 发起质押账户的自由金额的锁定期质押的token数量
     */
    /*@JSONField(name = "Released")
    private BigInteger released;*/

    /**
     * 发起质押账户的自由金额的犹豫期质押的token数量
     */
//    @JSONField(name = "ReleasedHes")
//    private BigInteger releasedHes;

    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "Detail")
    private String detail;
    @JSONField(name = "ElectronURI")
    private String electronURI;
    @JSONField(name = "P2PURI")
    private String p2PUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getElectronURI() {
        return electronURI;
    }

    public void setElectronURI(String electronURI) {
        this.electronURI = electronURI;
    }

    public String getP2PUri() {
        return p2PUri;
    }

    public void setP2PUri(String p2PUri) {
        this.p2PUri = p2PUri;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getBlsPubKey() {
        return BlsPubKey;
    }

    public void setBlsPubKey(String blsPubKey) {
        BlsPubKey = blsPubKey;
    }

    public String getStakingAddress() {
        return stakingAddress;
    }

    public void setStakingAddress(String stakingAddress) {
        this.stakingAddress = stakingAddress;
    }

    public String getBenefitAddress() {
        return benefitAddress;
    }

    public void setBenefitAddress(String benefitAddress) {
        this.benefitAddress = benefitAddress;
    }

    public BigInteger getStakingTxIndex() {
        return stakingTxIndex;
    }

    public void setStakingTxIndex(BigInteger stakingTxIndex) {
        this.stakingTxIndex = stakingTxIndex;
    }

    public BigInteger getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion(BigInteger programVersion) {
        this.programVersion = programVersion;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public BigInteger getStakingEpoch() {
        return stakingEpoch;
    }

    public void setStakingEpoch(BigInteger stakingEpoch) {
        this.stakingEpoch = stakingEpoch;
    }

    public BigInteger getStakingBlockNum() {
        return stakingBlockNum;
    }

    public void setStakingBlockNum(BigInteger stakingBlockNum) {
        this.stakingBlockNum = stakingBlockNum;
    }

/*    public BigInteger getShares() {
        return shares;
    }

    public void setShares(BigInteger shares) {
        this.shares = shares;
    }*/

/*    public BigInteger getReleased() {
        return released;
    }

    public void setReleased(BigInteger released) {
        this.released = released;
    }*/

//    public BigInteger getReleasedHes() {
//        return releasedHes;
//    }
//
//    public void setReleasedHes(BigInteger releasedHes) {
//        this.releasedHes = releasedHes;
//    }
}
