package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.BaseResponse;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.enums.VoteOption;
import com.bubble.contracts.dpos.dto.resp.Proposal;
import com.bubble.contracts.dpos.dto.resp.TallyResult;
import com.bubble.contracts.dpos.utils.ProposalUtils;
import com.bubble.crypto.Credentials;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert.Unit;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * 治理相关接口，包括，
 * 提交文本提案
 * 提交升级提案
 * 提交参数提案
 * 给提案投票
 * 版本声明
 * 查询提案
 * 查询提案结果
 * 查询提案列表
 * 查询生效版本
 * 查询节点代码版本
 * 查询可治理参数列表
 */
public class ProposalContractTest {

    private String nodeId = "405e2aae5566492aa5da37eb81291e502cee02eac7b03e2734d1a75d3eff78cb7cf849b68f072128e1d5324c150dce4dce06d675ed97f97daca5ce651d9c7303";
    String blsPubKey = "0b7418f7f774f538b610fa5a70d797fd4b205b7b6e91185119c603b9c4c5236c721af355ca109bedfd06ebadaccfe20ea1bf100b40d4e2f6ad5f5152515d7375d043ff988297d4d6826f1931c74e535fbd4b780d49f45df445caadba585fd810";
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));

    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials voteCredentials;

	private ProposalContract proposalContract;

    @Before
    public void init() throws Exception {

    	superCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	System.out.println("superCredentials balance="+ web3j.bubbleGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	stakingCredentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

    	voteCredentials = superCredentials;

        proposalContract = ProposalContract.load(web3j, stakingCredentials);
    }

    @Test
    public void transfer() throws Exception {
    	Transfer.sendFunds(web3j, superCredentials, stakingCredentials.getAddress(), new BigDecimal("10000"), Unit.KPVON).send();
    	System.out.println("stakingCredentials balance="+ web3j.bubbleGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }


    /**
     * 提交文本提案
     */
    @Test
    public void submitTextProposal() {
        try {
        	Proposal proposal = Proposal.createSubmitTextProposalParam(nodeId, String.valueOf(new Date().getTime()));
            BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());


            voteForProposal(bubbleSendTransaction.getTransactionHash());

            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 提交参数提案
     */
    @Test
    public void submitParamProposal() {
        try {

            String module = "staking";
            String name = "operatingThreshold";
            String value = "50000000000000000000";

        	Proposal proposal = Proposal.createSubmitParamProposalParam(nodeId, String.valueOf(new Date().getTime()), module, name, value);
            BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());

            voteForProposal(bubbleSendTransaction.getTransactionHash());

            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交升级提案
     */
    @Test
    public void submitVersionProposal() {
        try {
        	BigInteger newVersion =  ProposalUtils.versionStrToInteger("1.0.0");
        	BigInteger endVotingRounds =  BigInteger.valueOf(4);
            Proposal proposal = Proposal.createSubmitVersionProposalParam(nodeId, String.valueOf(new Date().getTime()), newVersion, endVotingRounds);
            BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());

            voteForProposal(bubbleSendTransaction.getTransactionHash());

            System.out.println("提案id："+bubbleSendTransaction.getTransactionHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交升级取消提案
     */
    @Test
    public void submitCancalVersionProposal() {
        try {
        	BigInteger newVersion =  ProposalUtils.versionStrToInteger("1.0.0");
        	BigInteger endVotingRounds =  BigInteger.valueOf(4);
            Proposal proposal = Proposal.createSubmitVersionProposalParam(nodeId,  String.valueOf(new Date().getTime()), newVersion, endVotingRounds);
            BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println("发起提案结果："+baseResponse.toString());

            voteForProposal(bubbleSendTransaction.getTransactionHash());


            //取消提案
            System.out.println("取消提案开始");


            BubbleSendTransaction bubbleSendTransactionCan = proposalContract.submitProposalReturnTransaction(Proposal.createSubmitCancelProposalParam(nodeId, "35", BigInteger.valueOf(2),bubbleSendTransaction.getTransactionHash())).send();
            BaseResponse baseResponseCan = proposalContract.getTransactionResponse(bubbleSendTransactionCan).send();
            System.out.println(baseResponseCan.toString());

            voteForProposal(bubbleSendTransactionCan.getTransactionHash());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交参数取消提案
     */
    @Test
    public void submitCancalParamProposal() {
        try {

            String module = "staking";
            String name = "operatingThreshold";
            String value = "10000000000000000000";

        	Proposal proposal = Proposal.createSubmitParamProposalParam(nodeId, String.valueOf(new Date().getTime()), module, name, value);
            BubbleSendTransaction bubbleSendTransaction = proposalContract.submitProposalReturnTransaction(proposal).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println("发起提案结果："+baseResponse.toString());

            voteForProposal(bubbleSendTransaction.getTransactionHash());


            //取消提案
            System.out.println("取消提案开始");


            BubbleSendTransaction bubbleSendTransactionCan = proposalContract.submitProposalReturnTransaction(Proposal.createSubmitCancelProposalParam(nodeId, String.valueOf(new Date().getTime()), BigInteger.valueOf(2),bubbleSendTransaction.getTransactionHash())).send();
            BaseResponse baseResponseCan = proposalContract.getTransactionResponse(bubbleSendTransactionCan).send();
            System.out.println(baseResponseCan.toString());

            voteForProposal(bubbleSendTransactionCan.getTransactionHash());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void voteForProposal(String proposalID) {
        vote(proposalID,
        		nodeId,
        		"http://192.168.31.117:7789", VoteOption.YEAS);
    }

    public void vote(String proposalID, String nodeId, String nodeHost, VoteOption voteOption) {
        try {
        	Web3j web3j =  Web3j.build(new HttpService(nodeHost));
        	ProposalContract voteContract = ProposalContract.load(web3j, voteCredentials);
            BaseResponse baseResponse = voteContract.vote(web3j.getProgramVersion().send().getAdminProgramVersion(), voteOption,proposalID, nodeId).send();
            System.out.println("投票结果："+baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryResult() {
        try {
        	queryResult("0x15b57a14d00695957d7dbb28c84585ea129e7840cabceb86d118e8df7f5236e2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void queryResult(String proposalID) {
        try {
            CallResponse<Proposal> baseResponse = proposalContract.getProposal(proposalID).send();
            System.out.println("提案信息："+baseResponse);
            CallResponse<TallyResult> result = proposalContract.getTallyResult(proposalID).send();
		    System.out.println("提案结果："+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询节点的链生效版本
     */
    @Test
    public void getActiveVersion() {
        try {
            BaseResponse baseResponse = proposalContract.getActiveVersion().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 版本声明
     * activeNode 声明的节点，只能是验证人/候选人
     */
    @Test
    public void declareVersion() {
        try {

            BubbleSendTransaction bubbleSendTransaction = proposalContract.declareVersionReturnTransaction(web3j.getProgramVersion().send().getAdminProgramVersion(), nodeId).send();
            BaseResponse baseResponse = proposalContract.getTransactionResponse(bubbleSendTransaction).send();


            System.out.println(baseResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询提案列表
     */
    @Test
    public void listProposal() {
        try {
            CallResponse<List<Proposal>> baseResponse = proposalContract.getProposalList().send();
            List<Proposal> proposalList = baseResponse.getData();
            for (Proposal proposal : proposalList) {
            	CallResponse<TallyResult> result = proposalContract.getTallyResult(proposal.getProposalId()).send();
				System.out.println(proposal);
			    System.out.println(result);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getGovernParamValue() throws Exception {

        String module = "staking";
        String name = "operatingThreshold";
        System.out.println(proposalContract.getGovernParamValue(module, name).send());
    }

    @Test
    public void getParamList() throws Exception {
    	String module = "staking";
        System.out.println(proposalContract.getParamList(module).send());
    }

}
