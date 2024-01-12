package com.bubble.contracts.token;

import com.bubble.protocol.core.DefaultBlockParameter;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;

/**
 * Describes the Ethereum "Basic" subset of the ERC-20 token standard.
 * <p>
 *     Implementations should provide the concrete <code>TransferEventResponse</code>
 *     from their token as the generic type "T".
 * </p>
 *
 * @see <a href="https://github.com/ethereum/EIPs/issues/179">ERC: Simpler Token Standard #179</a>
 * @see <a href="https://github.com/OpenZeppelin/zeppelin-solidity/blob/master/contracts/token/ERC20Basic.sol">OpenZeppelin's zeppelin-solidity reference implementation</a>
 */
@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
    
    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Observable<T> transferEventObservable(DefaultBlockParameter startBlock,
                                          DefaultBlockParameter endBlock);

}
