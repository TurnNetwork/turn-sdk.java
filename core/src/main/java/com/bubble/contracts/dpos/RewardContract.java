package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.abi.custom.NodeId;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.ErrorCode;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.resp.Reward;
import com.bubble.contracts.dpos.exception.EstimateGasException;
import com.bubble.contracts.dpos.exception.NoSupportFunctionType;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.methods.response.Log;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.exceptions.TransactionException;
import com.bubble.rlp.solidity.RlpDecoder;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RewardContract extends BaseContract {
	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static RewardContract load(Web3j web3j) {
        return new RewardContract(NetworkParameters.getDposContractAddressOfReward(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static RewardContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new RewardContract(NetworkParameters.getDposContractAddressOfReward(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static RewardContract load(Web3j web3j, Credentials credentials) {
        return new RewardContract(NetworkParameters.getDposContractAddressOfReward(),  web3j, credentials);
    }

    private RewardContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private RewardContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private RewardContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }


    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @return
     */
    public RemoteCall<TransactionResponse> withdrawDelegateReward() {
        Function function = createWithdrawDelegateRewardFunction();
        return executeRemoteCallTransaction(function);
    }

    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> withdrawDelegateReward(GasProvider gasProvider) {
        Function function = createWithdrawDelegateRewardFunction();
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 提取账户当前所有的可提取的委托奖励的gasProvider
     *
     * @return
     */
    public GasProvider getWithdrawDelegateRewardGasProvider() throws IOException, EstimateGasException, NoSupportFunctionType {
    	Function function = createWithdrawDelegateRewardFunction();
    	return getDefaultGasProvider(function);
    }

    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @return
     */
    public RemoteCall<BubbleSendTransaction> withdrawDelegateRewardReturnTransaction() {
        Function function = createWithdrawDelegateRewardFunction();
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @param gasProvider       用户指定的gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> withdrawDelegateRewardReturnTransaction( GasProvider gasProvider) {
        Function function = createWithdrawDelegateRewardFunction();
        return executeRemoteCallTransactionStep1(function,gasProvider);
    }

    private Function createWithdrawDelegateRewardFunction() {
    	Function function = new Function(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE);
        return function;
    }

    /**
     *  获得提取的明细（当提取账户当前所有的可提取的委托奖励成功时调用）
     *
     * @param transactionReceipt
     * @return
     * @throws TransactionException
     */
    public List<Reward> decodeWithdrawDelegateRewardLog(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if(logs==null||logs.isEmpty()){
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        String logData = logs.get(0).getData();
        if(null == logData || "".equals(logData) ){
            throw new TransactionException("TransactionReceipt logs[0].data is empty");
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        if(statusCode != ErrorCode.SUCCESS){
            throw new TransactionException("TransactionResponse code is 0");
        }

        List<Reward> rewards = new ArrayList<>();
        ((RlpList)((RlpList)RlpDecoder.decode(((RlpString)rlpList.get(1)).getBytes())).getValues().get(0)).getValues()
                .stream()
                .forEach(rl -> {
                    RlpList rlpL = (RlpList)rl;
                    Reward reward = new Reward();
                    reward.setNodeId(((RlpString)rlpL.getValues().get(0)).asString());
                    reward.setStakingNum(((RlpString)rlpL.getValues().get(1)).asPositiveBigInteger());
                    reward.setRewardBigIntegerValue((((RlpString)rlpL.getValues().get(2)).asPositiveBigInteger()));
                    rewards.add(reward);
                });

        return  rewards;
    }

    /**
     * 查询当前账户地址所委托的节点的NodeID和质押Id
     *
     * @param address 查询的地址
     * @param nodeList 节点id列表
     * @return
     */
    public RemoteCall<CallResponse<List<Reward>>> getDelegateReward(String address,List<String> nodeList) {
        List<NodeId> bytesTypeList = nodeList.stream().map(nodeId ->  new NodeId(nodeId)).collect(Collectors.toList());
        CustomStaticArray<NodeId> dynamicArray =  new CustomStaticArray<>(bytesTypeList);
        Function function = new Function(FunctionType.GET_DELEGATE_REWARD_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(address)), dynamicArray));
        return executeRemoteCallListValueReturn(function, Reward.class);
    }
}
