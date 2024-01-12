package com.bubble.protocol.http;

import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.methods.response.BubbleBlockNumber;
import com.bubble.protocol.core.methods.response.BubbleSubscribe;
import com.bubble.protocol.exceptions.ClientConnectionException;
import com.bubble.protocol.websocket.events.NewHeadsNotification;
import okhttp3.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class HttpServiceTest {
    
    private HttpService httpService = new HttpService();
    
    @Test
    public void testAddHeader() {
        String headerName = "customized_header0";
        String headerValue = "customized_value0";
        httpService.addHeader(headerName, headerValue);
        assertTrue(httpService.getHeaders().get(headerName).equals(headerValue));
    }
    
    @Test
    public void testAddHeaders() {
        String headerName1 = "customized_header1";
        String headerValue1 = "customized_value1";
        
        String headerName2 = "customized_header2";
        String headerValue2 = "customized_value2";
        
        HashMap<String, String> headersToAdd = new HashMap<>();
        headersToAdd.put(headerName1, headerValue1);
        headersToAdd.put(headerName2, headerValue2);
        
        httpService.addHeaders(headersToAdd);
        
        assertTrue(httpService.getHeaders().get(headerName1).equals(headerValue1));
        assertTrue(httpService.getHeaders().get(headerName2).equals(headerValue2));
    }

    @Test
    public void httpWebException() throws IOException {
        String content = "400 error";
        Response response = new Response.Builder()
                .code(400)
                .message("")
                .body(ResponseBody.create(null, content))
                .request(new okhttp3.Request.Builder()
                        .url(HttpService.DEFAULT_URL)
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .build();

        OkHttpClient httpClient = Mockito.mock(OkHttpClient.class);
        Mockito.when(httpClient.newCall(Mockito.any()))
                .thenAnswer(invocation -> {
                    Call call = Mockito.mock(Call.class);
                    Mockito.when(call.execute()).thenReturn(response);

                    return call;
                });
        HttpService mockedHttpService = new HttpService(httpClient);

        Request<String, BubbleBlockNumber> request = new Request<>(
                "eth_blockNumber1",
                Collections.emptyList(),
                mockedHttpService,
                BubbleBlockNumber.class);
        try {
            mockedHttpService.send(request, BubbleBlockNumber.class);
        } catch (ClientConnectionException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    "Invalid response received: "
                            + response.code() + "; " + content);
            return;
        }

        Assert.fail("No exception");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void subscriptionNotSupported() {
        Request<Object, BubbleSubscribe> subscribeRequest = new Request<>(
                "eth_subscribe",
                Arrays.asList("newHeads", Collections.emptyMap()),
                httpService,
                BubbleSubscribe.class);

        httpService.subscribe(
                subscribeRequest,
                "eth_unsubscribe",
                NewHeadsNotification.class
        );
    }
    
}
