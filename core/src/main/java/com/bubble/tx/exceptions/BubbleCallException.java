package com.bubble.tx.exceptions;

import com.bubble.protocol.core.Response;

public class BubbleCallException extends RuntimeException {

    private Response response;
    private int code;
    private String msg;

    public BubbleCallException(int code, String msg, Response response){
        this.code = code;
        this.msg = msg;
        this.response = response;
    }

    public BubbleCallException(int code, String msg, Response response, Throwable ex){
        super(ex);
        this.code = code;
        this.msg = msg;
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
