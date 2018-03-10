package com.zxk.entity;


import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * 网关接受请求对象
 *
 * Created by wangyi on 2016/11/16.
 */
public class HttpRequestDto {

    /**
     * 业务线编号
     */
    private String bizCode;

    /**
     * 签名
     */
    private String sign;

    /**
     * 加密后JSON字符串
     */
    private String context;

    /**
     * 调用接口名称
     */
    private String serviceCode;

    /**
     * 请求时间
     */
    private Date requestTime;


    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
}
