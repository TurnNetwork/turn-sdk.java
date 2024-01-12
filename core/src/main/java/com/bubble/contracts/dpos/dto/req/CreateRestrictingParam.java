package com.bubble.contracts.dpos.dto.req;

import com.bubble.abi.solidity.datatypes.BytesType;
import com.bubble.abi.solidity.datatypes.Type;
import com.bubble.contracts.dpos.abi.CustomStaticArray;
import com.bubble.contracts.dpos.dto.RestrictingPlan;

import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

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
                new BytesType(account.getBytes(UTF_8)),
                dynamicArray);
    }

}
