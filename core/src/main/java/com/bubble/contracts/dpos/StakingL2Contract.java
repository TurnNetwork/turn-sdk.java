package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.generated.Uint16;
import com.bubble.abi.solidity.datatypes.generated.Uint256;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.contracts.dpos.dto.req.L2StakingParam;
import com.bubble.contracts.dpos.dto.req.StakingParam;
import com.bubble.contracts.dpos.dto.req.EditCandidateParam;
import com.bubble.contracts.dpos.dto.resp.StakingL2Info;
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

public class StakingL2Contract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static StakingL2Contract load(Web3j web3j) {
        return new StakingL2Contract(NetworkParameters.getDposContractAddressOfL2Staking(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static StakingL2Contract load(Web3j web3j, TransactionManager transactionManager) {
    	return new StakingL2Contract(NetworkParameters.getDposContractAddressOfL2Staking(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static StakingL2Contract load(Web3j web3j, Credentials credentials) {
    	return new StakingL2Contract(NetworkParameters.getDposContractAddressOfL2Staking(), web3j, credentials);
    }

    private StakingL2Contract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private StakingL2Contract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private StakingL2Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 发起质押
     *
     * @param stakingParam
     * @return
     * @see StakingParam
     */
    public RemoteCall<TransactionResponse> staking(L2StakingParam stakingParam) {
        Function function = createStakingFunction(stakingParam);
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
    public RemoteCall<TransactionResponse> staking(L2StakingParam stakingParam, GasProvider gasProvider)  {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取质押gasProvider
     *
     * @param stakingParam
     * @return
     */
    public GasProvider getStakingGasProvider(L2StakingParam stakingParam) throws IOException, EstimateGasException, NoSupportFunctionType {
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
    public RemoteCall<BubbleSendTransaction> stakingReturnTransaction(L2StakingParam stakingParam)  {
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
    public RemoteCall<BubbleSendTransaction> stakingReturnTransaction(L2StakingParam stakingParam, GasProvider gasProvider) {
        Function function = createStakingFunction(stakingParam);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createStakingFunction(L2StakingParam stakingParam)  {
        Function function = new Function(
                FunctionType.L2_STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());
        return function;
    }

    /**
     * 获取节点信息
     *
     * @param nodeId
     * @return
     */
    public RemoteCall<CallResponse<StakingL2Info>> getCandidateInfo(String nodeId) {
        Function function = new Function(FunctionType.GET_L2_STAKING_INFO_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return executeRemoteCallObjectValueReturn(function, StakingL2Info.class);
    }



    /**
     * 撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<TransactionResponse> unStaking(String nodeId) {
        Function function = createUnStakingFunction(nodeId);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<TransactionResponse> unStaking(String nodeId, GasProvider gasProvider) {
        Function function = createUnStakingFunction(nodeId);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取撤销质押的gasProvider
     *
     * @param nodeId
     * @return
     */
    public GasProvider getUnStakingGasProvider(String nodeId) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createUnStakingFunction(nodeId);
        return getDefaultGasProvider(function);
    }

    /**
     * 撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<BubbleSendTransaction> unStakingReturnTransaction(String nodeId) {
        Function function = createUnStakingFunction(nodeId);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 撤销质押
     *
     * @param nodeId      64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @param gasProvider 自定义的gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> unStakingReturnTransaction(String nodeId, GasProvider gasProvider) {
        Function function = createUnStakingFunction(nodeId);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createUnStakingFunction(String nodeId) {
        Function function = new Function(FunctionType.WITHDREW_L2_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return function;
    }

    /**
     * 更新节点信息
     *
     * @param editCandidateParam
     * @return
     */
    public RemoteCall<TransactionResponse> editCandidate(EditCandidateParam editCandidateParam) {
        Function function = createUpdateStakingFunction(editCandidateParam);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 更新质押信息
     *
     * @param editCandidateParam
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> updateStakingInfo(EditCandidateParam editCandidateParam, GasProvider gasProvider) {
        Function function = createUpdateStakingFunction(editCandidateParam);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 获取更新质押信息GasProvider
     *
     * @param editCandidateParam
     * @return
     */
    public GasProvider getUpdateStakingInfoGasProvider(EditCandidateParam editCandidateParam) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createUpdateStakingFunction(editCandidateParam);
        return getDefaultGasProvider(function);
    }

    /**
     * 更新节点信息
     *
     * @param editCandidateParam
     * @return
     */
    public RemoteCall<BubbleSendTransaction> updateStakingInfoReturnTransaction(EditCandidateParam editCandidateParam) {
        Function function = createUpdateStakingFunction(editCandidateParam);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 更新质押信息
     *
     * @param editCandidateParam
     * @return
     */
    public RemoteCall<BubbleSendTransaction> updateStakingInfoReturnTransaction(EditCandidateParam editCandidateParam, GasProvider gasProvider) {
        Function function = createUpdateStakingFunction(editCandidateParam);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createUpdateStakingFunction(EditCandidateParam editCandidateParam) {
        Function function = new Function(FunctionType.UPDATE_L2_STAKING_INFO_FUNC_TYPE,
                editCandidateParam.getSubmitInputParameters());
        return function;
    }

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @return
     */
    public RemoteCall<TransactionResponse> addStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = createAddStakingFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> addStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createAddStakingFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取增持质押gasProvider
     *
     * @param nodeId
     * @param stakingAmountType
     * @param amount
     * @return
     */
    public GasProvider getAddStakingGasProvider(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createAddStakingFunction(nodeId, stakingAmountType, amount);
        return getDefaultGasProvider(function);
    }

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @return
     */
    public RemoteCall<BubbleSendTransaction> addStakingReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = createAddStakingFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @param gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> addStakingReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createAddStakingFunction(nodeId, stakingAmountType, amount );
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createAddStakingFunction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.ADD_L2_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)));
        return function;
    }
}
