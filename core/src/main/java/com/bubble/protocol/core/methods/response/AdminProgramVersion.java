package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.response.bean.ProgramVersion;

/**
 * bubble_evidences.
 */
public class AdminProgramVersion extends Response<ProgramVersion> {

    public ProgramVersion getAdminProgramVersion() {
        return getResult();
    }
}
