package com.bubble.contracts.dpos.dto.req;


import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.abi.solidity.datatypes.Uint;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.abi.CustomType;
import com.bubble.rlp.solidity.*;
import com.bubble.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AccountAsset extends CustomType implements Type {

    /**
     * Account address
     */
    private String account;

    /**
     * Native token balances
     */
    private BigInteger nativeAmount ;

    /**
     * Token assets
     */
    AccTokenAsset[] tokenAssets;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigInteger getNativeAmount() {
        return nativeAmount;
    }

    public void setNativeAmount(BigInteger nativeAmount) {
        this.nativeAmount = nativeAmount;
    }

    public AccTokenAsset[] getAccTokenAssets() {
        return tokenAssets;
    }

    public void setAccTokenAssets(AccTokenAsset[] tokenAssets) {
        this.tokenAssets = tokenAssets;
    }

    public List<Type> getSubmitInputParameters() {
        CustomStaticArray<AccTokenAsset> dynamicArray = new CustomStaticArray<>(Arrays.asList(tokenAssets));
        return Arrays.asList(
                new BytesType(account.getBytes(UTF_8)),
                new Uint(nativeAmount),
                dynamicArray);
    }

    public RlpType getRlpEncodeData() {
        List<RlpType> rlpListList = new ArrayList<>();
        rlpListList.add(RlpString.create(Numeric.hexStringToByteArray(account)));
        rlpListList.add(RlpString.create(nativeAmount));
        List<AccTokenAsset> accTokenAssets = Arrays.asList(tokenAssets);
        List<RlpType> accTokenAssetsList = new ArrayList<>();
        for (AccTokenAsset accTokenAsset : accTokenAssets) {
            accTokenAssetsList.add(accTokenAsset.getRlpEncodeData());
        }
        rlpListList.add(new RlpList(accTokenAssetsList));
        return RlpString.create(RlpEncoder.encode(new RlpList(rlpListList)));
    }

    public RlpType getRlpEncodeData2() {
        List<RlpType> rlpListList = new ArrayList<>();
        rlpListList.add(RlpString.create(Numeric.hexStringToByteArray(account)));
        rlpListList.add(RlpString.create(nativeAmount));
        List<AccTokenAsset> accTokenAssets = Arrays.asList(tokenAssets);
        List<RlpType> accTokenAssetsList = new ArrayList<>();
        for (AccTokenAsset accTokenAsset : accTokenAssets) {
            accTokenAssetsList.add(accTokenAsset.getRlpEncodeData());
        }
        rlpListList.add(new RlpList(accTokenAssetsList));
        return new RlpList(rlpListList);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getTypeAsString() {
        return null;
    }
}
