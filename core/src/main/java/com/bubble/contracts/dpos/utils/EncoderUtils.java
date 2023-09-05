package com.bubble.contracts.dpos.utils;

import com.alibaba.fastjson.JSONObject;
import com.bubble.abi.solidity.datatypes.*;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.abi.CustomType;
import com.bubble.contracts.dpos.abi.Function;
import com.bubble.contracts.dpos.dto.req.AccountAsset;
import com.bubble.rlp.solidity.RlpEncoder;
import com.bubble.rlp.solidity.RlpList;
import com.bubble.rlp.solidity.RlpString;
import com.bubble.rlp.solidity.RlpType;
import com.bubble.utils.Numeric;
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
                } else if (parameter instanceof Bool) {
                    result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((Bool) parameter).getValue()))));
                } else if (parameter instanceof CustomStaticArray) {
                    result.add(((CustomStaticArray) parameter).getRlpEncodeData());
                } else if (parameter instanceof CustomType) {
                    result.add(((CustomType) parameter).getRlpEncodeData());
                }
            }
        }
        return Numeric.toHexString(RlpEncoder.encode(new RlpList(result)));
    }
}
