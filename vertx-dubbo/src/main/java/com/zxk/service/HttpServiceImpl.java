package com.zxk.service;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxk.entity.*;
import com.zxk.enums.ExceptionEnums;
import com.zxk.exception.GatewayException;
import com.zxk.sharedData.LocalDataMap;
import com.zxk.utils.JsonParser;
import com.zxk.utils.LogUtil;
import com.zxk.utils.VerfyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 业务线请求处理
 * <p/>
 * Created by wangyi on 2016/11/16.
 */
public class HttpServiceImpl implements GatewayService<HttpRequestDto, HttpResponseDto> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);


    /**
     * 参数验证
     *
     * @param req
     * @return
     */
    @Override
    public void validate(HttpRequestDto req) throws GatewayException {

    }

    /**
     * 处理前
     *
     * @param req
     */
    @Override
    public void beforeHandler(HttpRequestDto req) throws GatewayException{

    }

    /**
     * 处理中
     *
     * @param req
     * @return
     */
    @Override
    public Object doHandler(HttpRequestDto req) throws GatewayException{

        MethodInfo methodInfo = LocalDataMap.getMethodMap().get(req.getServiceCode());
        if(methodInfo==null){
            throw new GatewayException(ExceptionEnums.INVOKE_UNIMPL_METHOD);
        }
        Object params = null;
        try {
            params = JSONObject.parseObject(req.getContext(),methodInfo.getParam());
        }catch (JSONException je){
            logger.error("jSON字符串转换对象异常",je);
            throw new GatewayException(ExceptionEnums.PARAM_PARSE_OBJECT_ERROR);
        }catch (Exception e){
            logger.error("jSON字符串转换对象未知异常",e);
            throw new GatewayException(ExceptionEnums.PARAM_ERROR);
        }
        try {
            long start = System.currentTimeMillis();
            Object object = methodInfo.getMethodName().invoke(methodInfo.getInteface(),params);
            logger.info("invoke intelface cast {} millisecond",(System.currentTimeMillis()-start));
            return object;
        }catch (InvocationTargetException ite){
            logger.error("调用第三方接口异常",ite);
            throw new GatewayException(ExceptionEnums.INVOKE_METHOD_ERROR);
        }catch (Exception e){
            logger.error("接口调用失败",e);
            throw new GatewayException(ExceptionEnums.HANDLER_ERROR);
        }
    }

   /**
     * 处理后
     *
     * @param respDto
     */
    @Override
    public HttpResponseDto afterHandler(Object respDto) {
        return HttpResponseDto.build(ExceptionEnums.SUCCESS, JsonParser.serialize(respDto));
    }


}
