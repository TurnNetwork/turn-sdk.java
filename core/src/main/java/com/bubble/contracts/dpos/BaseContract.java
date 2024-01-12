package com.bubble.contracts.dpos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson2.TypeReference;
import com.bubble.abi.solidity.datatypes.Address;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.generated.Uint256;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.CallResponse;
import com.bubble.contracts.dpos.dto.TransactionResponse;
import com.bubble.contracts.dpos.dto.common.ErrorCode;
import com.bubble.contracts.dpos.dto.common.FunctionType;
import com.bubble.contracts.dpos.exception.EstimateGasException;
import com.bubble.contracts.dpos.exception.NoSupportFunctionType;
import com.bubble.contracts.dpos.utils.EncoderUtils;
import com.bubble.contracts.dpos.utils.EstimateGasUtil;
import com.bubble.crypto.Credentials;
import com.bubble.exceptions.MessageDecodingException;
import com.bubble.parameters.NetworkParameters;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.DefaultBlockParameterName;
import com.bubble.protocol.core.RemoteCall;
import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.request.Transaction;
import com.bubble.protocol.core.methods.response.*;
import com.bubble.protocol.exceptions.TransactionException;
import com.bubble.rlp.solidity.RlpDecoder;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.tx.ManagedTransaction;
import com.bubble.tx.RawTransactionManager;
import com.bubble.tx.ReadonlyTransactionManager;
import com.bubble.tx.TransactionManager;
import com.bubble.tx.exceptions.ContractCallException;
import com.bubble.tx.exceptions.BubbleCallException;
import com.bubble.tx.exceptions.BubbleCallTimeoutException;
import com.bubble.tx.gas.ContractGasProvider;
import com.bubble.tx.gas.GasProvider;
import com.bubble.utils.JSONUtil;
import com.bubble.utils.Numeric;
import com.bubble.utils.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 内置合约基类
 * @author chendai
 */
public abstract class BaseContract extends ManagedTransaction {

    private static final Logger log = LoggerFactory.getLogger(BaseContract.class);

    protected String contractAddress;
    protected TransactionReceipt transactionReceipt;

    protected BaseContract(String contractAddress,  Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
        this.contractAddress = contractAddress;
    }

    protected BaseContract(String contractAddress, Web3j web3j, Credentials credentials) {
        this(contractAddress, web3j, new RawTransactionManager(web3j, credentials));
    }

    protected BaseContract(String contractAddress, Web3j web3j) {
        this(contractAddress, web3j, new ReadonlyTransactionManager(web3j, contractAddress));
    }

    public String getContractAddress() {
        return contractAddress;
    }

    protected <T> RemoteCall<CallResponse<T>> executeRemoteCallObjectValueReturn(Function function, Class<T> returnType) {
        return new RemoteCall<>(() -> executeCallObjectValueReturn(function, returnType));
    }

    protected <T> RemoteCall<CallResponse<List<T>>> executeRemoteCallListValueReturn(Function function, Class<T> returnType) {
        return new RemoteCall<>(() -> executeCallListValueReturn(function, returnType));
    }

