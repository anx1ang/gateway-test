package com.zxk.entity;



import com.zxk.enums.ExceptionEnums;

import java.util.Date;

/**
 * 网关返回对象
 *
 * Created by wangyi on 2016/11/16.
 */
public class HttpResponseDto {

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 各个系统返回数据
     */
    private String data;

    /**
     * 返回时间 yyyyMMdd HH:mm:ss
     */
    private Date responseTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public static HttpResponseDto build(String code, String msg, String data){
        HttpResponseDto resp = new HttpResponseDto();
        resp.setCode(code);
        resp.setMsg(msg);
        resp.setData(data);
        resp.setResponseTime(new Date());
        return resp;
    }

    public static HttpResponseDto build(ExceptionEnums exceptionEnums, String data){
        return build(exceptionEnums.code,exceptionEnums.msg,data);
    }

    public static HttpResponseDto build(ExceptionEnums exceptionEnums){
        return build(exceptionEnums,null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResponseDto{");
        sb.append("code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append(", responseTime='").append(responseTime).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
