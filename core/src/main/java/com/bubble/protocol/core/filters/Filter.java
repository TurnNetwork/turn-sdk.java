package com.bubble.protocol.core.filters;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.Request;
import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.RpcErrors;
import com.bubble.protocol.core.methods.response.BubbleFilter;
import com.bubble.protocol.core.methods.response.BubbleLog;
import com.bubble.protocol.core.methods.response.BubbleUninstallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Class for creating managed filter requests with callbacks.
 */
public abstract class Filter<T> {

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    final Web3j web3j;
    final Callback<T> callback;

    private volatile BigInteger filterId;

    private ScheduledFuture<?> schedule;
    
    private ScheduledExecutorService scheduledExecutorService;

    private long blockTime;

    public Filter(Web3j web3j, Callback<T> callback) {
        this.web3j = web3j;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            BubbleFilter ethFilter = sendRequest();
            if (ethFilter.hasError()) {
                throwException(ethFilter.getError());
            }

            filterId = ethFilter.getFilterId();
            this.scheduledExecutorService = scheduledExecutorService;
            this.blockTime = blockTime;
            // this runs in the caller thread as if any exceptions are encountered, we shouldn't
            // proceed with creating the scheduled task below
            getInitialFilterLogs();

            /*
            We want the filter to be resilient against client issues. On numerous occasions
            users have reported socket timeout exceptions when connected over HTTP to Geth and
            Parity clients. For examples, refer to
            https://github.com/web3j/web3j/issues/144 and
            https://github.com/ethereum/go-ethereum/issues/15243.

            Hence we consume errors and log them as errors, allowing our polling for changes to
            resume. The downside of this approach is that users will not be notified of
            downstream connection issues. But given the intermittent nature of the connection
            issues, this seems like a reasonable compromise.

            The alternative approach would be to have another thread that blocks waiting on
            schedule.get(), catching any Exceptions thrown, and passing them back up to the
            caller. However, the user would then be required to recreate subscriptions manually
            which isn't ideal given the aforementioned issues.
            */
            schedule = scheduledExecutorService.scheduleAtFixedRate(
                    () -> {
                        try {
                            this.pollFilter(ethFilter);
                        } catch (Throwable e) {
                            // All exceptions must be caught, otherwise our job terminates without
                            // any notification
                            log.error("Error sending request", e);
                        }
                    },
                    0, blockTime, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throwException(e);
        }
    }

    private void getInitialFilterLogs() {
        try {
            Optional<Request<?, BubbleLog>> maybeRequest = this.getFilterLogs(this.filterId);
            BubbleLog ethLog = null;
            if (maybeRequest.isPresent()) {
                ethLog = maybeRequest.get().send();
            } else {
                ethLog = new BubbleLog();
                ethLog.setResult(Collections.emptyList());
            }
            process(ethLog.getLogs());

        } catch (IOException e) {
            throwException(e);
        }
    }

    private void pollFilter(BubbleFilter ethFilter) {
        BubbleLog ethLog = null;
        try {
            ethLog = web3j.bubbleGetFilterChanges(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }
        if (ethLog.hasError()) {
            Response.Error error = ethLog.getError();
            switch (error.getCode()) {
                case RpcErrors.FILTER_NOT_FOUND: reinstallFilter();
                    break;
                default: throwException(error);
                    break;
            }
        } else {
            process(ethLog.getLogs());
        }
    }

    abstract BubbleFilter sendRequest() throws IOException;

    abstract void process(List<BubbleLog.LogResult> logResults);
    
    private void reinstallFilter() {
        log.warn("The filter has not been found. Filter id: " + filterId);
        schedule.cancel(true);
        this.run(scheduledExecutorService, blockTime);
    }

    public void cancel() {
        schedule.cancel(false);

        try {
            BubbleUninstallFilter ethUninstallFilter = web3j.bubbleUninstallFilter(filterId).send();
            if (ethUninstallFilter.hasError()) {
                throwException(ethUninstallFilter.getError());
            }

            if (!ethUninstallFilter.isUninstalled()) {
                throw new FilterException("Filter with id '" + filterId + "' failed to uninstall");
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    /**
     * Retrieves historic filters for the filter with the given id.
     * Getting historic logs is not supported by all filters.
     * If not the method should return an empty EthLog object
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return Historic logs, or an empty optional if the filter cannot retrieve historic logs
     */
    protected abstract Optional<Request<?, BubbleLog>> getFilterLogs(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: "
                + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}
