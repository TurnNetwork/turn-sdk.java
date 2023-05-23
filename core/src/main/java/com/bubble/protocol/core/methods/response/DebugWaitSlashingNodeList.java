package com.bubble.protocol.core.methods.response;

import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.response.bean.WaitSlashingNode;
import com.bubble.utils.JSONUtil;

import java.util.List;

/**
 * debug_getWaitSlashingNodeList
 */
public class DebugWaitSlashingNodeList extends Response<String> {

    public List<WaitSlashingNode> get() {
        return JSONUtil.parseArray(getResult(), WaitSlashingNode.class);
    }
}
