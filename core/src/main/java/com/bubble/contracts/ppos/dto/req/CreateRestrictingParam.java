package com.bubble.contracts.ppos.dto.req;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.bech32.Bech32;
import com.bubble.contracts.ppos.abi.CustomStaticArray;
import com.bubble.contracts.ppos.dto.RestrictingPlan;

import java.util.Arrays;
import java.util.List;

public class CreateRestrictingParam implements Cloneable {
    private String account;
    RestrictingPlan[] plans;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public RestrictingPlan[] getPlans() {
        return plans;
    }

    public void setPlans(RestrictingPlan[] plans) {
        this.plans = plans;
    }

    public List<Type> getSubmitInputParameters() {
        CustomStaticArray<RestrictingPlan> dynamicArray = new CustomStaticArray<>(Arrays.asList(plans));
        return Arrays.asList(
                new BytesType(Bech32.addressDecode(account)),
                dynamicArray);
    }

}
