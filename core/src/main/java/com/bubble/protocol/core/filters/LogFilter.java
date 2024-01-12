package com.bubble.protocol.core.filters;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.methods.request.BubbleFilter;
import com.bubble.protocol.core.methods.response.Log;
import com.bubble.protocol.core.methods.response.BubbleLog;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Log filter handler.
 */
public class LogFilter extends Filter<Log> {

    private final BubbleFilter ethFilter;

    public LogFilter(
            Web3j web3j, Callback<Log> callback,
            BubbleFilter ethFilter) {
        super(web3j, callback);
        this.ethFilter = ethFilter;
    }


    @Override
    com.bubble.protocol.core.methods.response.BubbleFilter sendRequest() throws IOException {
        return web3j.bubbleNewFilter(ethFilter).send();
    }

    @Override
    void process(List<BubbleLog.LogResult> logResults) {
        for (BubbleLog.LogResult logResult : logResults) {
            if (logResult instanceof BubbleLog.LogObject) {
                Log log = ((BubbleLog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }

    @Override
    protected Optional<Request<?, BubbleLog>> getFilterLogs(BigInteger filterId) {
        return Optional.of(web3j.bubbleGetFilterLogs(filterId));
    }
}
