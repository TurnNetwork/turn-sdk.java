package com.bubble.contracts.dpos.dto.resp;

import java.math.BigInteger;

public class CandidateMutable {

    private Long status;
    private Long stakingEpoch;
    private BigInteger shares;
    private BigInteger released;
    private BigInteger ReleasedHes;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStakingEpoch() {
        return stakingEpoch;
    }

    public void setStakingEpoch(Long stakingEpoch) {
        this.stakingEpoch = stakingEpoch;
    }

    public BigInteger getShares() {
        return shares;
    }

    public void setShares(BigInteger shares) {
        this.shares = shares;
    }

    public BigInteger getReleased() {
        return released;
    }

    public void setReleased(BigInteger released) {
        this.released = released;
    }

    public BigInteger getReleasedHes() {
        return ReleasedHes;
    }

    public void setReleasedHes(BigInteger releasedHes) {
        ReleasedHes = releasedHes;
    }
}
