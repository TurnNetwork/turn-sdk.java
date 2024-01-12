package com.bubble.protocol;

import com.bubble.contracts.dpos.Scenario;
import com.bubble.protocol.core.methods.response.bean.EconomicConfig;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;
import com.bubble.protocol.websocket.WebSocketService;
import org.junit.Test;

import java.math.BigInteger;
import java.net.ConnectException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class WebSocketServiceTest {

    @Test
    public void connect() throws Exception {
		WebSocketService webSocketService = new WebSocketService("ws://192.168.9.139:7789", false);
		webSocketService.connect(
			message -> {
				System.out.println("on message = " + message);
			},

			error -> {
				System.out.println("on error = ");
				error.printStackTrace();
			},

			() -> {
				System.out.println("on close");
			}
		);
		Web3j web3j = Web3j.build(webSocketService);
		System.out.println(web3j.bubbleBlockNumber().send().getBlockNumber());

		TimeUnit.SECONDS.sleep(10);
    }

}
