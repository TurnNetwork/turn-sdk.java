package com.bubble.protocol.core;

import com.bubble.protocol.Web3j;
import com.bubble.protocol.Web3jService;
import com.bubble.protocol.admin.methods.response.BooleanResponse;
import com.bubble.protocol.admin.methods.response.TxPoolStatus;
import com.bubble.protocol.admin.methods.response.admin.AdminDataDir;
import com.bubble.protocol.core.methods.response.DebugWaitSlashingNodeList;
import com.bubble.protocol.core.methods.response.BubbleSignTransaction;
import com.bubble.protocol.core.methods.request.ShhFilter;
import com.bubble.protocol.core.methods.response.*;
import com.bubble.protocol.rx.JsonRpc2_0Rx;
import com.bubble.protocol.websocket.events.LogNotification;
import com.bubble.protocol.websocket.events.NewHeadsNotification;
import com.bubble.utils.Async;
import com.bubble.utils.Numeric;
import com.bubble.utils.Strings;
import rx.Observable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Web3j implements Web3j {

    public static final int DEFAULT_BLOCK_TIME = 2 * 1000;

    protected final Web3jService web3jService;
    private final JsonRpc2_0Rx web3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Web3j(Web3jService web3jService) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Web3j(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                web3jService,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                web3jService,
                Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                web3jService,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                web3jService,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                web3jService,
                NetPeerCount.class);
    }

    @Override
    public Request<?, AdminNodeInfo> adminNodeInfo() {
        return new Request<>(
                "admin_nodeInfo", Collections.emptyList(), web3jService, AdminNodeInfo.class);
    }

    @Override
    public Request<?, AdminPeers> adminPeers() {
        return new Request<>(
                "admin_peers", Collections.emptyList(), web3jService, AdminPeers.class);
    }

    @Override
    public Request<?, BooleanResponse> adminAddPeer(String url) {
        return new Request<>(
                "admin_addPeer", Arrays.asList(url), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminRemovePeer(String url) {
        return new Request<>(
                "admin_removePeer", Arrays.asList(url), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, AdminDataDir> adminDataDir() {
        return new Request<>(
                "admin_datadir", Collections.emptyList(), web3jService, AdminDataDir.class);
    }

    @Override
    public Request<?, BooleanResponse> adminStartRPC(String host,int port,String cors,String apis) {
        if(host == null){
            host = "localhost";
        }
        if(cors == null){
            cors = "";
        }
        if(Strings.isBlank(apis)){
            apis = "bubble,net,web3,debug,admin";
        }
        return new Request<>(
                "admin_startRPC", Arrays.asList(host,port,cors,apis), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminStartWS(String host,int port,String cors,String apis) {
        if(host == null){
            host = "localhost";
        }
        if(cors == null){
            cors = "";
        }
        if(Strings.isBlank(apis)){
            apis = "bubble,net,web3,debug,admin";
        }
        return new Request<>(
                "admin_startWS", Arrays.asList(host,port,cors,apis), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminStopRPC() {
        return new Request<>(
                "admin_stopRPC", Collections.<String>emptyList(), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminStopWS() {
        return new Request<>(
                "admin_stopWS", Collections.<String>emptyList(), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminExportChain(String file) {
        return new Request<>(
                "admin_exportChain", Arrays.asList(file), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> adminImportChain(String file) {
        return new Request<>(
                "admin_importChain", Arrays.asList(file), web3jService, BooleanResponse.class);
    }

    @Override
    public Request<?, BubbleProtocolVersion> bubbleProtocolVersion() {
        return new Request<>(
                "bub_protocolVersion",
                Collections.<String>emptyList(),
                web3jService,
                BubbleProtocolVersion.class);
    }

    @Override
    public Request<?, BubbleSyncing> bubbleSyncing() {
        return new Request<>(
                "bub_syncing",
                Collections.<String>emptyList(),
                web3jService,
                BubbleSyncing.class);
    }

    @Override
    public Request<?, BubbleGasPrice> bubbleGasPrice() {
        return new Request<>(
                "bub_gasPrice",
                Collections.<String>emptyList(),
                web3jService,
                BubbleGasPrice.class);
    }

    @Override
    public Request<?, BubbleAccounts> bubbleAccounts() {
        return new Request<>(
                "bub_accounts",
                Collections.<String>emptyList(),
                web3jService,
                BubbleAccounts.class);
    }

    @Override
    public Request<?, BubbleBlockNumber> bubbleBlockNumber() {
        return new Request<>(
                "bub_blockNumber",
                Collections.<String>emptyList(),
                web3jService,
                BubbleBlockNumber.class);
    }

    @Override
    public Request<?, BubbleGetBalance> bubbleGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jService,
                BubbleGetBalance.class);
    }

    @Override
    public Request<?, BubbleGetStorageAt> bubbleGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_getStorageAt",
                Arrays.asList(
                        address,
                        Numeric.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                web3jService,
                BubbleGetStorageAt.class);
    }

    @Override
    public Request<?, BubbleGetTransactionCount> bubbleGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jService,
                BubbleGetTransactionCount.class);
    }

    @Override
    public Request<?, BubbleGetBlockTransactionCountByHash> bubbleGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "bub_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3jService,
                BubbleGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, BubbleGetBlockTransactionCountByNumber> bubbleGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                web3jService,
                BubbleGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, BubbleGetCode> bubbleGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jService,
                BubbleGetCode.class);
    }

    @Override
    public Request<?, BubbleSign> bubbleSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "bub_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                web3jService,
                BubbleSign.class);
    }

    @Override
    public Request<?, BubbleSendTransaction>
    bubbleSendTransaction(
            com.bubble.protocol.core.methods.request.Transaction transaction) {
        return new Request<>(
                "bub_sendTransaction",
                Arrays.asList(transaction),
                web3jService,
                BubbleSendTransaction.class);
    }

    @Override
    public Request<?, BubbleSendTransaction>
    bubbleSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "bub_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                BubbleSendTransaction.class);
    }

    @Override
    public Request<?, BubbleCall> bubbleCall(
            com.bubble.protocol.core.methods.request.Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "bub_call",
                Arrays.asList(transaction, defaultBlockParameter),
                web3jService,
                BubbleCall.class);
    }

    @Override
    public Request<?, BubbleEstimateGas> bubbleEstimateGas(com.bubble.protocol.core.methods.request.Transaction transaction) {
        return new Request<>(
                "bub_estimateGas",
                Arrays.asList(transaction),
                web3jService,
                BubbleEstimateGas.class);
    }

    @Override
    public Request<?, BubbleBlock> bubbleGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "bub_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        returnFullTransactionObjects),
                web3jService,
                BubbleBlock.class);
    }

    @Override
    public Request<?, BubbleBlock> bubbleGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "bub_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        returnFullTransactionObjects),
                web3jService,
                BubbleBlock.class);
    }

    @Override
    public Request<?, BubbleTransaction> bubbleGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "bub_getTransactionByHash",
                Arrays.asList(transactionHash),
                web3jService,
                BubbleTransaction.class);
    }


    @Override
    public Request<?, BubblePendingTransactions> bubblePendingTx() {
        return new Request<>(
                "bub_pendingTransactions",
                Collections.<String>emptyList(),
                web3jService,
                BubblePendingTransactions.class);
    }


    @Override
    public Request<?, BubbleTransaction> bubbleGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "bub_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                web3jService,
                BubbleTransaction.class);
    }

    @Override
    public Request<?, BubbleTransaction> bubbleGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "bub_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(transactionIndex)),
                web3jService,
                BubbleTransaction.class);
    }

    @Override
    public Request<?, BubbleGetTransactionReceipt> bubbleGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "bub_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                BubbleGetTransactionReceipt.class);
    }

    @Override
    public Request<?, BubbleFilter> bubbleNewFilter(
            com.bubble.protocol.core.methods.request.BubbleFilter bubbleFilter) {
        return new Request<>(
                "bub_newFilter",
                Arrays.asList(bubbleFilter),
                web3jService,
                BubbleFilter.class);
    }

    @Override
    public Request<?, BubbleFilter> bubbleNewBlockFilter() {
        return new Request<>(
                "bub_newBlockFilter",
                Collections.<String>emptyList(),
                web3jService,
                BubbleFilter.class);
    }

    @Override
    public Request<?, BubbleFilter> bubbleNewPendingTransactionFilter() {
        return new Request<>(
                "bub_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3jService,
                BubbleFilter.class);
    }

    @Override
    public Request<?, BubbleUninstallFilter> bubbleUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "bub_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                BubbleUninstallFilter.class);
    }

    @Override
    public Request<?, BubbleLog> bubbleGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "bub_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                BubbleLog.class);
    }

    @Override
    public Request<?, BubbleLog> bubbleGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "bub_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                BubbleLog.class);
    }

    @Override
    public Request<?, BubbleLog> bubbleGetLogs(
            com.bubble.protocol.core.methods.request.BubbleFilter bubbleFilter) {
        return new Request<>(
                "bub_getLogs",
                Arrays.asList(bubbleFilter),
                web3jService,
                BubbleLog.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                web3jService,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                web3jService,
                DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                web3jService,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                web3jService,
                DbGetHex.class);
    }

    @Override
    public Request<?, com.bubble.protocol.core.methods.response.ShhPost> shhPost(com.bubble.protocol.core.methods.request.ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                web3jService,
                com.bubble.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                web3jService,
                ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                web3jService,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                web3jService,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                web3jService,
                ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                web3jService,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                web3jService,
                ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getMessages",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3jService,
                ShhMessages.class);
    }

    @Override
    public Request<?, TxPoolStatus> txPoolStatus() {
        return new Request<>(
                "txpool_status", Collections.<String>emptyList(), web3jService, TxPoolStatus.class);
    }

    @Override
    public Observable<NewHeadsNotification> newHeadsNotifications() {
        return web3jService.subscribe(
                new Request<>(
                        "bub_subscribe",
                        Collections.singletonList("newHeads"),
                        web3jService,
                        BubbleSubscribe.class),
                "bub_unsubscribe",
                NewHeadsNotification.class
        );
    }

    @Override
    public Observable<LogNotification> logsNotifications(
            List<String> addresses, List<String> topics) {

        Map<String, Object> params = createLogsParams(addresses, topics);

        return web3jService.subscribe(
                new Request<>(
                        "bub_subscribe",
                        Arrays.asList("logs", params),
                        web3jService,
                        BubbleSubscribe.class),
                "bub_unsubscribe",
                LogNotification.class
        );
    }

    private Map<String, Object> createLogsParams(List<String> addresses, List<String> topics) {
        Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Observable<String> bubbleBlockHashObservable() {
        return web3jRx.ethBlockHashObservable(blockTime);
    }

    @Override
    public Observable<String> bubblePendingTransactionHashObservable() {
        return web3jRx.ethPendingTransactionHashObservable(blockTime);
    }

    @Override
    public Observable<Log> bubbleLogObservable(
            com.bubble.protocol.core.methods.request.BubbleFilter ethFilter) {
        return web3jRx.ethLogObservable(ethFilter, blockTime);
    }

    @Override
    public Observable<com.bubble.protocol.core.methods.response.Transaction>
    transactionObservable() {
        return web3jRx.transactionObservable(blockTime);
    }

    @Override
    public Observable<com.bubble.protocol.core.methods.response.Transaction>
    pendingTransactionObservable() {
        return web3jRx.pendingTransactionObservable(blockTime);
    }

    @Override
    public Observable<BubbleBlock> blockObservable(boolean fullTransactionObjects) {
        return web3jRx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<BubbleBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return web3jRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<BubbleBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        return web3jRx.replayBlocksObservable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Observable<com.bubble.protocol.core.methods.response.Transaction>
    replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return web3jRx.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<BubbleBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<BubbleBlock> onCompleteObservable) {
        return web3jRx.catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<BubbleBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<com.bubble.protocol.core.methods.response.Transaction>
    catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return web3jRx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<BubbleBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<com.bubble.protocol.core.methods.response.Transaction>
    catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock) {
        return web3jRx.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            web3jService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }
    }

    @Override
    public Request<?, BubbleEvidences> bubbleEvidences() {
        return new Request<>(
                "bub_evidences",
                Collections.<String>emptyList(),
                web3jService,
                BubbleEvidences.class);
    }

	@Override
	public Request<?, AdminProgramVersion> getProgramVersion() {
        return new Request<>(
                "admin_getProgramVersion",
                Collections.<String>emptyList(),
                web3jService,
                AdminProgramVersion.class);
	}

	@Override
	public Request<?, AdminSchnorrNIZKProve> getSchnorrNIZKProve() {
        return new Request<>(
                "admin_getSchnorrNIZKProve",
                Collections.<String>emptyList(),
                web3jService,
                AdminSchnorrNIZKProve.class);
	}

	@Override
	public Request<?, DebugEconomicConfig> getEconomicConfig() {
        return new Request<>(
                "debug_economicConfig",
                Collections.<String>emptyList(),
                web3jService,
                DebugEconomicConfig.class);
	}

    @Override
    public Request<?, BubbleChainId> getChainId() {
        return new Request<>(
                "bub_chainId",
                Collections.<String>emptyList(),
                web3jService,
                BubbleChainId.class);
    }

    @Override
    public Request<?, DebugWaitSlashingNodeList> getWaitSlashingNodeList() {
        return new Request<>(
                "debug_getWaitSlashingNodeList",
                Collections.<String>emptyList(),
                web3jService,
                DebugWaitSlashingNodeList.class);
    }

    @Override
    public Request<?, BubbleRawTransaction> bubbleGetRawTransactionByHash(String transactionHash) {
        return new Request<>(
                "bub_getRawTransactionByHash",
                Arrays.asList(transactionHash),
                web3jService,
                BubbleRawTransaction.class);
    }

    @Override
    public Request<?, BubbleRawTransaction> bubbleGetRawTransactionByBlockHashAndIndex(String blockHash, String index) {
        return new Request<>(
                "bub_getRawTransactionByBlockHashAndIndex",
                Arrays.asList(blockHash,index),
                web3jService,
                BubbleRawTransaction.class);
    }

    @Override
    public Request<?, BubbleRawTransaction> bubbleGetRawTransactionByBlockNumberAndIndex(String blockNumber, String index) {
        return new Request<>(
                "bub_getRawTransactionByBlockNumberAndIndex",
                Arrays.asList(blockNumber,index),
                web3jService,
                BubbleRawTransaction.class);
    }

    @Override
    public Request<?, BubbleGetAddressHrp> bubbleGetAddressHrp() {
        return new Request<>(
                "bub_getAddressHrp",
                Collections.<String>emptyList(),
                web3jService,
                BubbleGetAddressHrp.class);
    }

    @Override
    public Request<?, BubbleSignTransaction> bubbleSignTransaction(com.bubble.protocol.core.methods.request.Transaction transaction) {
        return new Request<>(
                "bub_signTransaction",
                Arrays.asList(transaction),
                web3jService,
                BubbleSignTransaction.class);
    }

    @Override
    public Request<?, BooleanResponse> minerSetGasPrice(String minGasPrice) {
        return new Request<>(
                "miner_setGasPrice",
                Arrays.asList(minGasPrice),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, AdminPeerEvents> adminPeerEvents() {
        return new Request<>(
                "admin_peerEvents",
                Collections.<String>emptyList(),
                web3jService,
                AdminPeerEvents.class);
    }
}
