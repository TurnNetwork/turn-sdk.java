package com.bubble.protocol.core;

import com.bubble.protocol.admin.methods.response.BooleanResponse;
import com.bubble.protocol.admin.methods.response.TxPoolStatus;
import com.bubble.protocol.admin.methods.response.admin.AdminDataDir;
import com.bubble.protocol.core.methods.response.DebugWaitSlashingNodeList;
import com.bubble.protocol.core.methods.response.BubbleSignTransaction;
import com.bubble.protocol.core.methods.request.ShhFilter;
import com.bubble.protocol.core.methods.response.*;

import java.math.BigInteger;

/**
 * Core Ethereum JSON-RPC API.
 */
public interface Bubble {
    Request<?, Web3ClientVersion> web3ClientVersion();

    Request<?, Web3Sha3> web3Sha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, AdminNodeInfo> adminNodeInfo();

    Request<?, AdminPeers> adminPeers();

    Request<?, BooleanResponse> adminAddPeer(String url);

    Request<?, BooleanResponse> adminRemovePeer(String url);

    Request<?, AdminDataDir> adminDataDir();

    Request<?, BooleanResponse> adminStartRPC(String host,int port,String cors,String apis);

    Request<?, BooleanResponse> adminStartWS(String host,int port,String cors,String apis);

    Request<?, BooleanResponse> adminStopRPC();

    Request<?, BooleanResponse> adminStopWS();

    Request<?, BooleanResponse> adminExportChain(String file);

    Request<?, BooleanResponse> adminImportChain(String file);

    Request<?, BubbleProtocolVersion> bubbleProtocolVersion();

    Request<?, BubbleSyncing> bubbleSyncing();

    Request<?, BubbleGasPrice> bubbleGasPrice();

    Request<?, BubbleAccounts> bubbleAccounts();

    Request<?, BubbleBlockNumber> bubbleBlockNumber();

    Request<?, BubbleGetBalance> bubbleGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleGetStorageAt> bubbleGetStorageAt(
            String address, BigInteger position,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleGetTransactionCount> bubbleGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleGetBlockTransactionCountByHash> bubbleGetBlockTransactionCountByHash(
            String blockHash);

    Request<?, BubbleGetBlockTransactionCountByNumber> bubbleGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleGetCode> bubbleGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleSign> bubbleSign(String address, String sha3HashOfDataToSign);

    Request<?, BubbleSendTransaction> bubbleSendTransaction(
            com.bubble.protocol.core.methods.request.Transaction transaction);

    Request<?, BubbleSendTransaction> bubbleSendRawTransaction(
            String signedTransactionData);

    Request<?, BubbleCall> bubbleCall(
            com.bubble.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, BubbleEstimateGas> bubbleEstimateGas(
            com.bubble.protocol.core.methods.request.Transaction transaction);

    Request<?, BubbleBlock> bubbleGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, BubbleBlock> bubbleGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);

    Request<?, BubbleTransaction> bubbleGetTransactionByHash(String transactionHash);

    Request<?, BubbleTransaction> bubbleGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex);

    Request<?, BubbleTransaction> bubbleGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, BubbleGetTransactionReceipt> bubbleGetTransactionReceipt(String transactionHash);

    Request<?, BubbleFilter> bubbleNewFilter(com.bubble.protocol.core.methods.request.BubbleFilter ethFilter);

    Request<?, BubbleFilter> bubbleNewBlockFilter();

    Request<?, BubbleFilter> bubbleNewPendingTransactionFilter();

    Request<?, BubbleUninstallFilter> bubbleUninstallFilter(BigInteger filterId);

    Request<?, BubbleLog> bubbleGetFilterChanges(BigInteger filterId);

    Request<?, BubbleLog> bubbleGetFilterLogs(BigInteger filterId);

    Request<?, BubbleLog> bubbleGetLogs(com.bubble.protocol.core.methods.request.BubbleFilter ethFilter);

    Request<?, BubblePendingTransactions> bubblePendingTx();

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, ShhPost> shhPost(
            com.bubble.protocol.core.methods.request.ShhPost shhPost);

    Request<?, ShhVersion> shhVersion();

    Request<?, ShhNewIdentity> shhNewIdentity();

    Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress);

    Request<?, ShhNewGroup> shhNewGroup();

    Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress);

    Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter);

    Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId);

    Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId);

    Request<?, ShhMessages> shhGetMessages(BigInteger filterId);

    Request<?, TxPoolStatus> txPoolStatus();

    Request<?, BubbleEvidences> bubbleEvidences();
    
    Request<?, AdminProgramVersion> getProgramVersion();
    
    Request<?, AdminSchnorrNIZKProve> getSchnorrNIZKProve();
    
    Request<?, DebugEconomicConfig> getEconomicConfig();

    Request<?, BubbleChainId> getChainId();

    Request<?, DebugWaitSlashingNodeList> getWaitSlashingNodeList();

    Request<?, BubbleRawTransaction> bubbleGetRawTransactionByHash(String transactionHash);

    Request<?, BubbleRawTransaction> bubbleGetRawTransactionByBlockHashAndIndex(String blockHash, String index);

    Request<?, BubbleRawTransaction> bubbleGetRawTransactionByBlockNumberAndIndex(String blockNumber, String index);

    Request<?, BubbleGetAddressHrp> bubbleGetAddressHrp();

    Request<?, BubbleSignTransaction> bubbleSignTransaction(com.bubble.protocol.core.methods.request.Transaction transaction);

    Request<?, BooleanResponse> minerSetGasPrice(String minGasPrice);

    Request<?, AdminPeerEvents> adminPeerEvents();
}
