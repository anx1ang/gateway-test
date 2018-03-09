package com.zxk.service.impl;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxk.entity.*;
import com.zxk.enums.ExceptionEnums;
import com.zxk.exception.GatewayException;
import com.zxk.service.BizInfoService;
import com.zxk.service.BizKeyService;
import com.zxk.service.GatewayDetailService;
import com.zxk.service.GatewayService;
import com.zxk.utils.JsonParser;
import com.zxk.utils.LogUtil;
import com.zxk.utils.VerfyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 业务线请求处理
 * <p/>
 * Created by wangyi on 2016/11/16.
 */
public class HttpServiceImpl implements GatewayService<HttpRequestDto, HttpResponseDto> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BizInfoServiceImpl.class);


    @Resource
    private BizInfoService bizInfoServiceImpl;

    @Resource
    private BizKeyService bizKeyServiceImpl;

    @Resource
    private GatewayDetailService gatewayDetailServiceImpl;

    private final static Set<String> whiteListServiceCode;

    static {
        whiteListServiceCode = new HashSet<String>();
        whiteListServiceCode.add("com.qufenqi.insurance.facade.InsuranceNotifyFacade.tkRefundNotify");
        whiteListServiceCode.add("com.qufenqi.pay.loanrisk.facade.LoanRiskNotifyFacade.certifiedCallback");
        whiteListServiceCode.add("com.qufenqi.pay.loanrisk.facade.LoanRiskNotifyFacade.creditCallback");
        whiteListServiceCode.add("com.qufenqi.insurance.facade.InsuranceNotifyFacade.yaRefundNotify");
    }

    /**
     * 参数验证
     *
     * @param req
     * @return
     */
    @Override
    public void validate(HttpRequestDto req) throws GatewayException {
        if(req==null){
            throw new GatewayException(ExceptionEnums.REQUEST_NULL);
        }
        String bizCode = req.getBizCode();
        String serviceCode = req.getServiceCode();

        if(org.apache.commons.lang3.StringUtils.isBlank(bizCode)){
            throw new GatewayException(ExceptionEnums.REQ_BIZCODE_NULL);
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(req.getContext())){
            throw new GatewayException(ExceptionEnums.REQ_CONTEXT_NULL);
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(serviceCode)){
            throw new GatewayException(ExceptionEnums.REQ_SERVICECODE_NULL);
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(req.getSign())){
            throw new GatewayException(ExceptionEnums.REQ_SIGN_NULL);
        }

        BizInfo bizInfo = bizInfoServiceImpl.queryBizInfoByCache(bizCode);
        if(bizInfo==null){
            throw new GatewayException(ExceptionEnums.QUERY_BIZCODE_NULL);
        }
        /*if(bizDetailServiceImpl.checkBizDetail(bizCode,serviceCode)){
            throw new GatewayException(ExceptionEnums.QUERY_SERVICE_CODE_NULL);
        }
        if(bizInfo.getIsValidateIp()){
            if(!whiteIpServiceImpl.checkIp(req.getIp(),bizCode)){
                throw new GatewayException(ExceptionEnums.IP_NULL);
            }
        }*/
    }

    /**
     * 处理前
     *
     * @param req
     */
    @Override
    public void beforeHandler(HttpRequestDto req) throws GatewayException{
        req.setServiceCode(gatewayDetailServiceImpl.getServiceCodeByName(req.getServiceCode()));
        BizKey bizKey = bizKeyServiceImpl.getBizKey(req.getBizCode(),req.getKeyGroupName());
        if(!VerfyUtil.verfySign(bizKey.getSignType(),req.getContext(),req.getSign(),bizKey.getSignKey())){
            String logSignKey = bizKey.getSignKey();
            if (StringUtils.isNotBlank(logSignKey) && logSignKey.length()>=2) {
                logSignKey = logSignKey.substring(logSignKey.length()-2);
            }
            LogUtil.error(logger,"验签失败,SignType:{0},Context:{1},Sign:{2},SignKey:{3},bizKey:{4}",
                    bizKey.getSignType(),req.getContext(),req.getSign(),logSignKey,bizKey);
            throw new GatewayException(ExceptionEnums.UNSIGN_ERROR);
        }
        String context = VerfyUtil.decrypt(bizKey.getEncryptType(),req.getContext(),bizKey.getEncryptKey());
        logger.info("收到业务线请求:{}",context);
        req.setContext(context);
    }

    /**
     * 处理中
     *
     * @param req
     * @return
     */
    @Override
    public Object doHandler(HttpRequestDto req) throws GatewayException{
        if (!whiteListServiceCode.contains(req.getServiceCode())) {
            throw new GatewayException(ExceptionEnums.QUERY_SERVICE_CODE_NULL);
        }
        MethodInfo methodInfo = MethodMap.getMethodMap().get(req.getServiceCode());
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
