package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.generated.Uint16;
import com.bubble.abi.solidity.datatypes.generated.Uint256;
import com.bubble.abi.solidity.datatypes.generated.Uint64;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.enums.DelegateAmountType;
import com.bubble.contracts.dpos.dto.enums.StakingAmountType;
import com.bubble.contracts.dpos.dto.resp.*;
import com.bubble.contracts.dpos.exception.EstimateGasException;
import com.bubble.contracts.dpos.exception.NoSupportFunctionType;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.exceptions.TransactionException;
import com.bubble.rlp.solidity.RlpDecoder;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DelegateContract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static DelegateContract load(Web3j web3j) {
        return new DelegateContract(NetworkParameters.getDposContractAddressOfStaking(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static DelegateContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new DelegateContract(NetworkParameters.getDposContractAddressOfStaking(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static DelegateContract load(Web3j web3j, Credentials credentials) {
        return new DelegateContract(NetworkParameters.getDposContractAddressOfStaking(), web3j, credentials);
    }

    private DelegateContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private DelegateContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private DelegateContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    @Deprecated
    public RemoteCall<TransactionResponse> delegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param delegateAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额  3: 委托锁定金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<TransactionResponse> delegate(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount) {
        Function function = createDelegateFunction(nodeId, delegateAmountType, amount);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    @Deprecated
    public RemoteCall<TransactionResponse> delegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param delegateAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额  3: 委托锁定金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> delegate(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createDelegateFunction(nodeId, delegateAmountType, amount);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 发起委托的gasProvider
     *
     * @param nodeId
     * @param stakingAmountType
     * @param amount
     * @return
     */
    @Deprecated
    public GasProvider getDelegateGasProvider(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return 	getDefaultGasProvider(function);
    }

    /**
     * 发起委托的gasProvider
     *
     * @param nodeId
     * @param delegateAmountType
     * @param amount
     * @return
     */
    public GasProvider getDelegateGasProvider(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createDelegateFunction(nodeId, delegateAmountType, amount);
        return 	getDefaultGasProvider(function);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    @Deprecated
    public RemoteCall<BubbleSendTransaction> delegateReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param delegateAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额  3: 委托锁定金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<BubbleSendTransaction> delegateReturnTransaction(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount) {
        Function function = createDelegateFunction(nodeId, delegateAmountType, amount);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> delegateReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createDelegateFunction(nodeId, stakingAmountType, amount );
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param delegateAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额  3: 委托锁定金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> delegateReturnTransaction(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount, GasProvider gasProvider) {
        Function function = createDelegateFunction(nodeId, delegateAmountType, amount );
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createDelegateFunction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        return createDelegateFunction(nodeId, stakingAmountType.getValue(), amount);
    }

    private Function createDelegateFunction(String nodeId, DelegateAmountType delegateAmountType, BigInteger amount) {
        return createDelegateFunction(nodeId, delegateAmountType.getValue(), amount);
    }

    private Function createDelegateFunction(String nodeId, int delegateAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(delegateAmountType)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
        return function;
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<TransactionResponse> unDelegate(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        Function function =  createUnDelegateFunction(nodeId, stakingBlockNum, amount);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param gasProvider     用户指定的gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> unDelegate(String nodeId, BigInteger stakingBlockNum, BigInteger amount, GasProvider gasProvider) {
        Function function = createUnDelegateFunction(nodeId, stakingBlockNum, amount);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)的gasProvider
     *
     * @param nodeId
     * @param stakingBlockNum
     * @param amount
     * @return
     */
    public GasProvider getUnDelegateGasProvider(String nodeId, BigInteger stakingBlockNum, BigInteger amount) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createUnDelegateFunction(nodeId, stakingBlockNum, amount);
    	return getDefaultGasProvider(function);
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<BubbleSendTransaction> unDelegateReturnTransaction(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        Function function = createUnDelegateFunction(nodeId, stakingBlockNum, amount);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<BubbleSendTransaction> unDelegateReturnTransaction(String nodeId, BigInteger stakingBlockNum, BigInteger amount, GasProvider gasProvider) {
        Function function = createUnDelegateFunction(nodeId, stakingBlockNum, amount);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createUnDelegateFunction(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
    	Function function = new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
        return function;
    }

    /**
     *  获得解除委托时所提取的委托收益（当减持/撤销委托成功时调用）
     *
     * @param transactionReceipt
     * @return
     * @throws TransactionException
     */
    @Deprecated
    public BigInteger decodeUnDelegateLog(TransactionReceipt transactionReceipt) throws TransactionException {
        return  decodeUnDelegateLogOfNew(transactionReceipt).getDelegateIncome();
    }

    /**
     *  获得解除委托时日志信息（当减持/撤销委托成功时调用）
     *
     * @param transactionReceipt
     * @return
     * @throws TransactionException
     */
    public UnDelegation decodeUnDelegateLogOfNew(TransactionReceipt transactionReceipt) throws TransactionException {
        List<RlpType> rlpList = decodePPOSLog(transactionReceipt);
        UnDelegation result = new UnDelegation();
        BigInteger delegateIncome = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes()).getValues().get(0)).asPositiveBigInteger();
        result.setDelegateIncome(delegateIncome);
        if(rlpList.size() > 2){
            BigInteger released = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(2)).getBytes()).getValues().get(0)).asPositiveBigInteger();
            BigInteger restrictingPlan = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(3)).getBytes()).getValues().get(0)).asPositiveBigInteger();
            BigInteger lockReleased = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(4)).getBytes()).getValues().get(0)).asPositiveBigInteger();
            BigInteger lockRestrictingPlan =((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(5)).getBytes()).getValues().get(0)).asPositiveBigInteger();
            result.setReleased(Optional.of(released));
            result.setRestrictingPlan(Optional.of(restrictingPlan));
            result.setLockReleased(Optional.of(lockReleased));
            result.setLockRestrictingPlan(Optional.of(lockRestrictingPlan));
        }
        return result;
    }

    /**
     * 领取解锁的委托
     *
     * @return
     */
    public RemoteCall<TransactionResponse> redeemDelegation() {
        Function function =  createRedeemDelegationFunction();
        return executeRemoteCallTransaction(function);
    }

    /**
     * 领取解锁的委托
     *
     * @return
     */
    public RemoteCall<TransactionResponse> redeemDelegation(GasProvider gasProvider) {
        Function function = createRedeemDelegationFunction();
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 领取解锁的委托
     *
     * @return
     */
    public GasProvider getRedeemDelegationGasProvider() throws IOException, EstimateGasException {
        Function function = createRedeemDelegationFunction();
        return getDefaultGasProvider(function);
    }

    /**
     * 领取解锁的委托
     *
     * @return
     */
    public RemoteCall<BubbleSendTransaction> redeemDelegationReturnTransaction() {
        Function function = createRedeemDelegationFunction();
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 领取解锁的委托
     *
     * @return
     */
    public RemoteCall<BubbleSendTransaction> redeemDelegationReturnTransaction(GasProvider gasProvider) {
        Function function = createRedeemDelegationFunction();
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createRedeemDelegationFunction() {
        Function function = new Function(FunctionType.REDEEM_DELEGATE_FUNC_TYPE);
        return function;
    }

    /**
     *  获得领取解锁的委托日志（当减持/撤销委托成功时调用）
     *
     * @param transactionReceipt
     * @return
     * @throws TransactionException
     */
    public RedeemDelegation decodeRedeemDelegateLog(TransactionReceipt transactionReceipt) throws TransactionException {
        List<RlpType> rlpList = decodePPOSLog(transactionReceipt);
        RedeemDelegation result = new RedeemDelegation();
        BigInteger released = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes()).getValues().get(0)).asPositiveBigInteger();
        BigInteger restrictingPlan = ((RlpString) RlpDecoder.decode(((RlpString)rlpList.get(2)).getBytes()).getValues().get(0)).asPositiveBigInteger();
        result.setReleased(released);
        result.setRestrictingPlan(restrictingPlan);
        return result;
    }

    /**
     * 查询当前单个委托信息
     *
     * @param nodeId          验证人的节点Id
     * @param delAddr         委托人账户地址
     * @param stakingBlockNum 发起质押时的区块高度
     * @return
     */
    public RemoteCall<CallResponse<Delegation>> getDelegateInfo(String nodeId, String delAddr, BigInteger stakingBlockNum) {

        Function function = new Function(FunctionType.GET_DELEGATEINFO_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(delAddr))
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))));

        return executeRemoteCallObjectValueReturn(function, Delegation.class);
    }

    /**
     * 查询当前账户地址所委托的节点的NodeID和质押Id
     *
     * @param address
     * @return
     */
    public RemoteCall<CallResponse<List<DelegationIdInfo>>> getRelatedListByDelAddr(String address) {
        Function function = new Function(FunctionType.GET_DELEGATELIST_BYADDR_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(address))));
        return executeRemoteCallListValueReturn(function, DelegationIdInfo.class);
    }

    /**
     * 查询账户处于锁定期与解锁期的委托信息
     *
     * @param delAddr         委托人账户地址
     * @return
     */
    public RemoteCall<CallResponse<DelegationLockInfo>> getDelegationLockInfo(String delAddr) {
        Function function = new Function(FunctionType.GET_DELEGATIONLOCKINFO_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(delAddr))));
        return executeRemoteCallObjectValueReturn(function, DelegationLockInfo.class);
    }
}
