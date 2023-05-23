package com.bubble.protocol.admin.methods.response;


import com.bubble.protocol.core.Response;

/** personal_importRawKey. */
public class PersonalImportRawKey extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
