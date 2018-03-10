package com.zxk.exception;


import com.alibaba.fastjson.JSONObject;
import com.zxk.enums.ExceptionEnums;

/**
 * Created by wangyi on 2016/11/17.
 */
public class GatewayException extends Exception {

    public String errorCode;

    public String errorMsg;

    public GatewayException() {
        super();
    }

    public GatewayException(String msg) {
        super(msg);
    }

    public GatewayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errorCode = errCode;
        this.errorMsg = errMsg;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", errorCode);
        jsonObject.put("errorMsg", errorMsg);
        return jsonObject.toString();
    }

    public GatewayException(ExceptionEnums exceptionEnums) {
        this(exceptionEnums.code, exceptionEnums.msg);
        this.errorCode = exceptionEnums.code;
        this.errorMsg = exceptionEnums.msg;
    }
}
