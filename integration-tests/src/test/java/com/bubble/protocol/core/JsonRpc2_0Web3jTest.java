package com.bubble.protocol.core;

import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.DebugWaitSlashingNodeList;
import com.bubble.protocol.core.methods.response.BubbleChainId;
import com.bubble.protocol.http.HttpService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author liushuyu
 * @Date 2021/7/6 15:52
 * @Version
 * @Desc
 */
public class JsonRpc2_0Web3jTest {

    private static final Logger logger = LoggerFactory.getLogger("JsonRpc2_0Web3jTest");

    @Before
    public void init(){
        NetworkParameters.selectBubble();
    }

    /**
     * 底层1.0.0版本
     * @throws IOException
     */
    @Test
    public void testChainId_version1_0_0() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6789"));
        BubbleChainId send = web3j.getChainId().send();
        Assert.assertTrue(send.getError() != null);
    }

    /**
     * 底层1.1.0版本
     * @throws IOException
     */
    @Test
    public void testChainId_version1_1_0() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6780"));
        BubbleChainId send = web3j.getChainId().send();
        Assert.assertTrue(send.getError() == null);
    }

    /**
     * 底层1.0.0版本
     * @throws IOException
     */
    @Test
    public void testGetWaitSlashingNodeList_version1_0_0() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6789"));
        DebugWaitSlashingNodeList nodeList = web3j.getWaitSlashingNodeList().send();
        Assert.assertTrue(nodeList.getResult() != null);
        Assert.assertTrue(nodeList.getError() == null);
    }

    /**
     * 底层1.1.0版本
     * @throws IOException
     */
    @Test
    public void testGetWaitSlashingNodeList_version1_1_0() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.120.150:6780"));
        DebugWaitSlashingNodeList nodeList = web3j.getWaitSlashingNodeList().send();
        Assert.assertTrue(nodeList.getResult() != null);
        Assert.assertTrue(nodeList.getError() == null);
    }
}
