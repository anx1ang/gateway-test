package com.zxk.service;


import com.zxk.exception.GatewayException;

/**
 * Created by wangyi on 2016/11/16.
 */
public interface GatewayService<REQ,RESP> {

    /**
     * 参数验证
     * @param req
     * @return
     */
    public void validate(REQ req) throws GatewayException;

    /**
     * 处理前
     * @param req
     */
    public void beforeHandler(REQ req) throws GatewayException;

    /**
     * 处理中
     * @param req
     * @return
     */
    public Object doHandler(REQ req) throws GatewayException;

    /**
     * 处理后
     * @param respDto
     */
    public RESP afterHandler(Object respDto) throws GatewayException;
}
