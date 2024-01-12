package com.bubble.contracts.dpos.dto.req;

import com.bubble.contracts.dpos.abi.CustomType;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.utils.Numeric;

import java.math.BigInteger;

public class AccTokenAsset extends CustomType {

    /**
     * ERC20合约地址
     */
    private String tokenAddr;
    /**
     * Token余额
     */
    private BigInteger balance;

    public String getTokenAddr() {
        return tokenAddr;
    }

    public void setTokenAddr(String tokenAddr) {
        this.tokenAddr = tokenAddr;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    @Override
    public RlpType getRlpEncodeData() {
        return new RlpList(RlpString.create(Numeric.hexStringToByteArray(tokenAddr)), RlpString.create(balance));
    }
}
