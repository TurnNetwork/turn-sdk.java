package com.bubble.tx.gas;

import java.math.BigInteger;

public interface GasProvider {

    BigInteger getGasPrice();

    BigInteger getGasLimit();
}
