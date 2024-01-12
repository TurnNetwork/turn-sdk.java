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
 * Handler for working with block filter requests.
 */
public class BlockFilter extends Filter<String> {

    public BlockFilter(Web3j web3j, Callback<String> callback) {
        super(web3j, callback);
    }

    @Override
    BubbleFilter sendRequest() throws IOException {
        return web3j.bubbleNewBlockFilter().send();
    }

    @Override
    void process(List<BubbleLog.LogResult> logResults) {
        for (BubbleLog.LogResult logResult : logResults) {
            if (logResult instanceof BubbleLog.Hash) {
                String blockHash = ((BubbleLog.Hash) logResult).get();
                callback.onEvent(blockHash);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }

    /**
     * Since the block filter does not support historic filters, the filterId is ignored
     * and an empty optional is returned.
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

