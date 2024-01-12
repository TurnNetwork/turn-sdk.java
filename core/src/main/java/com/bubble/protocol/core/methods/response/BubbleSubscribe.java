package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

public class BubbleSubscribe extends Response<String> {
    public String getSubscriptionId() {
        return getResult();
    }
}
