package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.RestrictingPlan;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.req.CreateRestrictingParam;
import com.bubble.contracts.dpos.dto.resp.RestrictingItem;
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
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RestrictingPlanContract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static RestrictingPlanContract load(Web3j web3j) {
        return new RestrictingPlanContract(NetworkParameters.getDposContractAddressOfRestrctingPlan(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static RestrictingPlanContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new RestrictingPlanContract(NetworkParameters.getDposContractAddressOfRestrctingPlan(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static RestrictingPlanContract load(Web3j web3j, Credentials credentials) {
        return new RestrictingPlanContract(NetworkParameters.getDposContractAddressOfRestrctingPlan(),  web3j, credentials);
    }

    private RestrictingPlanContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private RestrictingPlanContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private RestrictingPlanContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return
     */
    public RemoteCall<TransactionResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 获取创建锁仓计划的GasProvider
     *
     * @param param CreateRestrictingParam
     * @return
     */
    public GasProvider getCreateRestrictingPlanGasProvider(CreateRestrictingParam param) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createRestrictingPlanFunction(param.getAccount(), Arrays.asList(param.getPlans()));
        return getDefaultGasProvider(function);
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取创建锁仓计划的gasProvider
     *
     * @param account
     * @param restrictingPlanList
     * @return
     */
    public GasProvider getCreateRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) throws IOException, EstimateGasException, NoSupportFunctionType {
    	Function function = createRestrictingPlanFunction(account, restrictingPlanList);
    	return getDefaultGasProvider(function);
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return
     */
    public RemoteCall<BubbleSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList) {
    	Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        Function function = createRestrictingPlanFunction(account, restrictingPlanList);
        return executeRemoteCallTransactionStep1(function,  gasProvider);
    }

    private Function createRestrictingPlanFunction(String account, List<RestrictingPlan> restrictingPlanList) {
    	Function function = new Function(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray<RestrictingPlan>(restrictingPlanList)));
        return function;
    }

    /**
     * 获取锁仓信息
     *
     * @param account 锁仓释放到账账户
     * @return
     */
    public RemoteCall<CallResponse<RestrictingItem>> getRestrictingInfo(String account) {
    	Function function = new Function(
                FunctionType.GET_RESTRICTINGINFO_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(account))));
        return executeRemoteCallObjectValueReturn(function, RestrictingItem.class);
    }
}
