package com.bubble.protocol.core;

import com.alibaba.fastjson.JSONObject;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.DebugWaitSlashingNodeList;
import com.bubble.protocol.http.HttpService;

import java.io.IOException;

public class OtherTest {

    public static void main(String[] args) {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            DebugWaitSlashingNodeList send = web3j.getWaitSlashingNodeList().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
