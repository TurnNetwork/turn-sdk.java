package com.bubble.protocol.core.methods.response;

import com.alibaba.fastjson.JSON;
import com.bubble.protocol.core.Response;
import com.bubble.protocol.core.methods.response.bean.Evidences;

/**
 * bubble_evidences.
 */
public class BubbleEvidences extends Response<String> {

    public String getEvidencesStr() {
        return getResult();
    }
    
    public Evidences getEvidences() {    	
    	return JSON.parseObject(getResult(), Evidences.class); 
    }
}
