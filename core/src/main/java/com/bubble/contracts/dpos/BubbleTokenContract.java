package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.req.AccountAsset;
import com.bubble.contracts.dpos.dto.req.L2StakingTokenParam;
import com.bubble.contracts.dpos.dto.req.MintTokenParam;
import com.bubble.contracts.dpos.dto.req.StakingParam;
import com.bubble.contracts.dpos.exception.EstimateGasException;
import com.bubble.contracts.dpos.exception.NoSupportFunctionType;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class BubbleTokenContract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static BubbleTokenContract load(Web3j web3j) {
        return new BubbleTokenContract(NetworkParameters.getDposContractAddressOfBubbleToken(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static BubbleTokenContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new BubbleTokenContract(NetworkParameters.getDposContractAddressOfBubbleToken(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static BubbleTokenContract load(Web3j web3j, Credentials credentials) {
    	return new BubbleTokenContract(NetworkParameters.getDposContractAddressOfBubbleToken(), web3j, credentials);
    }

    private BubbleTokenContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private BubbleTokenContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private BubbleTokenContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 铸币
     * @param mintTokenParam
     * @return
     */
    public RemoteCall<BubbleSendTransaction> mintToken(MintTokenParam mintTokenParam) {
        Function function = new Function(FunctionType.MINT_TOKEN_FUNC_TYPE,
                mintTokenParam.getSubmitInputParameters());
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 结算
     *
     * @return
     */
    public RemoteCall<BubbleSendTransaction> settleBubble() {
        Function function = new Function(FunctionType.L2_SETTLE_BUBBLE_FUNC_TYPE,
                Arrays.asList());
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 根据主链Hash获取子链Hash
     *
     * @param l1StakingTokenTxHash
     * @return
     * @see StakingParam
     */
    public RemoteCall<CallResponse<String>> getL2HashByL1Hash(String l1StakingTokenTxHash) {
        Function function = new Function(FunctionType.GET_L2_HASH_BY_L1_HASH_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(l1StakingTokenTxHash))));
        return executeRemoteCallObjectValueReturn(function,String.class);
    }

    @Override
    public RemoteCall<TransactionResponse> getTransactionResponse(BubbleSendTransaction ethSendTransaction) {
        return super.getTransactionResponse2(ethSendTransaction);
    }
}
