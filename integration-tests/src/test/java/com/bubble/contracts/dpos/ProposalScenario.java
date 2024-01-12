package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.BaseResponse;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.ProposalType;
import com.bubble.contracts.dpos.dto.resp.Proposal;
import com.bubble.contracts.dpos.dto.resp.TallyResult;
import com.bubble.contracts.dpos.utils.ProposalUtils;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;
import com.bubble.utils.Convert.Unit;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class ProposalScenario extends Scenario {

    private BigDecimal transferValue = new BigDecimal("1000");

    /**
     * 正常的场景:
     * 初始化账户余额
     * 提交文本提案(2000)
     * 给提案投票(2003)
     * 查询提案(2100)
     * 查询提案结果(2101)
     * 查询提案列表(2102)
     * <p>
     * 提交升级提案(2001)
     * 给提案投票(2003)
     * 版本声明(2004)
     * 查询生效版本(2103)
     * <p>
     * 提交取消提(2005)
     * 给提案投票(2003)
     */
    @Test
    public void executeScenario() throws Exception {
        //初始化账户余额
        transfer();

        BigInteger stakingBalance = web3j.bubbleGetBalance(proposalCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger delegateBalance = web3j.bubbleGetBalance(voteCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();

        assertTrue(new BigDecimal(stakingBalance).compareTo(Convert.fromVon(transferValue, Unit.VON)) >= 0);
        assertTrue(new BigDecimal(delegateBalance).compareTo(Convert.fromVon(transferValue, Unit.VON)) >= 0);

        //提交文本提案(2000)
        TransactionResponse createTextProposalResponse = createTextProposal();
        assertTrue(createTextProposalResponse.toString(), createTextProposalResponse.isStatusOk());
        String textProposalHash = createTextProposalResponse.getTransactionReceipt().getTransactionHash();

        //给提案投票(2003)
        for (VoteInfo voteInfo : voteInfos) {
        	TransactionResponse voteBaseResponse = vote(voteInfo, textProposalHash);
            assertTrue(voteBaseResponse.toString(), voteBaseResponse.isStatusOk());
        }

        //查询提案(2100)
        CallResponse<Proposal> getProposalResponse = getProposal(textProposalHash);
        assertTrue(getProposalResponse.toString(), getProposalResponse.isStatusOk());

        //判断等待投票结果
        waitResult(getProposalResponse);

        //查询提案结果(2101)
        CallResponse<TallyResult> getTallyResultResponse = getTallyResult(textProposalHash);
        assertTrue(getTallyResultResponse.toString(), getTallyResultResponse.isStatusOk());

        //提交参数提案
        TransactionResponse createParamProposalResponse = createParamProposal();
        assertTrue(createParamProposalResponse.toString(), createParamProposalResponse.isStatusOk());
        String paramProposalHash = createParamProposalResponse.getTransactionReceipt().getTransactionHash();

        //给提案投票(2003)
        for (VoteInfo voteInfo : voteInfos) {
            TransactionResponse voteBaseResponse = vote(voteInfo, paramProposalHash);
            assertTrue(voteBaseResponse.toString(), voteBaseResponse.isStatusOk());
        }

        //查询提案(2100)
        getProposalResponse = getProposal(paramProposalHash);
        assertTrue(getProposalResponse.toString(), getProposalResponse.isStatusOk());

        //判断等待投票结果
        waitResult(getProposalResponse);

        //查询提案结果(2101)
        getTallyResultResponse = getTallyResult(paramProposalHash);
        assertTrue(getTallyResultResponse.toString(), getTallyResultResponse.isStatusOk());

        //提交升级提案(2001)
        TransactionResponse createVersionProposalResponse = createVersionProposal();
        assertTrue(createVersionProposalResponse.toString(), createVersionProposalResponse.isStatusOk());
        String versionProposalHash = createVersionProposalResponse.getTransactionReceipt().getTransactionHash();

        //查询提案列表(2102)
        CallResponse<List<Proposal>> getProposalListResponse = getProposalList();
        assertTrue(getProposalListResponse.toString(), getProposalListResponse.isStatusOk());

        List<Proposal> proposalList = getProposalListResponse.getData();
        assertTrue(proposalList != null && !proposalList.isEmpty());

        Proposal proposal = proposalList.get(0);

        //版本声明(2004)
        TransactionResponse declareVersionResponse = declareVersion();
        assertTrue(declareVersionResponse.toString(), declareVersionResponse.isStatusOk());

        //查询提案的累积可投票人数
        CallResponse<List<BigInteger>>  getAccuVerifiersCountResponse = getAccuVerifiersCount(proposal.getProposalId(), versionProposalHash);
        assertTrue(getAccuVerifiersCountResponse.toString(), getAccuVerifiersCountResponse.isStatusOk());

        Proposal paramProposal = getParamProposal(proposalList);
        assertTrue(paramProposal != null);

        //提交取消提(2005)
        TransactionResponse createCancalProposalResponse = createCancalProposal(versionProposalHash);
        assertTrue(createCancalProposalResponse.toString(), createCancalProposalResponse.isStatusOk());
        String cancalProposalHash = createCancalProposalResponse.getTransactionReceipt().getTransactionHash();

        //给提案投票(2003)
        for (VoteInfo voteInfo : voteInfos) {
            BaseResponse voteBaseResponse = vote(voteInfo, cancalProposalHash);
            assertTrue(voteBaseResponse.toString(), voteBaseResponse.isStatusOk());
        }
    }

    public Proposal getParamProposal(List<Proposal> proposalList) {

        for (Proposal proposal : proposalList) {
            if (proposal.getProposalType() == ProposalType.PARAM_PROPOSAL) {
                return proposal;
            }
            continue;
        }

        return null;
    }


    public TransactionResponse createCancalProposal(String versionProposalId) throws Exception {
        Proposal proposal = Proposal.createSubmitCancelProposalParam(proposalNodeId, String.valueOf(new Date().getTime()), BigInteger.valueOf(2), versionProposalId);
        BubbleSendTransaction bubbleSendTransactionCan = proposalContract.submitProposalReturnTransaction(proposal).send();
        TransactionResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransactionCan).send();
        return baseResponse;
    }

    public TransactionResponse declareVersion() throws Exception {
        BubbleSendTransaction bubbleSendTransaction = proposalContract.declareVersionReturnTransaction(proposalWeb3j.getProgramVersion().send().getAdminProgramVersion(), proposalNodeId).send();
        TransactionResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

    public TransactionResponse createVersionProposal() throws Exception {
        BigInteger newVersion = ProposalUtils.versionStrToInteger("100.0.0");
        BigInteger endVotingRounds = BigInteger.valueOf(4);
        Proposal proposal = Proposal.createSubmitVersionProposalParam(proposalNodeId, String.valueOf(new Date().getTime()), newVersion, endVotingRounds);
        BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
        TransactionResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

    public TransactionResponse createParamProposal() throws Exception {
        String module = "staking";
        String name = "operatingThreshold";
        String value = "20000000000000000000";
        Proposal proposal = Proposal.createSubmitParamProposalParam(proposalNodeId, String.valueOf(new Date().getTime()), module, name, value);
        BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
        TransactionResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

    public CallResponse<List<Proposal>> getProposalList() throws Exception {
        CallResponse<List<Proposal>> baseResponse = proposalContract.getProposalList().send();
        return baseResponse;
    }

    public CallResponse<TallyResult> getTallyResult(String proposalID) throws Exception {
    	CallResponse<TallyResult> baseResponse = proposalContract.getTallyResult(proposalID).send();
        return baseResponse;
    }

    public CallResponse<Proposal> getProposal(String proposalID) throws Exception {
    	CallResponse<Proposal> baseResponse = proposalContract.getProposal(proposalID).send();
        return baseResponse;
    }

    public TransactionResponse vote(VoteInfo voteInfo, String proposalID) throws Exception {
        BubbleSendTransaction bubbleSendTransaction = voteInfo.getVoteContract().voteReturnTransaction(voteInfo.getWeb3j().getProgramVersion().send().getAdminProgramVersion(), voteInfo.getVoteOption(), proposalID, voteInfo.getNodeId()).send();
        TransactionResponse baseResponse = voteInfo.getVoteContract().getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

    public TransactionResponse createTextProposal() throws Exception {
        Proposal proposal = Proposal.createSubmitTextProposalParam(proposalNodeId, String.valueOf(new Date().getTime()));
        BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
        TransactionResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
        return baseResponse;
    }

    public void transfer() throws Exception {
        Transfer.sendFunds(web3j, superCredentials, proposalCredentials.getAddress(), transferValue, Unit.KPVON).send();
        Transfer.sendFunds(web3j, superCredentials, voteCredentials.getAddress(), transferValue, Unit.KPVON).send();
    }

    public CallResponse<List<BigInteger>> getAccuVerifiersCount(String proposalId, String blockHash) throws Exception {
        return proposalContract.getAccuVerifiersCount(proposalId, blockHash).send();
    }

    public void waitResult(CallResponse<Proposal> getProposalResponse) throws Exception {
        int time = 0;
        while (true) {
            BigInteger endVotingBlock = getProposalResponse.getData().getEndVotingBlock();
            BigInteger curBlockNumber = web3j.bubbleBlockNumber().send().getBlockNumber();
            if (curBlockNumber.compareTo(endVotingBlock) > 0) {
                break;
            }
            TimeUnit.SECONDS.sleep(5);
            time++;
            if (time > 100) {
                throw new RuntimeException("等待超时");
            }
        }
    }
}
