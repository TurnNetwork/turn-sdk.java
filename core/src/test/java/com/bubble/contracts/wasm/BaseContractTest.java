package com.bubble.contracts.wasm;

import com.bubble.crypto.Credentials;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.Web3jService;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.RawTransactionManager;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.gas.ContractGasProvider;
import com.bubble.tx.gas.GasProvider;
import org.junit.Before;

import java.math.BigInteger;

public abstract class BaseContractTest {
	protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4712388L);
	protected static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);

	protected static final String nodeUrl = "http://192.168.120.150:6789";
	protected static final String privateKey = "0x3a4130e4abb887a296eb38c15bbd83253ab09492a505b10a54b008b7dcc1668";

	protected Credentials credentials;
	protected String credentialsAddress;
	protected Web3j web3j;
	protected Web3jService web3jService;
	protected TransactionManager transactionManager;
	protected GasProvider gasProvider;

	@Before
	public void init() {
		credentials = Credentials.create(privateKey);
		credentialsAddress = credentials.getAddress();
		web3jService = new HttpService(nodeUrl);
		web3j = Web3j.build(web3jService);
		transactionManager = new RawTransactionManager(web3j, credentials);
		gasProvider = new ContractGasProvider(GAS_PRICE, GAS_LIMIT);
	}
}
