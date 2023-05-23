//package org.web3j.protocol.core;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.Hash;
//import org.web3j.crypto.RawTransaction;
//import org.web3j.crypto.TransactionEncoder;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.Web3jService;
//import org.web3j.protocol.core.methods.request.Transaction;
//import org.web3j.protocol.core.methods.response.AdminProgramVersion;
//import org.web3j.protocol.core.methods.response.AdminSchnorrNIZKProve;
//import org.web3j.protocol.core.methods.response.DbGetHex;
//import org.web3j.protocol.core.methods.response.DbGetString;
//import org.web3j.protocol.core.methods.response.DbPutHex;
//import org.web3j.protocol.core.methods.response.DbPutString;
//import org.web3j.protocol.core.methods.response.DebugEconomicConfig;
//import org.web3j.protocol.core.methods.response.NetListening;
//import org.web3j.protocol.core.methods.response.NetPeerCount;
//import org.web3j.protocol.core.methods.response.NetVersion;
//import org.web3j.protocol.core.methods.response.BubbleAccounts;
//import org.web3j.protocol.core.methods.response.BubbleBlock;
//import org.web3j.protocol.core.methods.response.BubbleBlockNumber;
//import org.web3j.protocol.core.methods.response.BubbleCall;
//import org.web3j.protocol.core.methods.response.BubbleEstimateGas;
//import org.web3j.protocol.core.methods.response.BubbleEvidences;
//import org.web3j.protocol.core.methods.response.BubbleFilter;
//import org.web3j.protocol.core.methods.response.BubbleGasPrice;
//import org.web3j.protocol.core.methods.response.BubbleGetBalance;
//import org.web3j.protocol.core.methods.response.BubbleGetBlockTransactionCountByHash;
//import org.web3j.protocol.core.methods.response.BubbleGetBlockTransactionCountByNumber;
//import org.web3j.protocol.core.methods.response.BubbleGetCode;
//import org.web3j.protocol.core.methods.response.BubbleGetStorageAt;
//import org.web3j.protocol.core.methods.response.BubbleGetTransactionCount;
//import org.web3j.protocol.core.methods.response.BubbleGetTransactionReceipt;
//import org.web3j.protocol.core.methods.response.BubbleLog;
//import org.web3j.protocol.core.methods.response.BubblePendingTransactions;
//import org.web3j.protocol.core.methods.response.BubbleProtocolVersion;
//import org.web3j.protocol.core.methods.response.BubbleSendTransaction;
//import org.web3j.protocol.core.methods.response.BubbleSign;
//import org.web3j.protocol.core.methods.response.BubbleSyncing;
//import org.web3j.protocol.core.methods.response.BubbleTransaction;
//import org.web3j.protocol.core.methods.response.BubbleUninstallFilter;
//import org.web3j.protocol.core.methods.response.Web3ClientVersion;
//import org.web3j.protocol.core.methods.response.Web3Sha3;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import com.alibaba.fastjson.JSON;
//
///**
// * Verify the functionality of the base API
// *
// * @author lhdeng
// *
// */
//public class JsonRpc2_0ApiTest {
//	private Logger logger = LoggerFactory.getLogger(JsonRpc2_0ApiTest.class);
//
//	static final BigInteger GAS_LIMIT = BigInteger.valueOf(990000);
//	static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000L);
//
//	private long chainId;
//	private String nodeUrl;
//	private String privateKey;
//
//	private Credentials credentials;
//	private String address;
//	private Web3j web3j;
//	private Web3jService web3jService;
//
//	// The solidity smart contract 'HumanStandardToken' address
//	String contractAddress = "0xae362a98cec5bb2a3c8d598dbe825c40b5f1fc14";
//
//	@Before
//	public void init() {
//		chainId = 100L;
//		// nodeUrl = "http://10.10.8.21:8804";
//		nodeUrl = "http://10.1.1.1:8801";
//		// privateKey = "11e20dc277fafc4bc008521adda4b79c2a9e403131798c94eacb071005d43532";
//		privateKey = "3e9516bc43b09dd2754040ad228b9a6c6253c87aa6895318438c7c46002050a6";
//
//		credentials = Credentials.create(privateKey);
//		address = credentials.getAddress();
//		web3jService = new HttpService(nodeUrl);
//		web3j = Web3j.build(web3jService);
//	}
//
//	/**
//	 * Returns the current price per gas in Von.
//	 *
//	 * Result : {"gasPrice":1000000000,"id":0,"jsonrpc":"2.0","result":"0x3b9aca00"}
//	 */
//	@Test
//	public void getGasPrice() {
//		try {
//			BubbleGasPrice bubbleGasPrice = web3j.bubbleGasPrice().send();
//			logger.info("bubbleGasPrice >>> " + JSON.toJSONString(bubbleGasPrice));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the current client version.
//	 *
//	 * Result :
//	 * {"id":0,"jsonrpc":"2.0","result":"Bubblenetwork/bubble/v0.7.5-unstable-331655ad/linux-amd64/go1.12.9","web3ClientVersion":"Bubblenetwork/bubble/v0.7.5-unstable-331655ad/linux-amd64/go1.12.9"}
//	 */
//	@Test
//	public void web3ClientVersion() {
//		try {
//			Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
//			logger.info("web3ClientVersion >>> " + JSON.toJSONString(web3ClientVersion));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns Keccak-256 (not the standardized SHA3-256) of the given data.
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","result":"0x0eccfe5d014e8c46d0a4afdd7ba4a75f269473fb60de682768439deadd8e1165"}
//	 */
//	@Test
//	public void web3Sha3() {
//		// the data to convert into a SHA3 hash.
//		String data = Numeric.toHexString("web3Sha3".getBytes());
//		logger.info("data >>> " + data);
//
//		try {
//			Web3Sha3 web3Sha3 = web3j.web3Sha3(data).send();
//			logger.info("web3Sha3 >>> " + JSON.toJSONString(web3Sha3));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the current network id. The network id in: "1": Bubble Mainnet "2": Morden Testnet (deprecated) "3": Ropsten Testnet "4": Rinkeby
//	 * Testnet "42": Kovan Testnet
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","netVersion":"1","result":"1"}
//	 */
//	@Test
//	public void netVersion() {
//		try {
//			NetVersion netVersion = web3j.netVersion().send();
//			logger.info("netVersion >>> " + JSON.toJSONString(netVersion));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns true if client is actively listening for network connections.
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","listening":true,"result":true}
//	 */
//	@Test
//	public void netListening() {
//		try {
//			NetListening netListening = web3j.netListening().send();
//			logger.info("netListening >>> " + JSON.toJSONString(netListening));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns number of peers currently connected to the client.
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","quantity":0,"result":"0x0"}
//	 */
//	@Test
//	public void netPeerCount() {
//		try {
//			NetPeerCount netPeerCount = web3j.netPeerCount().send();
//			logger.info("netPeerCount >>> " + JSON.toJSONString(netPeerCount));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the current bubble protocol version.
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","protocolVersion":"0x3f","result":"0x3f"}
//	 */
//	@Test
//	public void bubbleProtocolVersion() {
//		BubbleProtocolVersion bubbleProtocolVersion;
//		try {
//			bubbleProtocolVersion = web3j.bubbleProtocolVersion().send();
//			logger.info("bubbleProtocolVersion >>> " + JSON.toJSONString(bubbleProtocolVersion));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns an object with data about the sync status or false. Object|Boolean, An object with sync status data or FALSE, when not syncing:
//	 * startingBlock: QUANTITY - The block at which the import started (will only be reset, after the sync reached his head) currentBlock: QUANTITY -
//	 * The current block, same as bubble_blockNumber highestBlock: QUANTITY - The estimated highest block
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","result":{"syncing":false},"syncing":false}
//	 */
//	@Test
//	public void bubbleSyncing() {
//		try {
//			BubbleSyncing bubbleSyncing = web3j.bubbleSyncing().send();
//			logger.info("bubbleSyncing >>> " + JSON.toJSONString(bubbleSyncing));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns a list of addresses owned by client.
//	 *
//	 * Result :
//	 * {"accounts":["0xe17552158f1b6f6e3244d7e9cdd58d530b6423e5","0xb0d6f0e1bd91ff93c85ee858105136a3e6de1ad4"],"id":0,"jsonrpc":"2.0","result":["0xe17552158f1b6f6e3244d7e9cdd58d530b6423e5","0xb0d6f0e1bd91ff93c85ee858105136a3e6de1ad4"]}
//	 */
//	@Test
//	public void bubbleAccounts() {
//		try {
//			BubbleAccounts bubbleAccounts = web3j.bubbleAccounts().send();
//			logger.info("bubbleAccounts >>> " + JSON.toJSONString(bubbleAccounts));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the number of most recent block.
//	 *
//	 * Result : {"blockNumber":955492,"id":0,"jsonrpc":"2.0","result":"0xe9464"}
//	 */
//	@Test
//	public void bubbleBlockNumber() {
//		try {
//			BubbleBlockNumber bubbleBlockNumber = web3j.bubbleBlockNumber().send();
//			logger.info("bubbleBlockNumber >>> " + JSON.toJSONString(bubbleBlockNumber));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the balance of the account of given address.
//	 *
//	 * Result :
//	 * {"balance":3533694129556768659166595001485837031654967793481217911164800423565239552,"id":0,"jsonrpc":"2.0","result":"0x1ffffffffffffffffffffffffffffffffffffff20a50a31a82520fceca500"}
//	 */
//	@Test
//	public void bubbleGetBalance() {
//		try {
//			BubbleGetBalance bubbleGetBalance = web3j.bubbleGetBalance(address, DefaultBlockParameterName.LATEST).send();
//			logger.info("bubbleGetBalance >>> " + JSON.toJSONString(bubbleGetBalance));
//
//			// integer of the current balance in von.
//			BigInteger balanceVon = bubbleGetBalance.getBalance();
//			BigDecimal balanceLat = Convert.fromVon(new BigDecimal(balanceVon), Convert.Unit.LAT);
//			logger.info("LAT >>> " + balanceLat.toPlainString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the value from a storage position at a given address.
//	 *
//	 * Result : {"data":"0x","id":0,"jsonrpc":"2.0","result":"0x"}
//	 */
//	@Test
//	public void bubbleGetStorageAt() {
//		BigInteger position = BigInteger.valueOf(0L);
//		try {
//			BubbleGetStorageAt bubbleGetStorageAt = web3j.bubbleGetStorageAt(address, position, DefaultBlockParameterName.LATEST).send();
//			logger.info("bubbleGetStorageAt >>> " + JSON.toJSONString(bubbleGetStorageAt));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the number of transactions sent from an address.
//	 *
//	 * Result : {"id":0,"jsonrpc":"2.0","result":"0x23d","transactionCount":573}
//	 */
//	@Test
//	public void bubbleGetTransactionCount() {
//		try {
//			BubbleGetTransactionCount bubbleGetTransactionCount = web3j.bubbleGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
//			logger.info("bubbleGetTransactionCount >>> " + JSON.toJSONString(bubbleGetTransactionCount));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns information about a block by block number.
//	 */
//	@Test
//	public void bubbleGetBlockByNumber() {
//		/**
//		 * QUANTITY|TAG - integer of a block number, or the string "earliest", "latest" or "pending", as in the default block parameter. Boolean - If
//		 * true it returns the full transaction objects, if false only the hashes of the transactions.
//		 */
//		try {
//			BubbleBlock bubbleBlock = web3j.bubbleGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
//			logger.info("bubbleBlock hash >>>> " + bubbleBlock.getBlock().getHash());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the number of transactions in a block from a block matching the given block hash.
//	 */
//	@Test
//	public void bubbleGetBlockTransactionCountByHash() {
//		try {
//			String blockHash = "0x667ab25b762d374dc4c2263acb2273f32a8511390d5fc23b5b2bc4dc1164258a";
//			BubbleGetBlockTransactionCountByHash bubbleGetBlockTransactionCountByNumber = web3j.bubbleGetBlockTransactionCountByHash(blockHash)
//					.send();
//			logger.info("BubbleGetBlockTransactionCountByNumber >>> " + JSON.toJSONString(bubbleGetBlockTransactionCountByNumber));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the number of transactions in a block matching the given block number.
//	 */
//	@Test
//	public void bubbleGetBlockTransactionCountByNumber() {
//		try {
//			BubbleGetBlockTransactionCountByNumber bubbleGetBlockTransactionCountByNumber = web3j
//					.bubbleGetBlockTransactionCountByNumber(DefaultBlockParameterName.EARLIEST).send();
//			logger.info("BubbleGetBlockTransactionCountByNumber >>> " + JSON.toJSONString(bubbleGetBlockTransactionCountByNumber));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns code at a given address.
//	 */
//	@Test
//	public void bubbleGetCode() {
//		try {
//			BubbleGetCode bubbleGetCode = web3j.bubbleGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
//			logger.info("bubbleGetCode >>> " + JSON.toJSONString(bubbleGetCode));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * The sign method calculates an Ethereum specific signature with: sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
//	 *
//	 * Note the address to sign with must be unlocked.
//	 */
//	@Test
//	public void bubbleSign() {
//		String data = "hello bubble";
//		byte[] hash = Hash.sha3(data.getBytes());
//		String sha3HashOfDataToSign = Numeric.toHexString(hash);
//
//		try {
//			BubbleSign bubbleSign = web3j.bubbleSign(address, sha3HashOfDataToSign).send();
//			logger.info("bubbleSign >>> " + JSON.toJSONString(bubbleSign));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Creates new message call transaction or a contract creation, if the data field contains code.
//	 */
//	@Test
//	public void bubbleSendTransaction() {
//		// The address the transaction is send from.
//		String from = address;
//
//		// (optional) Integer of a nonce. This allows to overwrite your own pending transactions that use the same nonce.
//		BigInteger nonce = null;
//		BubbleGetTransactionCount bubbleGetTransactionCount;
//		try {
//			bubbleGetTransactionCount = web3j.bubbleGetTransactionCount(from, DefaultBlockParameterName.LATEST).send();
//			nonce = bubbleGetTransactionCount.getTransactionCount();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		// (optional, default: To-Be-Determined) Integer of the gasPrice used for each paid gas
//		BigInteger gasPrice = GAS_PRICE;
//
//		// (optional, default: 90000) Integer of the gas provided for the transaction execution. It will return unused gas.
//		BigInteger gasLimit = GAS_LIMIT;
//
//		// (optional when creating new contract) The address the transaction is directed to.
//		String to = "0x31ac3dad7fa96b62d58b2be229575db40aa28b2c";
//
//		// (optional) Integer of the value sent with this transaction
//		BigInteger value = BigInteger.valueOf(20000L);
//
//		// The compiled code of a contract OR the hash of the invoked method signature and encoded parameters.
//		String data = "";
//		Transaction transaction = new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
//		try {
//			BubbleSendTransaction bubbleSendTransaction = web3j.bubbleSendTransaction(transaction).send();
//			logger.info("bubbleSendTransaction >>> " + JSON.toJSONString(bubbleSendTransaction));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Creates new message call transaction or a contract creation for signed transactions.
//	 */
//	@Test
//	public void bubbleSendRawTransaction() {
//		// The address the transaction is send from.
//		String from = address;
//
//		// (optional) Integer of a nonce. This allows to overwrite your own pending transactions that use the same nonce.
//		BigInteger nonce = null;
//		BubbleGetTransactionCount bubbleGetTransactionCount;
//		try {
//			bubbleGetTransactionCount = web3j.bubbleGetTransactionCount(from, DefaultBlockParameterName.LATEST).send();
//			nonce = bubbleGetTransactionCount.getTransactionCount();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		// (optional, default: To-Be-Determined) Integer of the gasPrice used for each paid gas
//		BigInteger gasPrice = GAS_PRICE;
//
//		// (optional, default: 90000) Integer of the gas provided for the transaction execution. It will return unused gas.
//		BigInteger gasLimit = GAS_LIMIT;
//
//		// (optional when creating new contract) The address the transaction is directed to.
//		String to = "0x31ac3dad7fa96b62d58b2be229575db40aa28b2c";
//
//		// (optional) Integer of the value sent with this transaction
//		BigInteger value = BigInteger.valueOf(20000L);
//
//		// The compiled code of a contract OR the hash of the invoked method signature and encoded parameters.
//		String data = "";
//
//		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
//		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
//		String hexValue = Numeric.toHexString(signedMessage);
//		BubbleSendTransaction bubbleSendTransaction;
//		try {
//			bubbleSendTransaction = web3j.bubbleSendRawTransaction(hexValue).send();
//			logger.info("bubbleSendTransaction >>> " + JSON.toJSONString(bubbleSendTransaction));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
//
//	/**
//	 * Executes a new message call immediately without creating a transaction on the block chain.
//	 */
//	@Test
//	public void platCall() {
//		// The address the transaction is send from.
//		String from = address;
//
//		// (optional) Integer of a nonce. This allows to overwrite your own pending transactions that use the same nonce.
//		BigInteger nonce = null;
//		BubbleGetTransactionCount bubbleGetTransactionCount;
//		try {
//			bubbleGetTransactionCount = web3j.bubbleGetTransactionCount(from, DefaultBlockParameterName.LATEST).send();
//			nonce = bubbleGetTransactionCount.getTransactionCount();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		// (optional, default: To-Be-Determined) Integer of the gasPrice used for each paid gas
//		BigInteger gasPrice = GAS_PRICE;
//
//		// (optional, default: 90000) Integer of the gas provided for the transaction execution. It will return unused gas.
//		BigInteger gasLimit = GAS_LIMIT;
//
//		// (optional when creating new contract) The address the transaction is directed to.
//		String to = "0x31ac3dad7fa96b62d58b2be229575db40aa28b2c";
//
//		// (optional) Integer of the value sent with this transaction
//		BigInteger value = BigInteger.valueOf(20000L);
//
//		// The compiled code of a contract OR the hash of the invoked method signature and encoded parameters.
//		String data = "";
//		Transaction transaction = new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
//
//		try {
//			BubbleCall bubbleCall = web3j.bubbleCall(transaction, DefaultBlockParameterName.LATEST).send();
//			logger.info("bubbleCall >>> " + JSON.toJSONString(bubbleCall));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Generates and returns an estimate of how much gas is necessary to allow the transaction to complete. The transaction will not be added to the
//	 * blockchain. Note that the estimate may be significantly more than the amount of gas actually used by the transaction, for a variety of reasons
//	 * including EVM mechanics and node performance.
//	 */
//	@Test
//	public void bubbleEstimateGas() {
//		// The address the transaction is send from.
//		String from = address;
//
//		// (optional) Integer of a nonce. This allows to overwrite your own pending transactions that use the same nonce.
//		BigInteger nonce = null;
//		BubbleGetTransactionCount bubbleGetTransactionCount;
//		try {
//			bubbleGetTransactionCount = web3j.bubbleGetTransactionCount(from, DefaultBlockParameterName.LATEST).send();
//			nonce = bubbleGetTransactionCount.getTransactionCount();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		// (optional, default: To-Be-Determined) Integer of the gasPrice used for each paid gas
//		BigInteger gasPrice = GAS_PRICE;
//
//		// (optional, default: 90000) Integer of the gas provided for the transaction execution. It will return unused gas.
//		BigInteger gasLimit = GAS_LIMIT;
//
//		// (optional when creating new contract) The address the transaction is directed to.
//		String to = "0x31ac3dad7fa96b62d58b2be229575db40aa28b2c";
//
//		// (optional) Integer of the value sent with this transaction
//		BigInteger value = BigInteger.valueOf(20000L);
//
//		// The compiled code of a contract OR the hash of the invoked method signature and encoded parameters.
//		String data = "";
//		Transaction transaction = new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
//
//		try {
//			BubbleEstimateGas bubbleEstimateGas = web3j.bubbleEstimateGas(transaction).send();
//			logger.info("bubbleEstimateGas >>> " + JSON.toJSONString(bubbleEstimateGas));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns information about a block by hash.
//	 */
//	@Test
//	public void bubbleGetBlockByHash() {
//		String blockHash = "0x667ab25b762d374dc4c2263acb2273f32a8511390d5fc23b5b2bc4dc1164258a";
//		try {
//			BubbleBlock bubbleBlock = web3j.bubbleGetBlockByHash(blockHash, true).send();
//			logger.info("bubbleBlock hash >>> " + bubbleBlock.getBlock().getHash());
//			logger.info("bubbleBlock author >>> " + bubbleBlock.getBlock().getAuthor());
//			logger.info("bubbleBlock number >>> " + bubbleBlock.getBlock().getNumber());
//			logger.info("bubbleBlock nonce >>> " + bubbleBlock.getBlock().getNonce());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns information about a transaction by block hash and transaction index position.
//	 */
//	@Test
//	public void bubbleGetTransactionByBlockHashAndIndex() {
//		String blockHash = "0x0a0d04b01aa6e04b96eb2343eb4e0d5a3d9dda0c11a41799d1c7b8f4a9a07a02";
//		BigInteger transactionIndex = BigInteger.valueOf(1L);
//		try {
//			BubbleTransaction bubbleTransaction = web3j.bubbleGetTransactionByBlockHashAndIndex(blockHash, transactionIndex).send();
//			logger.info("bubbleTransaction >>> " + JSON.toJSONString(bubbleTransaction));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the information about a transaction requested by transaction hash.
//	 */
//	@Test
//	public void bubbleGetTransactionByHash() {
//		String transactionHash = "0x8c35272a7c95ab38b9cd44180350ea18925220dc7b34d8e01a0167f5b299afec";
//		try {
//			BubbleTransaction bubbleTransaction = web3j.bubbleGetTransactionByHash(transactionHash).send();
//			logger.info("bubbleTransaction >>> " + JSON.toJSONString(bubbleTransaction));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns information about a transaction by block number and transaction index position.
//	 */
//	@Test
//	public void bubbleGetTransactionByBlockNumberAndIndex() {
//		long blockNumber = 958697L;
//		BigInteger transactionIndex = BigInteger.valueOf(0L);
//		try {
//			BubbleTransaction bubbleTransaction = web3j
//					.bubbleGetTransactionByBlockNumberAndIndex(new DefaultBlockParameterNumber(blockNumber), transactionIndex).send();
//			logger.info("bubbleTransaction >>> " + JSON.toJSONString(bubbleTransaction));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the receipt of a transaction by transaction hash.
//	 *
//	 * Note: That the receipt is not available for pending transactions.
//	 */
//	@Test
//	public void bubbleGetTransactionReceipt() {
//		String transactionHash = "0x8c35272a7c95ab38b9cd44180350ea18925220dc7b34d8e01a0167f5b299afec";
//		try {
//			BubbleGetTransactionReceipt bubbleGetTransactionReceipt = web3j.bubbleGetTransactionReceipt(transactionHash).send();
//			logger.info("bubbleGetTransactionReceipt >>> " + JSON.toJSONString(bubbleGetTransactionReceipt));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Creates a filter object, based on filter options, to notify when the state changes (logs). To check if the state has changed, call
//	 * bubble_getFilterChanges.
//	 */
//	@Test
//	public void bubbleNewFilter() {
//		org.web3j.protocol.core.methods.request.BubbleFilter bubbleFilter = new org.web3j.protocol.core.methods.request.BubbleFilter(
//				DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress);
//		try {
//			BubbleFilter bubbleFilter_res = web3j.bubbleNewFilter(bubbleFilter).send();
//			logger.info("bubbleFilter_res >>> " + JSON.toJSONString(bubbleFilter_res));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Creates a filter in the node, to notify when a new block arrives. To check if the state has changed, call bubble_getFilterChanges.
//	 */
//	@Test
//	public void bubbleNewBlockFilter() {
//		try {
//			BubbleFilter bubbleFilter = web3j.bubbleNewBlockFilter().send();
//			logger.info("bubbleFilter >>> " + JSON.toJSONString(bubbleFilter));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Creates a filter in the node, to notify when new pending transactions arrive. To check if the state has changed, call bubble_getFilterChanges.
//	 */
//	@Test
//	public void bubbleNewPendingTransactionFilter() {
//		try {
//			BubbleFilter bubbleFilter = web3j.bubbleNewPendingTransactionFilter().send();
//			logger.info("bubbleFilter >>> " + JSON.toJSONString(bubbleFilter));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Uninstalls a filter with given id. Should always be called when watch is no longer needed. Additonally Filters timeout when they aren't
//	 * requested with bubble_getFilterChanges for a period of time.
//	 */
//	@Test
//	public void bubbleUninstallFilter() {
//		BigInteger filterId = new BigInteger("258575391212208728421850232718129168292", 10);
//		try {
//			BubbleUninstallFilter bubbleUninstallFilter = web3j.bubbleUninstallFilter(filterId).send();
//			logger.info("bubbleUninstallFilter >>> " + JSON.toJSONString(bubbleUninstallFilter));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Polling method for a filter, which returns an array of logs which occurred since last poll.
//	 */
//	@Test
//	public void bubbleGetFilterChanges() {
//		BigInteger filterId = new BigInteger("336685864917132318099501088544437319929", 10);
//		try {
//			BubbleLog bubbleLog = web3j.bubbleGetFilterChanges(filterId).send();
//			logger.info("bubbleLog >>> " + JSON.toJSONString(bubbleLog));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns an array of all logs matching filter with given id.
//	 */
//	@Test
//	public void bubbleGetFilterLogs() {
//		BigInteger filterId = new BigInteger("336685864917132318099501088544437319929", 10);
//		try {
//			BubbleLog bubbleLog = web3j.bubbleGetFilterLogs(filterId).send();
//			logger.info("bubbleLog >>> " + JSON.toJSONString(bubbleLog));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns an array of all logs matching a given filter object.
//	 */
//	@Test
//	public void bubbleGetLogs() {
//		org.web3j.protocol.core.methods.request.BubbleFilter bubbleFilter = new org.web3j.protocol.core.methods.request.BubbleFilter(
//				DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contractAddress);
//		try {
//			BubbleLog bubbleLog = web3j.bubbleGetLogs(bubbleFilter).send();
//			logger.info("bubbleLog >>> " + JSON.toJSONString(bubbleLog));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns the pending transactions list.
//	 */
//	@Test
//	public void bubblePendingTransactions() {
//		try {
//			BubblePendingTransactions bubblePendingTransactions = web3j.bubblePendingTx().send();
//			logger.info("bubblePendingTransactions >>> " + JSON.toJSONString(bubblePendingTransactions));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Stores a string in the local database.
//	 */
//	@Test
//	public void dbPutString() {
//		String databaseName = "bubble";
//		String keyName = "key";
//		String stringToStore = "hello bubble";
//		try {
//			DbPutString dbPutString = web3j.dbPutString(databaseName, keyName, stringToStore).send();
//			logger.info("dbPutString >>> " + JSON.toJSONString(dbPutString));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns string from the local database.
//	 */
//	@Test
//	public void dbGetString() {
//		String databaseName = "bubble";
//		String keyName = "key";
//		try {
//			DbGetString dbGetString = web3j.dbGetString(databaseName, keyName).send();
//			logger.info("dbGetString >>> " + JSON.toJSONString(dbGetString));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Stores binary data in the local database.
//	 */
//	@Test
//	public void dbPutHex() {
//		String databaseName = "bubble";
//		String keyName = "key2";
//		String dataToStore = "0x68656c6c6f20776f726c64";
//		try {
//			DbPutHex dbPutHex = web3j.dbPutHex(databaseName, keyName, dataToStore).send();
//			logger.info("dbPutHex >>> " + JSON.toJSONString(dbPutHex));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Returns binary data from the local database.
//	 */
//	@Test
//	public void dbGetHex() {
//		String databaseName = "bubble";
//		String keyName = "key2";
//		try {
//			DbGetHex dbGetHex = web3j.dbGetHex(databaseName, keyName).send();
//			logger.info("dbGetHex >>> " + JSON.toJSONString(dbGetHex));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 返回双签举报数据
//	 */
//	@Test
//	public void bubbleEvidences() {
//		try {
//			BubbleEvidences bubbleEvidences = web3j.bubbleEvidences().send();
//			logger.info("bubbleEvidences >>> " + JSON.toJSONString(bubbleEvidences));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 获取代码版本
//	 */
//	@Test
//	public void getProgramVersion() {
//		try {
//			AdminProgramVersion adminProgramVersion = web3j.getProgramVersion().send();
//			logger.info("adminProgramVersion >>> " + JSON.toJSONString(adminProgramVersion));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 获取bls的证明
//	 */
//	@Test
//	public void getSchnorrNIZKProve() {
//		try {
//			AdminSchnorrNIZKProve adminSchnorrNIZKProve = web3j.getSchnorrNIZKProve().send();
//			logger.info("adminSchnorrNIZKProve >>> " + JSON.toJSONString(adminSchnorrNIZKProve));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 获取Bubble参数配置
//	 */
//	@Test
//	public void getEconomicConfig() {
//		try {
//			DebugEconomicConfig debugEconomicConfig = web3j.getEconomicConfig().send();
//			logger.info("debugEconomicConfig >>> " + JSON.toJSONString(debugEconomicConfig));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
