package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

import java.util.List;

/**
 * eth_accounts.
 */
public class BubbleAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
