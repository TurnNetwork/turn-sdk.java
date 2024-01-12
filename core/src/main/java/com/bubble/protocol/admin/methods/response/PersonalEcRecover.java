package com.bubble.protocol.admin.methods.response;


import com.bubble.protocol.core.Response;

/** personal_ecRecover. */
public class PersonalEcRecover extends Response<String> {
    public String getRecoverAccountId() {
        return getResult();
    }
}