    private <T> CallResponse<T> executeCallObjectValueReturn(Function function, Class<T> returnType) throws IOException {
        BubbleCall ethCall = web3j.bubbleCall(
                Transaction.createEthCallTransaction(
                        transactionManager.getFromAddress(), contractAddress, EncoderUtils.functionEncoder(function)),
                DefaultBlockParameterName.LATEST)
                .send();

        //判断底层返回的错误信息是否包含超时信息
        if(ethCall.hasError()){
            Response.Error error = ethCall.getError();
            String message = error.getMessage();
            String lowMessage = !Strings.isBlank(message)? message.toLowerCase() : null;
            //包含timeout则抛超时异常，其他错误则直接抛出runtime异常
            if(!Strings.isBlank(lowMessage)
                    && lowMessage.contains("timeout")){
                throw new BubbleCallTimeoutException(error.getCode(),error.getMessage(),ethCall);
            } else {
                throw new BubbleCallException(error.getCode(),error.getMessage(),ethCall);
            }
        }

        String result = Numeric.cleanHexPrefix(ethCall.getValue());
        if(result==null || "".equals(result)){
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        CallRet callRet = JSONUtil.parseObject(new String(Hex.decode(result)), CallRet.class);
        if (callRet == null) {
            throw new ContractCallException("Unable to convert response: " + result);
        }

        CallResponse<T> callResponse = new CallResponse<T>();
        if (callRet.isStatusOk()) {
            callResponse.setCode(callRet.getCode());
            if(BigInteger.class.isAssignableFrom(returnType) ) {
                callResponse.setData((T)numberDecoder(callRet.getRet()));
            }else {
                callResponse.setData(JSONUtil.parseObject(JSONUtil.toJSONString(callRet.getRet()), returnType));
            }
        } else {
            callResponse.setCode(callRet.getCode());
            callResponse.setErrMsg(callRet.getRet().toString());
        }
        return callResponse;
    }

    private BigInteger numberDecoder(Object number) {
        if(number instanceof String) {
            String numberStr = (String)number;
            return Numeric.decodeQuantity(numberStr);
        } else if(number instanceof Number ){
            Number number2 = (Number)number;
            return BigInteger.valueOf(number2.longValue());
        } else {
            throw new MessageDecodingException("Can not decode number value = " + number);
        }
    }

    private <T> CallResponse<List<T>> executeCallListValueReturn(Function function, Class<T> returnType) throws IOException {
        BubbleCall ethCall = web3j.bubbleCall(
                Transaction.createEthCallTransaction(
                        transactionManager.getFromAddress(), contractAddress, EncoderUtils.functionEncoder(function)),
                DefaultBlockParameterName.LATEST)
                .send();

        //判断底层返回的错误信息是否包含超时信息
        if(ethCall.hasError()){
            Response.Error error = ethCall.getError();
            String message = error.getMessage();
            String lowMessage = !Strings.isBlank(message)? message.toLowerCase() : null;
            //包含timeout则抛超时异常，其他错误则直接抛出runtime异常
            if(!Strings.isBlank(lowMessage)
                    && lowMessage.contains("timeout")){
                throw new BubbleCallTimeoutException(error.getCode(),error.getMessage(),ethCall);
            } else {
                throw new BubbleCallException(error.getCode(),error.getMessage(),ethCall);
            }
        }

        String result = Numeric.cleanHexPrefix(ethCall.getValue());
        if(result==null || "".equals(result)){
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        CallRet callRet = JSONUtil.parseObject(new String(Hex.decode(result)), CallRet.class);
        if (callRet == null) {
            throw new ContractCallException("Unable to convert response: " + result);
        }

        CallResponse<List<T>> callResponse = new CallResponse<List<T>>();
        if (callRet.isStatusOk()) {
            callResponse.setCode(callRet.getCode());
            callResponse.setData(JSONUtil.parseArray(JSONUtil.toJSONString(callRet.getRet()), returnType));
        } else {
            callResponse.setCode(callRet.getCode());
            callResponse.setErrMsg(callRet.getRet().toString());
        }

        if(callRet.getCode() == ErrorCode.OBJECT_NOT_FOUND){
            callResponse.setCode(ErrorCode.SUCCESS);
            callResponse.setData(Collections.emptyList());
        }

        return callResponse;
    }

    public static class CallRet{
        @JSONField(name = "Code")
        private int code;
        @JSONField(name = "Ret")
        private Object ret;

        public boolean isStatusOk() {
            return code == ErrorCode.SUCCESS;
        }
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public Object getRet() {
            return ret;
        }
        public void setRet(Object ret) {
            this.ret = ret;
        }

        @Override
        public String toString() {
            return "CallRet [code=" + code + ", ret=" + ret + "]";
        }
    }

    protected RemoteCall<BubbleSendTransaction> executeRemoteCallTransactionStep1(Function function, GasProvider gasProvider) {
        return new RemoteCall<>(() -> executeTransactionStep1(function, BigInteger.ZERO,gasProvider));
    }

    protected RemoteCall<BubbleSendTransaction> executeRemoteCallTransactionStep1(Function function) {
        return new RemoteCall<>(() -> executeTransactionStep1(function, BigInteger.ZERO, getDefaultGasProvider(function)));
    }

    private RemoteCall<TransactionResponse> executeRemoteCallTransactionStep2(BubbleSendTransaction ethSendTransaction) {
        return new RemoteCall<>(() -> executeTransactionStep2(ethSendTransaction));
    }

    private RemoteCall<TransactionResponse> executeRemoteCallTransactionStep3(BubbleSendTransaction ethSendTransaction) {
        return new RemoteCall<>(() -> executeTransactionStep3(ethSendTransaction));
    }

    public RemoteCall<TransactionResponse> getTransactionResponse(BubbleSendTransaction ethSendTransaction){
        return executeRemoteCallTransactionStep2(ethSendTransaction);
    }

    public RemoteCall<TransactionResponse> getTransactionResponse2(BubbleSendTransaction ethSendTransaction){
        return executeRemoteCallTransactionStep3(ethSendTransaction);
    }

    protected RemoteCall<TransactionResponse> executeRemoteCallTransaction(Function function) {
        return new RemoteCall<>(() -> executeTransaction(function, BigInteger.ZERO, getDefaultGasProvider(function)));
    }

    protected RemoteCall<TransactionResponse> executeRemoteCallTransaction(Function function, GasProvider gasProvider) {
        return new RemoteCall<>(() -> executeTransaction(function, BigInteger.ZERO, gasProvider));
    }


    protected GasProvider getDefaultGasProvider(Function function) throws IOException, EstimateGasException {
        return  getDefaultGasProviderRemote(function);
    }

    private GasProvider getDefaultGasProviderRemote(Function function) throws IOException, EstimateGasException {
        Transaction transaction = Transaction.createEthCallTransaction(transactionManager.getFromAddress(), contractAddress, EncoderUtils.functionEncoder(function));
        BubbleEstimateGas bubbleEstimateGas = web3j.bubbleEstimateGas(transaction).send();
        if(bubbleEstimateGas.hasError()){
            if(bubbleEstimateGas.getError().getCode() == ErrorCode.Bubble_Precompiled_Contract_EXEC_FAILED) {
                log.error("estimate gas error, code:={}, message:={}", bubbleEstimateGas.getError().getCode(), bubbleEstimateGas.getError().getData());
                Response.Error error = JSON.parseObject(bubbleEstimateGas.getError().getData(), Response.Error.class);
                //vm执行出错，需要解析出业务错误，并抛出
                throw new EstimateGasException(error.getMessage());
            }else{
                throw new EstimateGasException(bubbleEstimateGas.getError().getMessage());
            }
        }
        BigInteger gasLimit = Numeric.decodeQuantity(bubbleEstimateGas.getResult());
        BigInteger gasPrice = getDefaultGasPrice(function.getType());
        return new ContractGasProvider(gasPrice, gasLimit);
    }

    private GasProvider getDefaultGasProviderLocal(Function function) throws IOException, NoSupportFunctionType {
        BigInteger gasLimit = EstimateGasUtil.getGasLimit(function);
        BigInteger gasPrice = getDefaultGasPrice(function.getType());
        GasProvider gasProvider = new ContractGasProvider(gasPrice, gasLimit);
        return  gasProvider;
    }

    /**
     * 获得默认的gasPrice
     *
     * @param type
     * @return
     * @throws IOException
     */
    private BigInteger getDefaultGasPrice(int type) throws IOException {
        switch (type) {
            case FunctionType.SUBMIT_TEXT_FUNC_TYPE:
                return BigInteger.valueOf(1500000).multiply(BigInteger.valueOf(1000000000));
            case FunctionType.SUBMIT_VERSION_FUNC_TYPE:
                return BigInteger.valueOf(2100000).multiply(BigInteger.valueOf(1000000000));
            case FunctionType.SUBMIR_PARAM_FUNCTION_TYPE:
                return BigInteger.valueOf(2000000).multiply(BigInteger.valueOf(1000000000));
            case FunctionType.SUBMIT_CANCEL_FUNC_TYPE:
                return BigInteger.valueOf(3000000).multiply(BigInteger.valueOf(1000000000));
            default:
                return web3j.bubbleGasPrice().send().getGasPrice();
        }
    }




    private TransactionResponse executeTransaction(Function function, BigInteger vonValue, GasProvider gasProvider)throws TransactionException, IOException {

        TransactionReceipt receipt = send(contractAddress, EncoderUtils.functionEncoder(function), vonValue,
                gasProvider.getGasPrice(),
                gasProvider.getGasLimit());

        return getResponseFromTransactionReceipt(receipt);
    }

    private BubbleSendTransaction executeTransactionStep1(Function function, BigInteger vonValue, GasProvider gasProvider) throws IOException {

        return sendBubbleRawTransaction(contractAddress,  EncoderUtils.functionEncoder(function), vonValue, gasProvider.getGasPrice(), gasProvider.getGasLimit());

    }

    private TransactionResponse executeTransactionStep2(BubbleSendTransaction ethSendTransaction) throws IOException, TransactionException {

        TransactionReceipt receipt = getTransactionReceipt(ethSendTransaction);

        return getResponseFromTransactionReceipt(receipt);
    }

    private TransactionResponse executeTransactionStep3(BubbleSendTransaction ethSendTransaction) throws IOException, TransactionException {

        TransactionReceipt receipt = getTransactionReceipt(ethSendTransaction);

        return getResponseFromTransactionReceipt2(receipt);
    }

    private TransactionResponse getResponseFromTransactionReceipt(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if(logs==null||logs.isEmpty()){
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        String logData = logs.get(0).getData();
        if(null == logData || "".equals(logData) ){
            throw new TransactionException("TransactionReceipt log data is empty");
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCode(statusCode);
        transactionResponse.setTransactionReceipt(transactionReceipt);

        return transactionResponse;
    }

    private TransactionResponse getResponseFromTransactionReceipt2(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if(logs==null||logs.isEmpty()){
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        int statusCode = Integer.decode(transactionReceipt.getStatus());

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setCode(statusCode);
        transactionResponse.setTransactionReceipt(transactionReceipt);

        return transactionResponse;
    }

    protected List<RlpType> decodePPOSLog(TransactionReceipt transactionReceipt) throws TransactionException {
        List<Log> logs = transactionReceipt.getLogs();
        if(logs==null||logs.isEmpty()){
            throw new TransactionException("TransactionReceipt logs is empty");
        }

        String logData = logs.get(0).getData();
        if(null == logData || "".equals(logData) ){
            throw new TransactionException("TransactionReceipt logs[0].data is empty");
        }

        RlpList rlp = RlpDecoder.decode(Numeric.hexStringToByteArray(logData));
        List<RlpType> rlpList = ((RlpList)(rlp.getValues().get(0))).getValues();
        String decodedStatus = new String(((RlpString)rlpList.get(0)).getBytes());
        int statusCode = Integer.parseInt(decodedStatus);

        if(statusCode != ErrorCode.SUCCESS){
            throw new TransactionException("TransactionResponse code is 0");
        }
        return rlpList;
    }
}
