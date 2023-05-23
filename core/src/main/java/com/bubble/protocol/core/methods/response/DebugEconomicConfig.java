package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.response.bean.EconomicConfig;
import com.bubble.utils.JSONUtil;

/**
 * bubble_evidences.
 */
public class DebugEconomicConfig extends Response<String> {

    public EconomicConfig getEconomicConfig() {
        return JSONUtil.parseObject(getResult(), EconomicConfig.class);
    }
}
