package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.abi.solidity.datatypes.generated.Int256;
import com.bubble.abi.solidity.datatypes.generated.Uint8;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.req.L2StakingTokenParam;
import com.bubble.contracts.dpos.dto.req.SettlementBubbleParam;
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

public class BubbleContract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static BubbleContract load(Web3j web3j) {
        return new BubbleContract(NetworkParameters.getDposContractAddressOfBubble(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static BubbleContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new BubbleContract(NetworkParameters.getDposContractAddressOfBubble(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static BubbleContract load(Web3j web3j, Credentials credentials) {
    	return new BubbleContract(NetworkParameters.getDposContractAddressOfBubble(), web3j, credentials);
    }

    private BubbleContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private BubbleContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private BubbleContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }
    
    /**
     * 创建子链
     *
     * @return
     */
    public RemoteCall<TransactionResponse> createBubble() {
        Function function = new Function(FunctionType.CREATE_BUBBLE_FUNC_TYPE,
        		Arrays.asList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * 释放子链
     *
     * @param bubbleId
     * @return
     */
    public RemoteCall<TransactionResponse> releaseBubble(BigInteger bubbleId) {
        Function function = new Function(FunctionType.RELEASE_BUBBLE_FUNC_TYPE,
                Arrays.asList(new Uint(bubbleId)));
        return executeRemoteCallTransaction(function);
    }

    /**
     * 发起质押Token
     *
     * @param stakingTokenParam
     * @return
     * @see StakingParam
     */
    public RemoteCall<TransactionResponse> stakingToken(L2StakingTokenParam stakingTokenParam) {
        Function function = createStakingFunction(stakingTokenParam);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 发起质押
     *
     * @param stakingParam
     * @param gasProvider
     * @return
     * @see StakingParam
     */
    public RemoteCall<TransactionResponse> staking(L2StakingTokenParam stakingParam, GasProvider gasProvider)  {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取质押gasProvider
     *
     * @param stakingParam
     * @return
     */
    public GasProvider getStakingGasProvider(L2StakingTokenParam stakingParam) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createStakingFunction(stakingParam);
        return getDefaultGasProvider(function);
    }


    /**
     * 发起质押
     *
     * @param stakingParam
     * @return
     * @see StakingParam
     */
    public RemoteCall<BubbleSendTransaction> stakingReturnTransaction(L2StakingTokenParam stakingParam)  {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 发起质押
     *
     * @param stakingParam
     * @param gasProvider
     * @return
     * @see StakingParam
     */
    public RemoteCall<BubbleSendTransaction> stakingReturnTransaction(L2StakingTokenParam stakingParam, GasProvider gasProvider) {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createStakingFunction(L2StakingTokenParam stakingParam)  {
        Function function = new Function(
                FunctionType.L2_TOKEN_STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());
        return function;
    }



    /**
     * 赎回Token
     *
     * @param bubbleId 子链ID
     * @return
     */
    public RemoteCall<TransactionResponse> withdrewToken(BigInteger bubbleId) {
        Function function = createWithdrewTokenFunction(bubbleId);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 撤销质押
     *
     * @param bubbleId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<TransactionResponse> withdrewToken(BigInteger bubbleId, GasProvider gasProvider) {
        Function function = createWithdrewTokenFunction(bubbleId);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取撤销质押的gasProvider
     *
     * @param bubbleId
     * @return
     */
    public GasProvider getWithdrewTokenGasProvider(BigInteger bubbleId) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createWithdrewTokenFunction(bubbleId);
        return getDefaultGasProvider(function);
    }

    /**
     * 撤销质押
     *
     * @param bubbleId 子链ID
     * @return
     */
    public RemoteCall<BubbleSendTransaction> withdrewTokenReturnTransaction(BigInteger bubbleId) {
        Function function = createWithdrewTokenFunction(bubbleId);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 撤销质押
     *
     * @param bubbleId     子链ID
     * @param gasProvider 自定义的gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> withdrewTokenReturnTransaction(BigInteger bubbleId, GasProvider gasProvider) {
        Function function = createWithdrewTokenFunction(bubbleId);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createWithdrewTokenFunction(BigInteger bubbleId) {
        Function function = new Function(FunctionType.WITHDREW_L2_TOKEN_STAKING_FUNC_TYPE,
                Arrays.asList(new Uint(bubbleId)));
        return function;
    }

    /**
     * 结算
     * @param settlementBubbleParam
     * @return
     */
    public RemoteCall<BubbleSendTransaction> settleBubble(SettlementBubbleParam settlementBubbleParam) {
        Function function = createSettleBubbleFunction(settlementBubbleParam);
        return executeRemoteCallTransactionStep1(function);
    }

    private Function createSettleBubbleFunction(SettlementBubbleParam settlementBubbleParam) {
        Function function = new Function(FunctionType.SETTLE_BUBBLE_FUNC_TYPE,
                settlementBubbleParam.getSubmitInputParameters());
        return function;
    }

    /**
     * 获取子链信息
     *
     * @param bubbleId 子链ID
     * @return
     */
    public RemoteCall<CallResponse<String>> getBubbleInfo(BigInteger bubbleId) {
        Function function = createGetBubbleInfoFunction(bubbleId);
        return executeRemoteCallObjectValueReturn(function,String.class);
    }

    private Function createGetBubbleInfoFunction(BigInteger bubbleId) {
        Function function = new Function(FunctionType.GET_BUBBLE_INFO_FUNC_TYPE,
                Arrays.asList(new Uint(bubbleId)));
        return function;
    }

    /**
     * 根据子链Hash查询主链Hash
     *
     * @param bubbleId
     * @param l2TxHash
     * @return
     */
    public RemoteCall<CallResponse<String>> getL1HashByL2Hash(BigInteger bubbleId, String l2TxHash) {
        Function function = new Function(FunctionType.GET_L1_HASH_BY_L2_HASH_FUNC_TYPE,
                Arrays.asList(new Uint(bubbleId)
                , new BytesType(Numeric.hexStringToByteArray(l2TxHash))));
        return executeRemoteCallObjectValueReturn(function, String.class);
    }

    /**
     * 获取Bubble网络在主链上的交易hash列表
     *
     * @param bubbleId
     * @param txType Bubble交易类型，0：StakingToken，1：WithdrewToken，2：SettleBubble
     * @return
     */
    public RemoteCall<CallResponse<String>> getBubTxHashList(BigInteger bubbleId, Integer txType) {
        Function function = new Function(FunctionType.GET_BUB_TX_HASH_LIST_FUNC_TYPE,
                Arrays.asList(new Uint(bubbleId)
                             ,new Uint8(txType)));
        return executeRemoteCallObjectValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<TransactionResponse> getTransactionResponse(BubbleSendTransaction ethSendTransaction) {
        return super.getTransactionResponse2(ethSendTransaction);
    }

}
