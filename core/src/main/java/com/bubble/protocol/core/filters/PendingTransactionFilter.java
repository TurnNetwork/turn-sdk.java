package com.bubble.protocol.core.filters;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.methods.response.BubbleFilter;
import com.bubble.protocol.core.methods.response.BubbleLog;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Handler for working with transaction filter requests.
 */
public class PendingTransactionFilter extends Filter<String> {

    public PendingTransactionFilter(Web3j web3j, Callback<String> callback) {
        super(web3j, callback);
    }

    @Override
    BubbleFilter sendRequest() throws IOException {
        return web3j.bubbleNewPendingTransactionFilter().send();
    }

    @Override
    void process(List<BubbleLog.LogResult> logResults) {
        for (BubbleLog.LogResult logResult : logResults) {
            if (logResult instanceof BubbleLog.Hash) {
                String transactionHash = ((BubbleLog.Hash) logResult).get();
                callback.onEvent(transactionHash);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }

    /**
     * Since the pending transaction filter does not support historic filters,
     * the filterId is ignored and an empty optional is returned
     * @param filterId
     * Id of the filter for which the historic log should be retrieved
     * @return
     * Optional.empty()
     */
    @Override
    protected Optional<Request<?, BubbleLog>> getFilterLogs(BigInteger filterId) {
        return Optional.empty();
    }
}

