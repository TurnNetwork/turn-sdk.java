package com.bubble.protocol.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bubble.abi.solidity.FunctionEncoder;
import com.bubble.abi.solidity.TypeReference;
import com.bubble.abi.solidity.datatypes.Address;
import com.bubble.abi.solidity.datatypes.Function;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.abi.solidity.datatypes.generated.Uint256;
import com.bubble.crypto.Credentials;
import com.bubble.crypto.RawTransaction;
import com.bubble.crypto.TransactionEncoder;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.request.BubbleFilter;
import com.bubble.protocol.core.methods.request.Transaction;
import com.bubble.protocol.core.methods.response.*;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.RawTransactionManager;
import com.bubble.tx.Transfer;
import com.bubble.tx.gas.DefaultGasProvider;
import com.bubble.utils.Convert;
import com.bubble.utils.Numeric;

import java.io.Console;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;


public class Test {

    public void test(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BigInteger blockNumber = web3j.bubbleBlockNumber().send().getBlockNumber();
            System.out.println(blockNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void test1(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BubbleBlock.Block block = web3j.bubbleGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(705537)), true).send().getBlock();
            System.out.println(JSONObject.toJSON(block));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test2(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BubbleBlock.Block block = web3j.bubbleGetBlockByHash("0x5e2eee540b421ac955d320f0fb265308f301fe4fa189b153845a82194c1af06a", true).send().getBlock();
            System.out.println(JSONObject.toJSON(block));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test3(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BigInteger transactionCount = web3j.bubbleGetBlockTransactionCountByHash("0x4f1fdcb578fcbd4034ea3c90767647b3f0dd29b292197a2b1eb248b21f473952").send().getTransactionCount();
            System.out.println(JSONObject.toJSON(transactionCount));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test4(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BigInteger transactionCount = web3j.bubbleGetTransactionCount("0x36c756417E63F740d83908d5216310A4603d6ecc",DefaultBlockParameter.valueOf(BigInteger.valueOf(705537))).send().getTransactionCount();
            System.out.println(JSONObject.toJSON(transactionCount));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test5(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BubbleTransaction send = web3j.bubbleGetTransactionByBlockNumberAndIndex(DefaultBlockParameter.valueOf(BigInteger.valueOf(705537)), BigInteger.ZERO).send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test6(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            AdminNodeInfo adminNodeInfo = web3j.adminNodeInfo().send();
            System.out.println(JSONObject.toJSON(adminNodeInfo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test7(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BubbleChainId chainId = web3j.getChainId().send();
            System.out.println(JSONObject.toJSON(chainId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test8(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            DebugEconomicConfig config = web3j.getEconomicConfig().send();
            System.out.println(JSONObject.toJSON(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test9(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        try {
            BubbleGetBalance bubbleGetBalance = web3j.bubbleGetBalance("0x36c756417E63F740d83908d5216310A4603d6ecc", DefaultBlockParameterName.LATEST).send();
            System.out.println(JSONObject.toJSON(bubbleGetBalance));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test10(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:7789"));
        String from = "0x36c756417E63F740d83908d5216310A4603d6ecc";
        String to = "0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A";
        String symbolContractAddress = "0x2f3d4C6Ac5e816f4E104DFedF8bA73bAeD28bBAC";
        BigInteger txVale = BigInteger.valueOf(10L);
        try {
            BigInteger transactionCount = web3j.bubbleGetTransactionCount("0x36c756417E63F740d83908d5216310A4603d6ecc",DefaultBlockParameterName.LATEST).send().getTransactionCount();
            Function function = new Function("Transfer",
                    Arrays.<Type>asList(new Address(
                                    symbolContractAddress),
                            new Address(
                                    to),
                            new Uint256(
                                    txVale)),
                    Collections.<TypeReference<?>>emptyList());
            String data = FunctionEncoder.encode(function);
            Transaction ethCallTransaction = Transaction.createEthCallTransaction(
                    from,
                    to, data);
            BubbleEstimateGas bubbleEstimateGas = web3j.bubbleEstimateGas(ethCallTransaction).send();
            System.out.println(JSONObject.toJSON(bubbleEstimateGas));
            System.out.println(Numeric.decodeQuantity(bubbleEstimateGas.getResult()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test11(String[] args) {
        Credentials credentials = Credentials.create(
                "daf9e8a3b0bd17a28abbfb5a8e9e9c052734a754c7a707f9bf27cd1b4a0d31b5");
        Web3j web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        Transfer transfer = new Transfer(web3j, transactionManager);
        RemoteCall<TransactionReceipt> transactionReceiptRemoteCall = transfer.sendFunds("0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A",
                new BigDecimal(1000), Convert.Unit.VON);
        try {
            System.out.println(transactionReceiptRemoteCall.send().getBlockHash());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test12(String[] args) {
        NetworkParameters.init(100);
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        String from = "0xAE58Bf1855dF1e4067ea3308f22335FA16D5644A";
        String to = "0x36c756417E63F740d83908d5216310A4603d6ecc";
        DefaultGasProvider defaultGasProvider = new DefaultGasProvider();
        BigInteger txValue = BigInteger.valueOf(10000000L);
        try {
            Transaction transaction = Transaction.createEtherTransaction(from, BigInteger.valueOf(3L), defaultGasProvider.getGasPrice(), defaultGasProvider.getGasLimit(), to, txValue);
            BubbleCall send = web3j.bubbleCall(transaction, DefaultBlockParameterName.LATEST).send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void test13(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubbleFilter bubbleFilter = new BubbleFilter();
            bubbleFilter.addSingleTopic("0x36c756417E63F740d83908d521");
            com.bubble.protocol.core.methods.response.BubbleFilter filter = web3j.bubbleNewBlockFilter().send();
            System.out.println(JSONObject.toJSON(filter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test14(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            com.bubble.protocol.core.methods.response.BubbleFilter filter = web3j.bubbleNewPendingTransactionFilter().send();
            System.out.println(JSONObject.toJSON(filter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test15(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            com.bubble.protocol.core.methods.response.BubbleFilter filter = web3j.bubbleNewPendingTransactionFilter().send();
            System.out.println(JSONObject.toJSON(filter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void test16(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubbleLog bubbleLog = web3j.bubbleGetFilterChanges(BigInteger.ZERO).send();
            System.out.println(JSONObject.toJSON(bubbleLog));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test17(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubbleFilter bubbleFilter = new BubbleFilter();
            bubbleFilter.addSingleTopic("0xf66778a71ad05be3533189f52b3685653815adca5f24272e139571b8e1892f5e");
            BubbleLog bubbleLog = web3j.bubbleGetLogs(bubbleFilter).send();
            System.out.println(JSONObject.toJSON(bubbleLog));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test18(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubblePendingTransactions send = web3j.bubblePendingTx().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubbleEvidences send = web3j.bubbleEvidences().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test20(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            AdminProgramVersion send = web3j.getProgramVersion().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test21(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            AdminSchnorrNIZKProve send = web3j.getSchnorrNIZKProve().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test22(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            DebugEconomicConfig send = web3j.getEconomicConfig().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test23(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            BubbleChainId send = web3j.getChainId().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test24(String[] args) {
        Web3j  web3j = Web3j.build(new HttpService("http://192.168.31.117:6789"));
        try {
            DebugWaitSlashingNodeList send = web3j.getWaitSlashingNodeList().send();
            System.out.println(JSONObject.toJSON(send));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
