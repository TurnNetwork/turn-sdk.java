package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;

import java.util.List;

/**
 * User: dongqile
 * Date: 2018/11/12
 * Time: 14:19
 */
public class BubblePendingTransactions extends Response<List<Transaction>> {
    public List<Transaction> getTransactions() {
        return getResult();
    }
}