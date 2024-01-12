package com.bubble.contracts.dpos;

import com.bubble.contracts.dpos.dto.BaseResponse;
import com.bubble.contracts.dpos.dto.common.DuplicateSignType;
import com.bubble.crypto.Credentials;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.BubbleSendTransaction;
import com.bubble.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

public class SlashContractTest {

    private String nodeId = "405e2aae5566492aa5da37eb81291e502cee02eac7b03e2734d1a75d3eff78cb7cf849b68f072128e1d5324c150dce4dce06d675ed97f97daca5ce651d9c7303";
    long chainId = 103;
    private Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));

    private String evidence = "{\"prepareA\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0x47c0b9cc1e544e866ed3afb1a2fec5f8c0c6d97a04680f56f26b238b362482ca\",\"blockNumber\":583848,\"blockIndex\":0,\"blockData\":\"0x005e8ae4a78cd34d2c9fb08abda0e39d781e4abc58ea7f9b03c56f6a8e804027\",\"validateNode\":{\"index\":0,\"address\":\"0x0550184a50db8162c0cfe9296f06b2b1db019331\",\"nodeId\":\"77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050\",\"blsPubKey\":\"5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811\"},\"signature\":\"0x974d787c28b7fb2ec67decdb750e4e29ace69e07ab3d1864c4fba9b7eb780868fb36966183ac6f156b99b0f1d8034d8500000000000000000000000000000000\"},\"prepareB\":{\"epoch\":0,\"viewNumber\":0,\"blockHash\":\"0x4bf9291e34fb7ae3f93eb4bb77a4b41251dce247e4a9b8e120dbf69a310f87bb\",\"blockNumber\":583848,\"blockIndex\":0,\"blockData\":\"0x4b6fd7afae0fd5ab3b2457720297165539623562eff16827981904357720b995\",\"validateNode\":{\"index\":0,\"address\":\"0x0550184a50db8162c0cfe9296f06b2b1db019331\",\"nodeId\":\"77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050\",\"blsPubKey\":\"5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811\"},\"signature\":\"0x615967a22bb06245a76c94ce8914c95bdc9eda8fbfaa97b85d8472c7c7bb10bc6aff325798965e583130690ac7ef568c00000000000000000000000000000000\"}}";
    private SlashContract slashContract;

    private Credentials credentials;

    @Before
    public void init() {
        credentials = Credentials.create("daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        slashContract = SlashContract.load(web3j, credentials);
    }

    /**
     * 举报双签
     * data 证据的json值，格式为RPC接口Evidences的返回值
     */
    @Test
    public void reportDuplicateSign() {
        try {
            BubbleSendTransaction bubbleSendTransaction = slashContract.reportDoubleSignReturnTransaction(DuplicateSignType.PREPARE_BLOCK, evidence).send();
            BaseResponse baseResponse = slashContract.getTransactionResponse(bubbleSendTransaction).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询节点是否已被举报过多签
     * typ  代表双签类型，1：prepare，2：viewChange，3：TimestampViewChange
     * addr 举报的节点地址
     * blockNumber 多签的块高
     */
    @Test
    public void checkDuplicateSign() {
        try {
            BaseResponse baseResponse = slashContract.checkDoubleSign(DuplicateSignType.PREPARE_BLOCK, nodeId, BigInteger.valueOf(583848L)).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
