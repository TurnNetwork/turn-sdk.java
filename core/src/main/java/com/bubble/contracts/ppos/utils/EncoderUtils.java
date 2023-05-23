package com.bubble.contracts.ppos.utils;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.IntType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Utf8String;
import com.bubble.contracts.ppos.abi.CustomStaticArray;
import com.bubble.contracts.ppos.abi.CustomType;
import com.bubble.contracts.ppos.abi.Function;
import com.bubble.rlp.solidity.RlpEncoder;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

public class EncoderUtils {

    public static String functionEncoder(Function function){
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(RlpEncoder.encode(RlpString.create(function.getType()))));

        List<Type> parameters = function.getInputParameters();

        if (parameters != null && parameters.size() > 0) {

            for (Type parameter : parameters) {
                if (parameter == null) {
                    result.add(RlpString.EMPTY);
                }else if (parameter instanceof IntType) {
                    result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((IntType) parameter).getValue()))));
                } else if (parameter instanceof BytesType) {
                    result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((BytesType) parameter).getValue()))));
                } else if (parameter instanceof Utf8String) {
                    result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((Utf8String) parameter).getValue()))));
                } else if (parameter instanceof CustomStaticArray) {
                    result.add(((CustomStaticArray) parameter).getRlpEncodeData());
                } else if (parameter instanceof CustomType) {
                    result.add(((CustomType) parameter).getRlpEncodeData());
                }
            }
        }

        String data = Hex.toHexString(RlpEncoder.encode(new RlpList(result)));
        return data;
    }
}
