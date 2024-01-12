package com.bubble.contracts.dpos;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.abi.solidity.datatypes.generated.Uint32;
import com.bubble.abi.solidity.datatypes.generated.Uint8;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.dto.enums.VoteOption;
import com.bubble.contracts.dpos.dto.resp.GovernParam;
import com.bubble.contracts.dpos.dto.resp.Proposal;
import com.bubble.contracts.dpos.dto.resp.TallyResult;
import com.bubble.contracts.dpos.exception.EstimateGasException;
import com.bubble.contracts.dpos.exception.NoSupportFunctionType;
import com.bubble.crypto.Credentials;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ProposalContract extends BaseContract {

	/**
	 * 加载合约, 默认ReadonlyTransactionManager事务管理
	 *
	 * @param web3j
	 * @return
	 */
    public static ProposalContract load(Web3j web3j) {
        return new ProposalContract(NetworkParameters.getDposContractAddressOfProposal(), web3j);
    }

    /**
     * 加载合约
     *
     * @param web3j
     * @param transactionManager
     * @return
     */
    public static ProposalContract load(Web3j web3j, TransactionManager transactionManager) {
    	return new ProposalContract(NetworkParameters.getDposContractAddressOfProposal(), web3j, transactionManager);
    }

    /**
     * 加载合约, 默认RawTransactionManager事务管理
     *
     * @param web3j
     * @param credentials
     * @return
     */
    public static ProposalContract load(Web3j web3j, Credentials credentials) {
        return new ProposalContract(NetworkParameters.getDposContractAddressOfProposal(), web3j, credentials);
    }

    private ProposalContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private ProposalContract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    private ProposalContract(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(contractAddress, web3j, transactionManager);
    }

    /**
     * 查询提案
     *
     * @param proposalId
     * @return
     */
    public RemoteCall<CallResponse<Proposal>> getProposal(String proposalId) {
        Function function = new Function(FunctionType.GET_PROPOSAL_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalId))));
        return executeRemoteCallObjectValueReturn(function, Proposal.class);
    }

    /**
     * 查询提案结果
     *
     * @param proposalId
     * @return
     */
    public RemoteCall<CallResponse<TallyResult>> getTallyResult(String proposalId) {
        Function function = new Function(FunctionType.GET_TALLY_RESULT_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalId))));
        return executeRemoteCallObjectValueReturn(function, TallyResult.class);
    }

    /**
     * 获取提案列表
     *
     * @return
     */
    public RemoteCall<CallResponse<List<Proposal>>> getProposalList() {
        Function function = new Function(FunctionType.GET_PROPOSAL_LIST_FUNC_TYPE,
                Arrays.asList());
        return executeRemoteCallListValueReturn(function, Proposal.class);
    }

    /**
     * 给提案投票
     *
     * @param programVersion
     * @param voteOption     投票选项
     * @param proposalID     提案ID
     * @param verifier       投票验证人
     * @return
     */
    public RemoteCall<TransactionResponse> vote(ProgramVersion programVersion, VoteOption voteOption, String proposalID, String verifier)  {
        Function function = createVoteFunction(programVersion, proposalID, verifier, voteOption);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 给提案投票
     *
     * @param programVersion
     * @param voteOption     投票选项
     * @param proposalID     提案ID
     * @param verifier       投票验证人
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> vote(ProgramVersion programVersion, VoteOption voteOption, String proposalID, String verifier, GasProvider gasProvider)  {
        Function function = createVoteFunction(programVersion, proposalID, verifier, voteOption);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 给提案投票
     *
     * @param programVersion
     * @param voteOption     投票选项
     * @param proposalID     提案ID
     * @param verifier       投票验证人
     * @return
     */
    public GasProvider getVoteProposalGasProvider(ProgramVersion programVersion, VoteOption voteOption, String proposalID, String verifier) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createVoteFunction(programVersion, proposalID, verifier, voteOption);
        return getDefaultGasProvider(function);
    }

    /**
     * @param programVersion
     * @param voteOption     投票选项
     * @param proposalID     提案ID
     * @param verifier       投票验证人
     * @return
     */
    public RemoteCall<BubbleSendTransaction> voteReturnTransaction(ProgramVersion programVersion, VoteOption voteOption, String proposalID, String verifier) {
        Function function = createVoteFunction(programVersion, proposalID, verifier, voteOption);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * @param programVersion
     * @param voteOption     投票选项
     * @param proposalID     提案ID
     * @param verifier       投票验证人
     * @param gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> voteReturnTransaction(ProgramVersion programVersion, String proposalID, String verifier, VoteOption voteOption, GasProvider gasProvider)  {
        Function function = createVoteFunction(programVersion, proposalID, verifier, voteOption);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createVoteFunction(ProgramVersion programVersion, String proposalID, String verifier, VoteOption voteOption) {
        Function function = new Function(FunctionType.VOTE_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(verifier)),
                        new BytesType(Numeric.hexStringToByteArray(proposalID)), new Uint8(voteOption.getValue()),
                        new Uint32(programVersion.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(programVersion.getProgramVersionSign()))));
        return function;
    }

    /**
     * 版本声明
     *
     * @param verifier 声明的节点，只能是验证人/候选人
     * @return
     */
    public RemoteCall<TransactionResponse> declareVersion(ProgramVersion programVersion, String verifier)  {
        Function function = createDeclareVersionFunction(programVersion, verifier);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 版本声明
     *
     * @param programVersion
     * @param verifier       声明的节点，只能是验证人/候选人
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> declareVersion(ProgramVersion programVersion, String verifier, GasProvider gasProvider) {
        Function function = createDeclareVersionFunction(programVersion, verifier);
        return executeRemoteCallTransaction(function, gasProvider);
    }

    /**
     * 获取版本声明的gasProvider
     *
     * @param programVersion
     * @param verifier
     * @return
     */
    public GasProvider getDeclareVersionGasProvider(ProgramVersion programVersion, String verifier) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createDeclareVersionFunction(programVersion, verifier);
        return getDefaultGasProvider(function);
    }

    /**
     * @param programVersion
     * @param verifier       声明的节点，只能是验证人/候选人
     * @return
     */
    public RemoteCall<BubbleSendTransaction> declareVersionReturnTransaction(ProgramVersion programVersion, String verifier)  {
        Function function = createDeclareVersionFunction(programVersion, verifier);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * @param verifier    声明的节点，只能是验证人/候选人
     * @param gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> declareVersionReturnTransaction(ProgramVersion programVersion, String verifier, GasProvider gasProvider) {
        Function function = createDeclareVersionFunction(programVersion, verifier);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createDeclareVersionFunction(ProgramVersion programVersion, String verifier)  {
        Function function = new Function(FunctionType.DECLARE_VERSION_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(verifier)),
                        new Uint32(programVersion.getProgramVersion()),
                        new BytesType(Numeric.hexStringToByteArray(programVersion.getProgramVersionSign()))));
        return function;
    }

    /**
     * 提交提案
     *
     * @param proposal 包括文本提案和版本提案
     * @return
     */
    public RemoteCall<TransactionResponse> submitProposal(Proposal proposal) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransaction(function);
    }

    /**
     * 提交提案
     *
     * @param proposal    包括文本提案和版本提案
     * @param gasProvider
     * @return
     */
    public RemoteCall<TransactionResponse> submitProposal(Proposal proposal, GasProvider gasProvider) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransaction(function,gasProvider);
    }

    /**
     * 获取提交提案gasProvider
     *
     * @param proposal
     * @return
     */
    public GasProvider getSubmitProposalGasProvider(Proposal proposal) throws IOException, EstimateGasException, NoSupportFunctionType {
        Function function = createSubmitProposalFunction(proposal);
        return getDefaultGasProvider(function);
    }

    /**
     * 提交提案
     *
     * @param proposal
     * @return
     */
    public RemoteCall<BubbleSendTransaction> submitProposalReturnTransaction(Proposal proposal) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransactionStep1(function);
    }

    /**
     * 提交提案
     *
     * @param proposal
     * @param gasProvider
     * @return
     */
    public RemoteCall<BubbleSendTransaction> submitProposalReturnTransaction(Proposal proposal, GasProvider gasProvider) {
        Function function = createSubmitProposalFunction(proposal);
        return executeRemoteCallTransactionStep1(function, gasProvider);
    }

    private Function createSubmitProposalFunction(Proposal proposal) {
        if (proposal == null) {
            throw new NullPointerException("proposal must not be null");
        }
        Function function = new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters());
        return function;
    }

    /**
     * 查询已生效的版本
     *
     * @return
     */
    public RemoteCall<CallResponse<BigInteger>> getActiveVersion() {
        Function function = new Function(FunctionType.GET_ACTIVE_VERSION);
        return executeRemoteCallObjectValueReturn(function, BigInteger.class);
    }

    /**
     * 查询当前块高的治理参数值
     *
     * @param module 参数模块
     * @param name   参数名称
     * @return
     */
    public RemoteCall<CallResponse<String>> getGovernParamValue(String module, String name) {
        Function function = new Function(FunctionType.GET_GOVERN_PARAM_VALUE, Arrays.asList(new Utf8String(module), new Utf8String(name)));
        return executeRemoteCallObjectValueReturn(function, String.class);
    }

    /**
     * 查询提案的累积可投票人数
     *
     * @param proposalId 提案ID
     * @param blockHash  块hash
     * @return
     */
    public RemoteCall<CallResponse<List<BigInteger>>> getAccuVerifiersCount(String proposalId, String blockHash) {
        Function function = new Function(FunctionType.GET_ACCUVERIFIERS_COUNT, Arrays.asList(new BytesType(Numeric.hexStringToByteArray(proposalId)), new BytesType(Numeric.hexStringToByteArray(blockHash))));
        return executeRemoteCallListValueReturn(function, BigInteger.class);
    }

    /**
     * 查询可治理参数列表
     *
     * @return
     */
    public RemoteCall<CallResponse<List<GovernParam>>> getParamList(String module) {
        Function function = new Function(FunctionType.GET_PARAM_LIST, Arrays.asList(new Utf8String(module)));
        return executeRemoteCallListValueReturn(function, GovernParam.class);
    }
}
